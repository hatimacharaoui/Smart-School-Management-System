import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";

import { useAuth } from "../../context/AuthContext";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";

const schema = yup.object({
    titre: yup
        .string()
        .trim()
        .max(180, "180 caractères maximum")
        .required("Le titre est obligatoire"),
    description: yup.string().max(1000, "1000 caractères maximum"),
    matiereId: yup
        .number()
        .typeError("La matière est obligatoire")
        .required("La matière est obligatoire"),
    classeId: yup
        .number()
        .typeError("La classe est obligatoire")
        .required("La classe est obligatoire"),
    enseignantId: yup
        .number()
        .typeError("L'enseignant est obligatoire")
        .required("L'enseignant est obligatoire"),
    dateLimite: yup.string().required("La date limite est obligatoire"),
    statut: yup.string().required("Le statut est obligatoire"),
});

export default function FormulaireDevoir() {
    const { user } = useAuth();
    const { id } = useParams();
    const navigate = useNavigate();
    const [classes, setClasses] = useState([]);
    const [matieres, setMatieres] = useState([]);
    const {
        register,
        handleSubmit,
        reset,
        watch,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
        defaultValues: {
            titre: "",
            description: "",
            matiereId: "",
            classeId: "",
            enseignantId: user.id,
            dateLimite: "",
            statut: "A_VENIR",
        },
    });
    const matiereId = watch("matiereId");

    useEffect(() => {
        preparer();
    }, [id, user.id]);

    async function preparer() {
        const reponses = await Promise.all([
            classesApi.getAll({ size: 1000 }),
            matieresApi.getAll({ size: 1000 }),
            enseignantsApi.getById(user.id),
            enseignantsApi.getClasses(user.id, { size: 1000 }),
        ]);
        const idsClasses = reponses[3].data.content.map((lien) => lien.classeId);
        const classesEnseignant = reponses[0].data.content.filter((classe) =>
            idsClasses.includes(classe.id),
        );
        setClasses(classesEnseignant);
        setMatieres(reponses[1].data.content);

        reset({
            titre: "",
            description: "",
            enseignantId: user.id,
            matiereId: reponses[2].data.matiereId || "",
            classeId: classesEnseignant[0]?.id || "",
            dateLimite: "",
            statut: "A_VENIR",
        });

        if (id) {
            const reponse = await devoirsApi.getById(id);
            reset(reponse.data);
        }
    }

    async function enregistrer(donnees) {
        if (id) await devoirsApi.update(id, donnees);
        else await devoirsApi.create(donnees);
        navigate("/devoirs");
    }

    return (
        <div>
            <header className="page-header">
                <h1>{id ? "Modifier le devoir" : "Créer un devoir"}</h1>
            </header>
            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                <div className="form-grid">
                    <div className="form-group">
                        <label>Titre</label>
                        <input className="field" {...register("titre")} />
                        {errors.titre && (
                            <small className="error-text">{errors.titre.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Matière de l’enseignant</label>
                        <select className="field" value={matiereId} disabled>
                            {matieres.map((matiere) => (
                                <option value={matiere.id} key={matiere.id}>
                                    {matiere.nom}
                                </option>
                            ))}
                        </select>
                        <input
                            type="hidden"
                            {...register("matiereId", { valueAsNumber: true })}
                        />
                        <input
                            type="hidden"
                            {...register("enseignantId", { valueAsNumber: true })}
                        />
                        {errors.matiereId && (
                            <small className="error-text">{errors.matiereId.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Classe</label>
                        <select
                            className="field"
                            {...register("classeId", { valueAsNumber: true })}
                        >
                            {classes.map((classe) => (
                                <option value={classe.id} key={classe.id}>
                                    {classe.nom}
                                </option>
                            ))}
                        </select>
                        {errors.classeId && (
                            <small className="error-text">{errors.classeId.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Date limite</label>
                        <input className="field" type="date" {...register("dateLimite")} />
                        {errors.dateLimite && (
                            <small className="error-text">{errors.dateLimite.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Statut</label>
                        <select className="field" {...register("statut")}>
                            <option value="A_VENIR">À venir</option>
                            <option value="EN_CORRECTION">En correction</option>
                            <option value="CORRIGE">Corrigé</option>
                        </select>
                        {errors.statut && (
                            <small className="error-text">{errors.statut.message}</small>
                        )}
                    </div>
                    <div className="form-group" style={{ gridColumn: "1/-1" }}>
                        <label>Description</label>
                        <textarea className="field" {...register("description")} />
                        {errors.description && (
                            <small className="error-text">{errors.description.message}</small>
                        )}
                    </div>
                </div>
                <div className="form-actions">
                    <button
                        type="button" className="button secondary"
                        onClick={() => navigate("/devoirs")}
                    >Annuler</button>
                    <button className="button" disabled={isSubmitting}>
                        {isSubmitting ? "Enregistrement..." : "Enregistrer"}
                    </button>
                </div>
            </form>
        </div>
    );
}
