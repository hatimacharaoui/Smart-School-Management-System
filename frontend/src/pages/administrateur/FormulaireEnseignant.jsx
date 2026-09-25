import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";
import {matieresApi} from "../../services/api/matieresApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

const valeursInitiales = {
    prenom: "",
    nom: "",
    email: "",
    motDePasse: "",
    telephone: "",
    matiereId: "",
    classeIds: [],
    actif: true,
};

function creerSchema(estCreation) {
    return yup.object({
        prenom: yup.string().trim().required("Le prénom est obligatoire"),
        nom: yup.string().trim().required("Le nom est obligatoire"),
        email: yup
            .string()
            .email("Adresse email invalide")
            .required("L'adresse email est obligatoire"),
        motDePasse: estCreation
            ? yup
                .string()
                .min(5, "Le mot de passe doit contenir au moins 5 caractères")
                .max(72, "Le mot de passe ne doit pas dépasser 72 caractères")
                .required("Le mot de passe est obligatoire")
            : yup.string().notRequired(),
        telephone: yup.string().max(30, "30 caractères maximum"),
        matiereId: yup
            .number()
            .typeError("La matière est obligatoire")
            .required("La matière est obligatoire"),
        classeIds: yup
            .array()
            .min(1, "Sélectionnez au moins une classe")
            .required("Sélectionnez au moins une classe"),
    });
}

export default function FormulaireEnseignant() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [matieres, setMatieres] = useState([]);
    const [classes, setClasses] = useState([]);
    const [erreur, setErreur] = useState("");
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(creerSchema(!id)),
        defaultValues: valeursInitiales,
    });

    useEffect(() => {
        preparerFormulaire();
    }, [id]);

    async function preparerFormulaire() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({ size: 1000 }),
                classesApi.getAll({ size: 1000 }),
            ]);
            const matieresDisponibles = reponses[0].data.content;
            setMatieres(matieresDisponibles);
            setClasses(reponses[1].data.content);

            if (id) {
                const donnees = await Promise.all([
                    enseignantsApi.getById(id),
                    enseignantsApi.getClasses(id, { size: 1000 }),
                ]);
                reset({
                    ...donnees[0].data,
                    classeIds: donnees[1].data.content.map((affectation) =>
                        String(affectation.classeId),
                    ),
                });
            } else {
                reset({
                    ...valeursInitiales,
                    matiereId: matieresDisponibles[0]?.id || "",
                });
            }
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    async function enregistrer(valeurs) {
        setErreur("");
        const donnees = {
            prenom: valeurs.prenom,
            nom: valeurs.nom,
            email: valeurs.email,
            telephone: valeurs.telephone,
            matiereId: valeurs.matiereId,
            actif: valeurs.actif,
        };

        if (!id) {
            donnees.motDePasse = valeurs.motDePasse;
        }

        try {
            let enseignantId = id;
            if (id) {
                await enseignantsApi.update(id, donnees);
            } else {
                const reponse = await enseignantsApi.create(donnees);
                enseignantId = reponse.data.id;
            }

            await enseignantsApi.updateClasses(
                enseignantId,
                valeurs.classeIds.map(Number),
            );
            navigate("/enseignants");
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>{id ? "Modifier un enseignant" : "Ajouter un enseignant"}</h1>
            </header>

            {erreur && <p className="notice">{erreur}</p>}

            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                <div className="form-grid">
                    <Champ
                        nom="prenom"
                        label="Prénom"
                        register={register}
                        erreur={errors.prenom}
                    />
                    <Champ
                        nom="nom"
                        label="Nom"
                        register={register}
                        erreur={errors.nom}
                    />
                    <Champ
                        nom="email"
                        label="Email"
                        type="email"
                        register={register}
                        erreur={errors.email}
                    />
                    {!id && (
                        <Champ
                            nom="motDePasse"
                            label="Mot de passe"
                            type="password"
                            register={register}
                            erreur={errors.motDePasse}
                        />
                    )}
                    <Champ
                        nom="telephone"
                        label="Téléphone"
                        register={register}
                        erreur={errors.telephone}
                    />

                    <div className="form-group">
                        <label>Matière</label>
                        <select
                            className="field"
                            {...register("matiereId", { valueAsNumber: true })}
                        >
                            <option value="">Sélectionner une matière</option>
                            {matieres.map((matiere) => (
                                <option key={matiere.id} value={matiere.id}>
                                    {matiere.nom}
                                </option>
                            ))}
                        </select>
                        {errors.matiereId && (
                            <small className="error-text">{errors.matiereId.message}</small>
                        )}
                    </div>

                    <div className="form-group" style={{ gridColumn: "1 / -1" }}>
                        <label>Classes enseignées</label>
                        <div className="checkbox-list">
                            {classes.map((classe) => (
                                <label className="checkbox-item" key={classe.id}>
                                    <input
                                        type="checkbox"
                                        value={classe.id}
                                        {...register("classeIds")}
                                    />
                                    <span>{classe.nom}</span>
                                </label>
                            ))}
                        </div>
                        {errors.classeIds && (
                            <small className="error-text">{errors.classeIds.message}</small>
                        )}
                    </div>
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        className="button secondary"
                        onClick={() => navigate("/enseignants")}
                    >
                        Annuler
                    </button>
                    <button type="submit" className="button" disabled={isSubmitting}>
                        {isSubmitting ? "Enregistrement..." : "Enregistrer"}
                    </button>
                </div>
            </form>
        </div>
    );
}

function Champ({ nom, label, type = "text", register, erreur }) {
    return (
        <div className="form-group">
            <label>{label}</label>
            <input
                className="field"
                type={type}
                autoComplete={type === "password" ? "new-password" : undefined}
                {...register(nom)}
            />
            {erreur && <small className="error-text">{erreur.message}</small>}
        </div>
    );
}
