import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";
import {matieresApi} from "../../services/api/matieresApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {parentsApi} from "../../services/api/parentsApi.js";

const configs = {
    enseignants: {
        ajout: "Ajouter un enseignant",
        modification: "Modifier un enseignant",
        fields: [
            ["prenom", "Prénom"],
            ["nom", "Nom"],
            ["email", "Email", "email"],
            ["telephone", "Téléphone"],
            ["matiereId", "Matière", "select-matiere"],
        ],
        defaults: {
            prenom: "",
            nom: "",
            email: "",
            telephone: "",
            matiereId: 1,
            classeIds: [],
            actif: true,
        },
    },
    parents: {
        ajout: "Ajouter un parent",
        modification: "Modifier un parent",
        fields: [
            ["prenom", "Prénom"],
            ["nom", "Nom"],
            ["email", "Email", "email"],
            ["telephone", "Téléphone"],
        ],
        defaults: {
            prenom: "",
            nom: "",
            email: "",
            telephone: "",
            actif: true,
        },
    },
    classes: {
        ajout: "Ajouter une classe",
        modification: "Modifier une classe",
        fields: [
            ["nom", "Nom de la classe"],
            ["niveau", "Niveau"],
        ],
        defaults: { nom: "", niveau: "" },
    },
    matieres: {
        ajout: "Ajouter une matière",
        modification: "Modifier une matière",
        fields: [
            ["nom", "Nom de la matière"],
            ["coefficient", "Coefficient", "number"],
        ],
        defaults: { nom: "", coefficient: 1, actif: true },
    },
};

const schemas = {
    enseignants: yup.object({
        prenom: yup.string().trim().required("Le prénom est obligatoire"),
        nom: yup.string().trim().required("Le nom est obligatoire"),
        email: yup
            .string()
            .email("Adresse email invalide")
            .required("L'adresse email est obligatoire"),
        telephone: yup.string().max(30, "30 caractères maximum"),
        matiereId: yup
            .number()
            .typeError("La matière est obligatoire")
            .required("La matière est obligatoire"),
        classeIds: yup
            .array()
            .min(1, "Sélectionnez au moins une classe")
            .required("Sélectionnez au moins une classe"),
    }),
    parents: yup.object({
        prenom: yup.string().trim().required("Le prénom est obligatoire"),
        nom: yup.string().trim().required("Le nom est obligatoire"),
        email: yup
            .string()
            .email("Adresse email invalide")
            .required("L'adresse email est obligatoire"),
        telephone: yup.string().max(30, "30 caractères maximum"),
    }),
    classes: yup.object({
        nom: yup.string().trim().required("Le nom est obligatoire"),
        niveau: yup.string().trim().required("Le niveau est obligatoire"),
    }),
    matieres: yup.object({
        nom: yup.string().trim().required("Le nom est obligatoire"),
        coefficient: yup
            .number()
            .typeError("Le coefficient est obligatoire")
            .integer("Le coefficient doit être un nombre entier")
            .positive("Le coefficient doit être positif")
            .required("Le coefficient est obligatoire"),
    }),
};

export default function FormulaireEnseignant({ type }) {
    const config = configs[type];
    const { id } = useParams();
    const [matieres, setMatieres] = useState([]);
    const [classes, setClasses] = useState([]);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schemas[type]),
        defaultValues: config.defaults,
    });

    useEffect(() => {
        preparer();
    }, [type, id]);

    async function preparer() {
        setMessage("");
        try {
            if (type === "enseignants") {
                const reponses = await Promise.all([
                    matieresApi.getAll({ size: 1000 }),
                    classesApi.getAll({ size: 1000 }),
                ]);
                setMatieres(reponses[0].data.content);
                setClasses(reponses[1].data.content);
            }

            if (id) {
                const serviceAPI = obtenirServiceAPI(type);
                if (type === "enseignants") {
                    const reponses = await Promise.all([
                        serviceAPI.getById(id),
                        enseignantsApi.getClasses(id, { size: 1000 }),
                    ]);
                    reset({...reponses[0].data,
                        classeIds: reponses[1].data.content.map((lien) =>
                            String(lien.classeId)),
                    });
                } else {
                    const reponse = await serviceAPI.getById(id);
                    reset(reponse.data);
                }
            } else {
                reset(config.defaults);
            }
        } catch (error) {
            setMessage("Impossible de charger les informations ");
        }
    }

    async function enregistrer(valeurs) {
        setMessage("");
        try {
            const { classeIds = [], ...donnees } = valeurs;
            let identifiant = id;
            if (id) {
                const serviceAPI = obtenirServiceAPI(type);
                await serviceAPI.update(id, donnees);
            } else {
                const serviceAPI = obtenirServiceAPI(type);
                const reponse = await serviceAPI.create(donnees);
                identifiant = reponse.data.id;
            }
            if (type === "enseignants") {
                await enseignantsApi.updateClasses(identifiant, classeIds.map(Number));
            }
            navigate("/" + type);
        } catch (error) {
            setMessage("Impossible d'enregistrer les informations ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>{id ? config.modification : config.ajout}</h1>
            </header>
            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                {message && <p className="badge red">{message}</p>}
                <div className="form-grid">
                    {config.fields.map(([name, label, inputType]) => (
                        <div className="form-group" key={name}>
                            <label>{label}</label>
                            {inputType === "select-matiere" ? (
                                <select
                                    className="field"
                                    {...register(name, { valueAsNumber: true })}
                                >
                                    {matieres.map((matiere) => (
                                        <option key={matiere.id} value={matiere.id}>
                                            {matiere.nom}
                                        </option>
                                    ))}
                                </select>
                            ) : (
                                <input
                                    className="field"
                                    type={inputType || "text"}
                                    {...register(name, {
                                        valueAsNumber: inputType === "number",
                                    })}
                                />
                            )}
                            {errors[name] && (
                                <small className="error-text">{errors[name].message}</small>
                            )}
                        </div>
                    ))}
                    {type === "enseignants" && (
                        <div className="form-group" style={{ gridColumn: "1/-1" }}>
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
                    )}
                </div>
                <div className="form-actions">
                    <button
                        type="button"
                        className="button secondary"
                        onClick={() => navigate("/" + type)}
                    >Annuler
                    </button>
                    <button className="button" disabled={isSubmitting}>
                        {isSubmitting ? "Enregistrement..." : "Enregistrer"}
                    </button>
                </div>
            </form>
        </div>
    );
}

function obtenirServiceAPI(type) {
    if (type === "enseignants") return enseignantsApi;
    if (type === "parents") return parentsApi;
    if (type === "classes") return classesApi;
    return matieresA;
}
