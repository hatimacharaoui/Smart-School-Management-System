import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";

import { useAuth } from "../../context/AuthContext";
import usePagination, {PageVide,} from "../../hooks/usePagination";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {notesApi} from "../../services/api/notesApi.js";

export default function NotesEnseignant() {
    const { user } = useAuth();
    const [pageNotes, setPageNotes] = useState(PageVide());
    const [matieres, setMatieres] = useState([]);
    const [devoirs, setDevoirs] = useState([]);
    const [eleves, setEleves] = useState([]);
    const [classes, setClasses] = useState([]);
    const [classeId, setClasseId] = useState("");
    const [devoirId, setDevoirId] = useState("");
    const [modification, setModification] = useState(null);
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(
        pageNotes,
        10,
        classeId + "-" + devoirId,
    );

    useEffect(() => {
        chargerReferences();
    }, [user.id]);

    useEffect(() => {
        chargerNotes();
    }, [
        user.id,
        classeId,
        devoirId,
        pagination.numeroPage,
        pagination.taillePage,
    ]);

    async function chargerReferences() {
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({ size: 1000 }),
                devoirsApi.getAll({ size: 1000 }),
                elevesApi.getAll({ size: 1000 }),
                classesApi.getAll({ size: 1000 }),
                enseignantsApi.getClasses(user.id, { size: 1000 }),
            ]);
            const idsClasses = reponses[4].data.content.map((lien) => lien.classeId);
            setMatieres(reponses[0].data.content);
            setDevoirs(reponses[1].data.content);
            setEleves(reponses[2].data.content);
            setClasses(
                reponses[3].data.content.filter((classe) =>
                    idsClasses.includes(classe.id),
                ),
            );
        } catch (exception) {
            setErreur("Impossible de charger les références des notes ");
        }
    }

    async function chargerNotes() {
        setErreur("");
        try {
            const reponse = await notesApi.getAll({
                enseignantId: user.id,
                classeId: classeId || undefined,
                devoirId: devoirId || undefined,
                ...pagination.parametres,
            });
            setPageNotes(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les notes ");
        }
    }

    async function enregistrer(note) {
        try {
            await notesApi.update(note.id, {
                ...note,
                valeur: Number(modification.valeur),
                commentaire: modification.commentaire,
            });
            setModification(null);
            await chargerNotes();
        } catch (exception) {
            setErreur("Impossible de modifier la note ");
        }
    }

    const devoirsEnseignant = devoirs.filter(
        (devoir) => devoir.enseignantId === user.id,
    );

    return (
        <div>
            <header className="page-header">
                <h1>Notes de mes classes</h1>
                <Link className="button" to="/notes/add">Ajouter les notes</Link>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <select
                    className="field search"
                    value={classeId}
                    onChange={(event) => {
                        setClasseId(event.target.value);
                        setDevoirId("");
                    }}
                >
                    <option value="">Toutes mes classes</option>
                    {classes.map((classe) => (
                        <option key={classe.id} value={classe.id}>
                            {classe.nom}
                        </option>
                    ))}
                </select>
                <select
                    className="field search"
                    value={devoirId}
                    onChange={(event) => setDevoirId(event.target.value)}
                >
                    <option value="">Tous mes devoirs</option>
                    {devoirsEnseignant
                        .filter(
                            (devoir) =>
                                !classeId || String(devoir.classeId) === String(classeId),
                        )
                        .map((devoir) => (
                            <option key={devoir.id} value={devoir.id}>
                                {devoir.titre}
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
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((note) => {
                        const devoir = devoirs.find(
                            (element) => element.id === note.devoirId,
                        );
                        const eleve = eleves.find(
                            (element) => element.id === note.eleveId,
                        );
                        const matiere = matieres.find(
                            (element) => element.id === devoir?.matiereId,
                        );
                        return (
                            <tr key={note.id}>
                                <td>
                                    {eleve
                                        ? eleve.prenom + " " + eleve.nom
                                        : "Élève #" + note.eleveId}
                                </td>
                                <td>{devoir?.titre || note.devoirId}</td>
                                <td>{matiere?.nom || "—"}</td>
                                <td>
                                    {modification?.id === note.id ? (
                                        <input
                                            className="field" type="number" min="0" max="20" value={modification.valeur}
                                            onChange={(event) =>
                                                setModification({...modification, valeur: event.target.value,
                                                })}
                                        />
                                    ) : (
                                        <span>{note.valeur}/{note.valeurMaximale}</span>
                                    )}
                                </td>
                                <td>
                                    {modification?.id === note.id ? (
                                        <input
                                            className="field"
                                            value={modification.commentaire}
                                            onChange={(event) =>
                                                setModification({
                                                    ...modification, commentaire: event.target.value,
                                                })
                                            }
                                        />
                                    ) : (
                                        note.commentaire || "—"
                                    )}
                                </td>
                                <td>{note.date}</td>
                                <td className="actions">
                                    {modification?.id === note.id ? (
                                        <>
                                            <button
                                                className="button small"
                                                onClick={() => enregistrer(note)}
                                            >Enregistrer</button>
                                            <button
                                                className="button secondary small"
                                                onClick={() => setModification(null)}
                                            >Annuler</button>
                                        </>
                                    ) : (
                                        <button
                                            className="button secondary small"
                                            onClick={() =>
                                                setModification({
                                                    id: note.id,
                                                    valeur: note.valeur,
                                                    commentaire: note.commentaire || "",
                                                })}
                                        >Éditer</button>
                                    )}
                                </td>
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
