import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";

import usePagination, {
    PageVide,
} from "../../hooks/usePagination";
import {matieresApi} from "../../services/api/matieresApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {notesApi} from "../../services/api/notesApi.js";

export default function NotesAdministrateur() {
    const [pageNotes, setPageNotes] = useState(PageVide());
    const [matieres, setMatieres] = useState([]);
    const [devoirs, setDevoirs] = useState([]);
    const [eleves, setEleves] = useState([]);
    const [matiereId, setMatiereId] = useState("");
    const [rechercheEleve, setRechercheEleve] = useState("");
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(
        pageNotes,
        10,
        matiereId + "-" + rechercheEleve,
    );

    useEffect(() => {
        chargerReferences();
    }, []);

    useEffect(() => {
        chargerNotes();
    }, [matiereId, rechercheEleve, pagination.numeroPage, pagination.taillePage]);

    async function chargerReferences() {
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({ size: 1000 }),
                devoirsApi.getAll({ size: 1000 }),
                elevesApi.getAll({ size: 1000 }),
            ]);
            setMatieres(reponses[0].data.content);
            setDevoirs(reponses[1].data.content);
            setEleves(reponses[2].data.content);
        } catch (exception) {
            setErreur("Impossible de charger les références des notes.");
        }
    }

    async function chargerNotes() {
        setErreur("");
        try {
            const reponse = await notesApi.getAll({
                matiereId: matiereId || undefined,
                rechercheEleve: rechercheEleve || undefined,
                ...pagination.parametres,
            });
            setPageNotes(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les notes");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Notes</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <input
                    className="field search"
                    value={rechercheEleve}
                    onChange={(event) => setRechercheEleve(event.target.value)}
                    placeholder="Rechercher un élève..."
                />
                <select
                    className="field search"
                    value={matiereId}
                    onChange={(event) => setMatiereId(event.target.value)}
                >
                    <option value="">Toutes les matières</option>
                    {matieres.map((matiere) => (
                        <option key={matiere.id} value={matiere.id}>
                            {matiere.nom}
                        </option>
                    ))}
                </select>
            </div>
            <TableauNotes
                pagination={pagination}
                devoirs={devoirs}
                eleves={eleves}
                matieres={matieres}
            />
        </div>
    );
}

function TableauNotes({ pagination, devoirs, eleves, matieres }) {
    return (
        <div className="card table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Elève</th>
                    <th>Devoir</th>
                    <th>Matière</th>
                    <th>Note</th>
                    <th>Commentaire</th>
                    <th>Date</th>
                </tr>
                </thead>
                <tbody>
                {pagination.elementsPage.map((note) => {
                    const devoir = devoirs.find(
                        (element) => element.id === note.devoirId,
                    );
                    const eleve = eleves.find((element) => element.id === note.eleveId);
                    const matiere = matieres.find(
                        (element) => element.id === devoir?.matiereId,
                    );
                    return (
                        <tr key={note.id}>
                            <td>
                                {eleve ? eleve.prenom + " " + eleve.nom : "Élève #" + note.eleveId}
                            </td>
                            <td>{devoir?.titre || note.devoirId}</td>
                            <td>{matiere?.nom || "—"}</td>
                            <td>
                                <span>{note.valeur}/{note.valeurMaximale}</span>
                            </td>
                            <td>{note.commentaire || "—"}</td>
                            <td>{note.date}</td>
                        </tr>
                    );
                })}
                {pagination.totalElements === 0 && (
                    <tr>
                        <td>Aucune note disponible</td>
                    </tr>
                )}
                </tbody>
            </table>
            <Pagination pagination={pagination} />
        </div>
    );
}
