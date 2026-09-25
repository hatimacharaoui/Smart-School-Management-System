import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { Link, useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";
import {classesApi} from "../../services/api/classesApi.js";
import {parentsApi} from "../../services/api/parentsApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
const empty = {
    matricule: "",
    prenom: "",
    nom: "",
    email: "",
    motDePasse: "",
    telephone: "",
    dateNaissance: "",
    adresse: "",
    classeId: 1,
    parentId: "",
    actif: true,
};

function creerSchema(estCreation) {
    return yup.object({
        matricule: yup
            .string()
            .trim()
            .required("Le numéro d'élève est obligatoire"),
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
        dateNaissance: yup
            .string()
            .required("La date de naissance est obligatoire")
            .test(
                "date-passee",
                "La date doit être dans le passé",
                (valeur) => !valeur || new Date(valeur) < new Date(),
            ),
        adresse: yup.string().max(255, "255 caractères maximum"),
        classeId: yup
            .number()
            .typeError("La classe est obligatoire")
            .required("La classe est obligatoire"),
        parentId: yup
            .number()
            .transform((valeur, original) => (original === "" ? null : valeur))
            .typeError("Le parent est obligatoire")
            .required("Le parent est obligatoire"),
    });
}

export default function FormulaireEleve({ lectureSeule = false }) {
    const [classes, setClasses] = useState([]);
    const [parents, setParents] = useState([]);
    const [rechercheParent, setRechercheParent] = useState("");
    const [afficherParents, setAfficherParents] = useState(false);
    const { id } = useParams();
    const navigate = useNavigate();
    const {
        register,
        handleSubmit,
        reset,
        setValue,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(creerSchema(!id)),
        defaultValues: empty,
    });
    useEffect(
        function () {
            preparerFormulaire();
        },
        [id],
    );
    async function preparerFormulaire() {
        const reponseClasses = await classesApi.getAll({ size: 1000 });
        setClasses(reponseClasses.data.content);
        const reponseParents = await parentsApi.getAll({ size: 1000 });
        const parentsDisponibles = reponseParents.data.content;
        setParents(parentsDisponibles);
        if (id) {
            const response = await elevesApi.getById(id);
            reset(response.data);
            const parent = parentsDisponibles.find(
                (element) => element.id === response.data.parentId,
            );
            setRechercheParent(parent ? nomParent(parent) : "");
        } else {
            reset({
                ...empty,
                classeId: reponseClasses.data.content[0]?.id || "",
                parentId: "",
            });
            setRechercheParent("");
        }
    }
    async function enregistrer(donnees) {
        if (id) {
            const donneesSansMotDePasse = { ...donnees };
            delete donneesSansMotDePasse.motDePasse;
            await elevesApi.update(id, donneesSansMotDePasse);
        } else {
            await elevesApi.create(donnees);
        }
        navigate("/eleves");
    }

    return (
        <div>
            <header className="page-header">
                <h1>
                    {lectureSeule
                        ? "Détails de l'élève"
                        : id
                            ? "Modifier l'élève"
                            : "Ajouter un élève"}
                </h1>
            </header>
            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                <div className="form-grid">
                    {[
                        ["matricule", "N° élève"],
                        ["prenom", "Prénom"],
                        ["nom", "Nom"],
                        ["email", "Email"],
                        ...(!id && !lectureSeule
                            ? [["motDePasse", "Mot de passe", "password"]]
                            : []),
                        ["telephone", "Téléphone"],
                        ["dateNaissance", "Date de naissance", "date"],
                        ["adresse", "Adresse"],
                    ].map((field) => (
                        <div className="form-group" key={field[0]}>
                            <label>{field[1]}</label>
                            <input
                                className="field"
                                type={field[2] || "text"}
                                disabled={lectureSeule}
                                {...register(field[0])}
                            />
                            {errors[field[0]] && (
                                <small className="error-text">{errors[field[0]].message}</small>
                            )}
                        </div>
                    ))}
                    <div className="form-group">
                        <label>Classe</label>
                        <select
                            className="field"
                            disabled={lectureSeule}
                            {...register("classeId", { valueAsNumber: true })}
                        >
                            {classes.map((classe) => (
                                <option key={classe.id} value={classe.id}>
                                    {classe.nom}
                                </option>
                            ))}
                        </select>
                        {errors.classeId && (
                            <small className="error-text">{errors.classeId.message}</small>
                        )}
                    </div>
                    <div className="form-group autocomplete">
                        <label>Rechercher le parent</label>
                        <input
                            className="field"
                            disabled={lectureSeule}
                            value={rechercheParent}
                            placeholder="Nom, email ou téléphone du parent"
                            onFocus={() => setAfficherParents(true)}
                            onChange={(event) => {
                                setRechercheParent(event.target.value);
                                setValue("parentId", "", { shouldValidate: true });
                                setAfficherParents(true);
                            }}
                        />
                        <input
                            type="hidden"
                            {...register("parentId", { valueAsNumber: true })}
                        />
                        {!lectureSeule && afficherParents && rechercheParent && (
                            <div className="autocomplete-results">
                                {filtrerParents(parents, rechercheParent).map((parent) => (
                                    <button
                                        type="button"
                                        className="autocomplete-option"
                                        key={parent.id}
                                        onClick={() => {
                                            setValue("parentId", parent.id, {
                                                shouldValidate: true,
                                            });
                                            setRechercheParent(nomParent(parent));
                                            setAfficherParents(false);
                                        }}
                                    >
                                        <strong>{nomParent(parent)}</strong>
                                        <small>{parent.email}</small>
                                    </button>
                                ))}
                                {filtrerParents(parents, rechercheParent).length === 0 && (
                                    <p className="autocomplete-empty">Aucun parent trouvé.</p>
                                )}
                            </div>
                        )}
                        {errors.parentId && (
                            <small className="error-text">{errors.parentId.message}</small>
                        )}
                    </div>
                </div>
                <div className="form-actions">
                    <Link className="button secondary" to="/eleves">
                        Retour
                    </Link>
                    {!lectureSeule && (
                        <button className="button" disabled={isSubmitting}>
                            {isSubmitting ? "Enregistrement..." : "Enregistrer"}
                        </button>
                    )}
                </div>
            </form>
        </div>
    );
}

function nomParent(parent) {
    return parent.prenom + " " + parent.nom;
}

function filtrerParents(parents, recherche) {
    const valeur = recherche.trim().toLowerCase();
    if (!valeur) return [];
    return parents
        .filter((parent) => {
            const contenu = [
                nomParent(parent),
                parent.email || "",
                parent.telephone || "",
            ]
                .join(" ")
                .toLowerCase();
            return contenu.includes(valeur);
        })
        .slice(0, 8);
}
