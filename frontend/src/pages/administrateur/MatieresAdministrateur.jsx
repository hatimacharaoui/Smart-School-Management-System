import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";

import usePagination, {PageVide,} from "../../hooks/usePagination";
import {matieresApi} from "../../services/api/matieresApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

export default function MatieresAdministrateur() {
    const [pageMatieres, setPageMatieres] = useState(PageVide());
    const [enseignants, setEnseignants] = useState([]);
    const [recherche, setRecherche] = useState("");
    const [erreur, setErreur] = useState("");
    const [message, setMessage] = useState("");
    const pagination = usePagination(pageMatieres, 10, recherche);

    useEffect(() => {
        charger();
    }, [recherche, pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({
                    recherche,
                    ...pagination.parametres,
                }),
                enseignantsApi.getAll({ size: 1000 }),
            ]);
            setPageMatieres(reponses[0].data);
            setEnseignants(reponses[1].data.content);
        } catch (exception) {
            setErreur("Impossible de charger les matières.");
        }
    }

    async function supprimer(matiere) {
        if (
            !window.confirm(
                "Voulez-vous vraiment supprimer « " + matiere.nom + " » ?",
            )
        ) {
            return;
        }
        setErreur("");
        setMessage("");
        try {
            await matieresApi.delete(matiere.id);
            setMessage("Suppression effectuée avec succès.");
            await charger();
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Matières</h1>
                <Link className="button" to="/matieres/add">
                    Ajouter une matière
                </Link>
            </header>
            {message && <p className="notice success">{message}</p>}
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <input className="field search" value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher dans les matières..."
                />
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Identifiant</th>
                        <th>Matière</th>
                        <th>Coefficient</th>
                        <th>Enseignants</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((matiere) => (
                        <tr key={matiere.id}>
                            <td>{matiere.id}</td>
                            <td>{matiere.nom}</td>
                            <td>{matiere.coefficient}</td>
                            <td>{nomsEnseignants(enseignants, matiere.id)}</td>
                            <td className="actions">
                                <Link
                                    className="button secondary small" to={"/matieres/" + matiere.id + "/edit"}
                                >Modifier
                                </Link>
                                <button
                                    className="button danger small"
                                    onClick={() => supprimer(matiere)}
                                >
                                    Supprimer</button>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucune matière disponible</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function nomsEnseignants(enseignants, matiereId) {
    const noms = enseignants
        .filter((enseignant) => enseignant.matiereId === matiereId)
        .map((enseignant) => enseignant.prenom + " " + enseignant.nom);
    return noms.length > 0 ? noms.join(", ") : "Aucun enseignant";
}
