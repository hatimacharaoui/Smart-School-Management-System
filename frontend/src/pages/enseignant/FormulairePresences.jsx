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
import {presencesApi} from "../../services/api/presencesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";

const schema = yup.object({
    classeId: yup
        .number()
        .typeError("La classe est obligatoire")
        .required("La classe est obligatoire"),
    date: yup.string().required("La date est obligatoire"),
    recherche: yup.string(),
    statuts: yup.object().required(),
});

export default function FormulairePresences() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [classes, setClasses] = useState([]);
    const [matiereId, setMatiereId] = useState(null);
    const [pageEleves, setPageEleves] = useState(PageVide(20));
    const [message, setMessage] = useState("");
    const {
        register,
        handleSubmit,
        setValue,
        watch,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
        defaultValues: {
            classeId: "",
            date: dateAujourdhui(),
            recherche: "",
            statuts: {},
        },
    });
    const classeId = watch("classeId");
    const date = watch("date");
    const recherche = watch("recherche");
    const pagination = usePagination(
        pageEleves, 20, classeId + "-" + date + "-" + recherche);

    useEffect(() => {
        preparer();
    }, [user.id]);

    useEffect(() => {
        if (classeId) chargerClasse();
    }, [classeId, date, recherche, pagination.numeroPage, pagination.taillePage]);

    async function preparer() {
        setMessage("");
        try {
            const reponses = await Promise.all([
                classesApi.getAll({ size: 1000 }),
                enseignantsApi.getClasses(user.id, { size: 1000 }),
                enseignantsApi.getById(user.id),
            ]);
            const ids = reponses[1].data.content.map((lien) => lien.classeId);
            const classesEnseignant = reponses[0].data.content.filter((classe) =>
                ids.includes(classe.id),
            );
            setClasses(classesEnseignant);
            setMatiereId(reponses[2].data.matiereId);
            setValue("classeId", classesEnseignant[0]?.id || "");
        } catch (exception) {
            setMessage("Impossible de charger les classes ");
        }
    }

    async function chargerClasse() {
        setMessage("");
        try {
            const reponses = await Promise.all([
                elevesApi.getByClasse(classeId, {
                    recherche, ...pagination.parametres,}),
                presencesApi.getByClasse(classeId, { date, size: 1000 }),
            ]);
            setPageEleves(reponses[0].data);
            const statuts = {};
            reponses[0].data.content.forEach((eleve) => {
                const presence = reponses[1].data.content.find(
                    (element) => element.eleveId === eleve.id,
                );
                statuts[eleve.id] = presence?.statut || "PRESENT";
            });
            setValue("statuts", statuts);
        } catch (exception) {
            setMessage("Impossible de charger les élèves ");
        }
    }

    async function enregistrer(valeurs) {
        setMessage("");
        const donnees = pagination.elementsPage.map((eleve) => ({
            eleveId: eleve.id,
            classeId: valeurs.classeId,
            matiereId,
            enseignantId: user.id,
            date: valeurs.date,
            statut: valeurs.statuts[eleve.id] || "PRESENT",
        }));

        try {
            await presencesApi.createGroup(donnees);
            navigate("/presences");
        } catch (exception) {
            const messageApi = exception.response?.data?.message;
            setMessage(messageApi || "Impossible d’enregistrer les présences ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Enregistrer les présences</h1>
            </header>
            {message && <p className="notice erreur">{message}</p>}
            <form onSubmit={handleSubmit(enregistrer)} noValidate>
                <div className="filters">
                    <select
                        className="field search"
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
                    <input className="field search" type="date" {...register("date")} />
                    {errors.date && (
                        <small className="error-text">{errors.date.message}</small>
                    )}
                    <input
                        className="field search" placeholder="Rechercher un élève"{...register("recherche")}/>
                </div>
                <div className="card table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Élève</th>
                            <th>Classe</th>
                            <th>Date</th>
                            <th>Statut</th>
                        </tr>
                        </thead>
                        <tbody>
                        {pagination.elementsPage.map((eleve) => (
                            <tr key={eleve.id}>
                                <td>{eleve.prenom + " " + eleve.nom}</td>
                                <td>
                                    {classes.find(
                                        (classe) => String(classe.id) === String(classeId),
                                    )?.nom || "—"}
                                </td>
                                <td>{date}</td>
                                <td>
                                    <select
                                        className="field"{...register("statuts." + eleve.id)}>
                                        <option value="PRESENT">Présent</option>
                                        <option value="ABSENT">Absent</option>
                                        <option value="EN_RETARD">En retard</option>
                                    </select>
                                </td>
                            </tr>
                        ))}
                        {pagination.totalElements === 0 && (
                            <tr>
                                <td>Aucun élève trouvé</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                    <Pagination pagination={pagination} />
                </div>
                <div className="form-actions">
                    <button
                        type="button" className="button secondary"
                        onClick={() => navigate("/presences")}
                    >Annuler</button>
                    <button className="button" disabled={isSubmitting || !matiereId}>
                        {isSubmitting ? "Enregistrement..." : "Enregistrer les présences"}
                    </button>
                </div>
            </form>
        </div>
    );
}

function dateAujourdhui() {
    const date = new Date();
    const annee = date.getFullYear();
    const mois = String(date.getMonth() + 1).padStart(2, "0");
    const jour = String(date.getDate()).padStart(2, "0");
    return annee + "-" + mois + "-" + jour;
}
