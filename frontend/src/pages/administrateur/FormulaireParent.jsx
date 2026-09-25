import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";
import {parentsApipi} from "../../services/api/parentsApipi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";
import {parentsApi} from "../../services/api/parentsApi.js";

const valeursInitiales = {
    prenom: "",
    nom: "",
    email: "",
    motDePasse: "",
    telephone: "",
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
    });
}

export default function FormulaireParent() {
    const { id } = useParams();
    const navigate = useNavigate();
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
        if (!id) {
            reset(valeursInitiales);
            return;
        }

        try {
            const reponse = await parentsApipi.getById(id);
            reset(reponse.data);
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
            actif: valeurs.actif,
        };

        if (!id) {
            donnees.motDePasse = valeurs.motDePasse;
        }

        try {
            if (id) {
                await parentsApi.update(id, donnees);
            } else {
                await parentsApi.create(donnees);
            }
            navigate("/parents");
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>{id ? "Modifier un parent" : "Ajouter un parent"}</h1>
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
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        className="button secondary"
                        onClick={() => navigate("/parents")}
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
