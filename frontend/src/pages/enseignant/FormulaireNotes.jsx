import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import * as yup from "yup";
import Pagination from "../../components/Pagination";

import { useAuth } from "../../context/AuthContext";
import usePagination, {PageVide,} from "../../hooks/usePagination";
import {classesApi} from "../../services/api/classesApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {notesApi} from "../../services/api/notesApi.js";

const schemaNote = yup.object({
    valeur: yup
        .number()
        .typeError("La note est obligatoire")
        .min(0, "La note minimale est 0")
        .max(20, "La note maximale est 20")
        .required("La note est obligatoire"),
    commentaire: yup.string().max(500, "500 caractères maximum"),
});

const schema = yup.object({
    devoirId: yup
        .number()
        .typeError("Le devoir est obligatoire")
        .required("Le devoir est obligatoire"),
    notes: yup.lazy((valeurs = {}) => {
        const champs = {};
        Object.keys(valeurs).forEach((eleveId) => {
            champs[eleveId] = schemaNote;
        });
        return yup.object(champs);
    }),
});

export default function FormulaireNotes() {
    const { user } = useAuth();
    const [classes, setClasses] = useState([]);
    const [classeId, setClasseId] = useState("");
    const [devoirs, setDevoirs] = useState([]);
    const [pageEleves, setPageEleves] = useState(PageVide(20));
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const paginationEleves = usePagination(pageEleves, 20, classeId);
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
        defaultValues: { devoirId: "", notes: {} },
    });

    useEffect(() => {
        chargerClasses();
    }, []);

    useEffect(() => {
        if (classeId) chargerClasse();
    }, [classeId, paginationEleves.numeroPage, paginationEleves.taillePage]);

    async function chargerClasses() {
        const liens = await enseignantsApi.getClasses(user.id);
        const toutesLesClasses = await classesApi.getAll({ size: 1000 });
        const identifiants = liens.data.content.map((lien) => lien.classeId);
        const classesEnseignant = toutesLesClasses.data.content.filter((classe) =>
            identifiants.includes(classe.id),
        );
        setClasses(classesEnseignant);
        if (classesEnseignant.length > 0) setClasseId(classesEnseignant[0].id);
    }

    async function chargerClasse() {
        const reponses = await Promise.all([
            elevesApi.getByClasse(classeId, paginationEleves.parametres),
            devoirsApi.getByClasse(classeId, { size: 1000 }),
        ]);
        setPageEleves(reponses[0].data);
        const devoirsEnseignant = reponses[1].data.content.filter(
            (devoir) => devoir.enseignantId === user.id,
        );
        setDevoirs(devoirsEnseignant);
        const notesVides = {};
        reponses[0].data.content.forEach((eleve) => {
            notesVides[eleve.id] = { valeur: "", commentaire: "" };
        });
        reset({
            devoirId: devoirsEnseignant[0]?.id || "",
            notes: notesVides,
        });
    }

    async function enregistrer(valeurs) {
        setMessage("");
        const notes = paginationEleves.elementsPage.map((eleve) => ({
            eleveId: eleve.id,
            valeur: valeurs.notes[eleve.id].valeur,
            commentaire: valeurs.notes[eleve.id].commentaire || "",
        }));
        try {
            await notesApi.createGroup({
                devoirId: valeurs.devoirId,
                enseignantId: user.id,
                notes: notes,
            });
            navigate("/notes");
        } catch (exception) {
            setMessage("Impossible d'enregistrer les notes ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Ajouter les notes</h1>
            </header>
            {message && <p className="notice erreur">{message}</p>}
            <form onSubmit={handleSubmit(enregistrer)} noValidate>
                <div className="filters">
                    <select
                        className="field search" value={classeId}
                        onChange={(event) => setClasseId(event.target.value)}
                    >
                        {classes.map((classe) => (
                            <option value={classe.id} key={classe.id}>
                                {classe.nom}
                            </option>
                        ))}
                    </select>
                    <select
                        className="field search"
                        {...register("devoirId", { valueAsNumber: true })}
                    >
                        <option value="">Sélectionner un devoir</option>
                        {devoirs.map((devoir) => (
                            <option value={devoir.id} key={devoir.id}>
                                {devoir.titre}
                            </option>
                        ))}
                    </select>
                    {errors.devoirId && (
                        <small className="error-text">{errors.devoirId.message}</small>
                    )}
                </div>
                <div className="card table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Elève</th>
                            <th>Note /20</th>
                            <th>Commentaire</th>
                        </tr>
                        </thead>
                        <tbody>
                        {paginationEleves.elementsPage.map((eleve) => (
                            <tr key={eleve.id}>
                                <td>
                                    {eleve.prenom} {eleve.nom}
                                </td>
                                <td>
                                    <input
                                        className="field" style={{ maxWidth: 110 }} type="number" min="0" max="20"
                                        {...register(`notes.${eleve.id}.valeur`, {
                                            valueAsNumber: true,
                                        })}
                                    />
                                    {errors.notes?.[eleve.id]?.valeur && (
                                        <small className="error-text">
                                            {errors.notes[eleve.id].valeur.message}
                                        </small>
                                    )}
                                </td>
                                <td>
                                    <input
                                        className="field" placeholder="Commentaire pédagogique"
                                        {...register(`notes.${eleve.id}.commentaire`)}
                                    />
                                    {errors.notes?.[eleve.id]?.commentaire && (
                                        <small className="error-text">
                                            {errors.notes[eleve.id].commentaire.message}
                                        </small>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <Pagination pagination={paginationEleves} />
                </div>
                <div className="form-actions">
                    <button
                        type="button" className="button secondary"
                        onClick={() => navigate("/notes")}
                    >Annuler</button>
                    <button className="button" disabled={isSubmitting}>
                        {isSubmitting ? "Enregistrement..." : "Enregistrer les notes"}
                    </button>
                </div>
            </form>
        </div>
    );
}
