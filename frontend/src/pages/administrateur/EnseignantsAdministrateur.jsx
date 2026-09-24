import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";
import usePagination, {PageVide} from "../../hooks/usePagination.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {notesApi as matieresAPI} from "../../services/api/notesApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {affectationsClassesApi} from "../../services/api/affectationsClassesApi.js";

export default function EnseignantsAdministrateur() {
    const [pageEnseignants, setPageEnseignants] = useState(PageVide());
    const [matieres, setMatieres] = useState([]);
    const [classes, setClasses] = useState([]);
    const [affectations, setAffectations] = useState([]);
    const [recherche, setRecherche] = useState("");
    const [erreur, setErreur] = useState("");
    const [message, setMessage] = useState("");
    const pagination = usePagination(pageEnseignants, 10, recherche);

    useEffect(() => {
        charger();
    }, [recherche, pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                enseignantsApi.getAll({
                    recherche, ...pagination.parametres,}),
                matieresAPI.getAll({ size: 1000 }),
                classesApi.getAll({ size: 1000 }),
                affectationsClassesApi.getAll({ size: 1000 }),
            ]);
            setPageEnseignants(reponses[0].data);
            setMatieres(reponses[1].data.content);
            setClasses(reponses[2].data.content);
            setAffectations(reponses[3].data.content);
        } catch (exception) {
            setErreur("Impossible de charger les enseignants ");
        }
    }

    async function supprimer(enseignant) {
        const nom = enseignant.prenom + " " + enseignant.nom;
        if (!window.confirm("Voulez-vous vraiment supprimer « " + nom + " » ?")) {
            return;
        }
        setErreur("");
        setMessage("");
        try {
            await enseignantsApi.delete(enseignant.id);
            setMessage("Suppression effectuée avec succès.");
            await charger();
        } catch (exception) {
            setErreur(exception.messageUtilisateur || "Impossible de charger les données");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Enseignants</h1>
                <Link className="button" to="/enseignants/add">Ajouter un enseignant</Link>
            </header>
            {message && <p className="notice success">{message}</p>}
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">

                <input className="field search" value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher dans les enseignants "
                />
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Identifiant</th>
                        <th>Enseignant</th>
                        <th>Email</th>
                        <th>Matière</th>
                        <th>Classes</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((enseignant) => (
                        <tr key={enseignant.id}>
                            <td>{enseignant.id}</td>
                            <td>{enseignant.prenom + " " + enseignant.nom}</td>
                            <td>{enseignant.email}</td>
                            <td>{nomMatiere(matieres, enseignant.matiereId)}</td>
                            <td>{nomsClasses(affectations, classes, enseignant.id)}</td>
                            <td className="actions">
                                <Link
                                    className="button secondary small" to={"/enseignants/" + enseignant.id + "/edit"}>
                                    Modifier
                                </Link>
                                <button className="button danger small" onClick={() => supprimer(enseignant)}>
                                    Supprimer
                                </button>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td className="empty">Aucun enseignant disponible</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function nomMatiere(matieres, id) {
    return matieres.find((matiere) => matiere.id === id)?.nom || "Non définie";
}

function nomsClasses(affectations, classes, enseignantId) {
    const ids = affectations
        .filter((affectation) => affectation.enseignantId === enseignantId)
        .map((affectation) => affectation.classeId);
    const noms = classes
        .filter((classe) => ids.includes(classe.id))
        .map((classe) => classe.nom);
    return noms.length > 0 ? noms.join(", ") : "Aucune classe";
}
