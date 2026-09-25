import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";
import {classesApi} from "../../services/api/classesApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";
import {matieresApi} from "../../services/api/matieresApi.js";


const configurations = {
    classes: {
        ajout: "Ajouter une classe",
        modification: "Modifier une classe",
        champs: [
            ["nom", "Nom de la classe"],
            ["niveau", "Niveau"],
        ],
        valeursInitiales: { nom: "", niveau: "" },
        schema: yup.object({
            nom: yup.string().trim().required("Le nom est obligatoire"),
            niveau: yup.string().trim().required("Le niveau est obligatoire"),
        }),
        service: classesApi,
    },
    matieres: {
        ajout: "Ajouter une matière",
        modification: "Modifier une matière",
        champs: [
            ["nom", "Nom de la matière"],
            ["coefficient", "Coefficient", "number"],
        ],
        valeursInitiales: { nom: "", coefficient: 1, actif: true },
        schema: yup.object({
            nom: yup.string().trim().required("Le nom est obligatoire"),
            coefficient: yup
                .number()
                .typeError("Le coefficient est obligatoire")
                .integer("Le coefficient doit être un nombre entier")
                .positive("Le coefficient doit être positif")
                .required("Le coefficient est obligatoire"),
        }),
        service: matieresApi,
    },
};

export default function FormulaireRessource({ type }) {
    const configuration = configurations[type];
    const { id } = useParams();
    const navigate = useNavigate();
    const [erreur, setErreur] = useState("");
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(configuration.schema),
        defaultValues: configuration.valeursInitiales,
    });

    useEffect(() => {
        preparerFormulaire();
    }, [type, id]);

    async function preparerFormulaire() {
        setErreur("");
        if (!id) {
            reset(configuration.valeursInitiales);
            return;
        }

        try {
            const reponse = await configuration.service.getById(id);
            reset(reponse.data);
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    async function enregistrer(valeurs) {
        setErreur("");
        try {
            if (id) {
                await configuration.service.update(id, valeurs);
            } else {
                await configuration.service.create(valeurs);
            }
            navigate("/" + type);
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>{id ? configuration.modification : configuration.ajout}</h1>
            </header>

            {erreur && <p className="notice">{erreur}</p>}

            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                <div className="form-grid">
                    {configuration.champs.map(([nom, label, typeChamp]) => (
                        <div className="form-group" key={nom}>
                            <label>{label}</label>
                            <input
                                className="field"
                                type={typeChamp || "text"}
                                {...register(nom, {
                                    valueAsNumber: typeChamp === "number",
                                })}
                            />
                            {errors[nom] && (
                                <small className="error-text">{errors[nom].message}</small>
                            )}
                        </div>
                    ))}
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        className="button secondary"
                        onClick={() => navigate("/" + type)}
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
