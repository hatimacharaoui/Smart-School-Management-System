import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";

import usePagination, {PageVide,} from "../../hooks/usePagination";
import {parentsApi} from "../../services/api/parentsApi.js";
import {notesApi as elevesAPI} from "../../services/api/notesApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

export default function ParentsAdministrateur() {
    const [pageParents, setPageParents] = useState(PageVide());
    const [eleves, setEleves] = useState([]);
    const [recherche, setRecherche] = useState("");
    const [erreur, setErreur] = useState("");
    const [message, setMessage] = useState("");
    const pagination = usePagination(pageParents, 10, recherche);

    useEffect(() => {
        charger();
    }, [recherche, pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                parentsApi.getAll({
                    recherche,
                    ...pagination.parametres,
                }),
                elevesAPI.getAll({ size: 1000 }),
            ]);
            setPageParents(reponses[0].data);
            setEleves(reponses[1].data.content);
        } catch (exception) {
            setErreur("Impossible de charger les parents.");
        }
    }

    async function supprimer(parent) {
        const nom = parent.prenom + " " + parent.nom;
        if (!window.confirm("Voulez-vous vraiment supprimer « " + nom + " » ?")) {
            return;
        }
        setErreur("");
        setMessage("");
        try {
            await parentsApi.delete(parent.id);
            setMessage("Suppression effectuée avec succès.");
            await charger();
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Parents</h1>
                <Link className="button" to="/parents/add">
                    Ajouter un parent
                </Link>
            </header>
            {message && <p className="notice success">{message}</p>}
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <input
                    className="field search"
                    value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher dans les parents..."
                />
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Identifiant</th>
                        <th>Parent</th>
                        <th>Email</th>
                        <th>Téléphone</th>
                        <th>Enfants</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((parent) => (
                        <tr key={parent.id}>
                            <td>{parent.id}</td>
                            <td>{parent.prenom + " " + parent.nom}</td>
                            <td>{parent.email}</td>
                            <td>{parent.telephone || "—"}</td>
                            <td>{nomsEnfants(eleves, parent.id)}</td>
                            <td className="actions">
                                <Link
                                    className="button secondary small"
                                    to={"/parents/" + parent.id + "/edit"}
                                >
                                    Modifier
                                </Link>
                                <button
                                    className="button danger small"
                                    onClick={() => supprimer(parent)}
                                >
                                    Supprimer
                                </button>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td colSpan="6" className="empty">
                                Aucun parent disponible.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function nomsEnfants(eleves, parentId) {
    const noms = eleves
        .filter((eleve) => eleve.parentId === parentId)
        .map((eleve) => eleve.prenom + " " + eleve.nom);
    return noms.length > 0 ? noms.join(", ") : "Aucun enfant";
}
