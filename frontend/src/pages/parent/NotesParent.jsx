import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";

import { useAuth } from "../../context/AuthContext";
import usePagination, {PageVide,} from "../../hooks/usePagination";
import {matieresApi} from "../../services/api/matieresApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {notesApi} from "../../services/api/notesApi.js";

export default function NotesParent() {
    const { user } = useAuth();
    const [pageNotes, setPageNotes] = useState(PageVide());
    const [matieres, setMatieres] = useState([]);
    const [devoirs, setDevoirs] = useState([]);
    const [enfant, setEnfant] = useState(null);
    const [eleveId, setEleveId] = useState("");
    const [matiereId, setMatiereId] = useState("");
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(pageNotes, 10, matiereId);

    useEffect(() => {
        preparer();
    }, [user.id]);

    useEffect(() => {
        if (!eleveId) return;
        chargerNotes();
    }, [eleveId, matiereId, pagination.numeroPage, pagination.taillePage]);

    async function preparer() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({ size: 1000 }),
                devoirsApi.getAll({ size: 1000 }),
            ]);
            setMatieres(reponses[0].data.content);
            setDevoirs(reponses[1].data.content);
            let enfantId = localStorage.getItem("enfantSelectionneId");
            if (!enfantId) {
                const enfants = await elevesApi.getByParent(user.id, { size: 1000 });
                enfantId = enfants.data.content[0]?.id;
                if (enfantId) localStorage.setItem("enfantSelectionneId", enfantId);
            }
            if (!enfantId) throw new Error("Aucun enfant lié au parent");
            const reponseEnfant = await elevesApi.getById(enfantId);
            setEnfant(reponseEnfant.data);
            setEleveId(enfantId);
        } catch (exception) {
            setErreur("Impossible de préparer les notes ");
        }
    }

    async function chargerNotes() {
        setErreur("");
        try {
            const reponse = await notesApi.getAll({
                eleveId: eleveId, matiereId: matiereId || undefined, ...pagination.parametres,});
            setPageNotes(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les notes ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Notes de mon enfant</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <select
                    className="field search" value={matiereId}
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
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Élève</th>
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
                        const matiere = matieres.find(
                            (element) => element.id === devoir?.matiereId,
                        );
                        return (
                            <tr key={note.id}>
                                <td>{enfant ? enfant.prenom + " " + enfant.nom : "—"}</td>
                                <td>{devoir?.titre || note.devoirId}</td>
                                <td>{matiere?.nom || "—"}</td>
                                <td>
                    <span className="badge">
                      {note.valeur}/{note.valeurMaximale}
                    </span>
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
        </div>
    );
}
