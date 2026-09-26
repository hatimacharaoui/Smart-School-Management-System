import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";
import usePagination, {
    PageVide,
} from "../../hooks/usePagination";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {presencesApi} from "../../services/api/presencesApi.js";

export default function PresencesAdministrateur() {
    const [classes, setClasses] = useState([]);
    const [classeId, setClasseId] = useState("");
    const [date, setDate] = useState(dateAujourdhui());
    const [recherche, setRecherche] = useState("");
    const [pageEleves, setPageEleves] = useState(PageVide());
    const [presences, setPresences] = useState([]);
    const [message, setMessage] = useState("");
    const pagination = usePagination(
        pageEleves,
        10,
        classeId + "-" + date + "-" + recherche,
    );

    useEffect(() => {
        preparer();
    }, []);

    useEffect(() => {
        if (classeId) chargerClasse();
    }, [classeId, date, recherche, pagination.numeroPage, pagination.taillePage]);

    async function preparer() {
        setMessage("");
        try {
            const reponse = await classesApi.getAll({ size: 1000 });
            const classesAutorisees = reponse.data.content;
            setClasses(classesAutorisees);
            setClasseId(classesAutorisees[0]?.id || "");
            
        } catch (exception) {
            setMessage("Impossible de charger les classes.");
        }
    }

    async function chargerClasse() {
        setMessage("");
        try {
            const reponses = await Promise.all([
                elevesApi.getByClasse(classeId, {recherche, ...pagination.parametres,}),
                presencesApi.getByClasse(classeId, { date, size: 1000 }),
            ]);
            setPageEleves(reponses[0].data);
            setPresences(reponses[1].data.content);
            
        } catch (exception) {
            setMessage("Impossible de charger la classe sélectionnée.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Présences</h1>
            </header>
            {message && <p className="notice">{message}</p>}
            <div className="filters">
                <select
                    className="field search"
                    value={classeId}
                    onChange={(event) => setClasseId(event.target.value)}
                >
                    {classes.map((classe) => (
                        <option value={classe.id} key={classe.id}>
                            {classe.nom}
                        </option>
                    ))}
                </select>
                <input
                    className="field search"
                    type="date"
                    value={date}
                    onChange={(event) => setDate(event.target.value)}
                />
                <input
                    className="field search"
                    value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher un élève..."
                />
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
                            <td>{nomClasse(classes, eleve.classeId)}</td>
                            <td>{date}</td>
                            <td>
                                <Statut
                                    statut={presences.find((presence) => presence.eleveId === eleve.id,)?.statut}
                                />
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucun élève trouvé.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function Statut({ statut }) {
    if (statut === "PRESENT") return <span>Présent</span>;
    if (statut === "ABSENT") return <span>Absent</span>;
    if (statut === "EN_RETARD") {
        return <span>En retard</span>;
    }
    return <span>Non enregistré</span>;
}

function nomClasse(classes, id) {
    return classes.find((classe) => classe.id === id)?.nom || id || "—";
}

function dateAujourdhui() {
    const date = new Date();
    const annee = date.getFullYear();
    const mois = String(date.getMonth() + 1).padStart(2, "0");
    const jour = String(date.getDate()).padStart(2, "0");
    return annee + "-" + mois + "-" + jour;
}
