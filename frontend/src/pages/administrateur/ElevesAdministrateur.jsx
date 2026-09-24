import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";
import usePagination, { PageVide } from "../../hooks/usePagination.js";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";

export default function ElevesAdministrateur() {
    const [pageEleves, setPageEleves] = useState(PageVide());
    const [recherche, setRecherche] = useState("");
    const [classes, setClasses] = useState([]);
    const [classeId, setClasseId] = useState("");
    const [erreur, setErreur] = useState("");
    const [message, setMessage] = useState("");
    const pagination = usePagination(pageEleves, 10, recherche + "-" + classeId);

    useEffect(() => {
        chargerClasses();
    }, []);

    useEffect(() => {
        chargerEleves();
    }, [recherche, classeId, pagination.numeroPage, pagination.taillePage]);

    async function chargerClasses() {
        try {
            const reponse = await classesApi.getAll({ size: 1000 });

            setClasses(reponse.data.content);
        } catch (exception) {
            setErreur("Impossible de charger les classes");
        }
    }

    async function chargerEleves() {
        setErreur("");
        try {
            let reponse;

            if (classeId) {
                reponse = await elevesApi.getByClasse(classeId, {
                    recherche, ...pagination.parametres });
            } else {
                reponse = await elevesApi.getAll({ recherche, ...pagination.parametres });
            }

            setPageEleves(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les élèves");
        }
    }

    async function supprimer(eleve) {
        const nom = eleve.prenom + " " + eleve.nom;
        if (
            !window.confirm("Voulez-vous vraiment supprimer l'élève « " + nom + " » ?")
        ) {
            return;
        }
        setErreur("");
        setMessage("");
        try {
            await elevesApi.delete(eleve.id);
            setMessage("L'élève a été supprimé avec succès ");
            await chargerEleves();

        } catch (exception) {
            setErreur(exception.messageUtilisateur || "Impossible de charger les données");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Elèves</h1>
                <Link className="button" to="/eleves/add"> Ajouter un élève </Link>
            </header>
            {message && <p className="notice">{message}</p>}
            {erreur && <p className="notice">{erreur}</p>}

            <div className="filters">
                <select className="field search" value={classeId}
                    onChange={(event) => setClasseId(event.target.value)}
                >
                    <option value="">Toutes les classes</option>
                    {classes.map((classe) => (
                        <option value={classe.id} key={classe.id}>
                            {classe.nom}
                        </option>
                    ))}
                </select>
                <input
                    className="field search" value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher un élève "
                />
            </div>

            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>N</th>
                        <th>Nom</th>
                        <th>Email</th>
                        <th>Classe</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((eleve) => (
                        <tr key={eleve.id}>
                            <td>{eleve.matricule}</td>
                            <td>{eleve.prenom + " " + eleve.nom}</td>
                            <td>{eleve.email}</td>
                            <td>{classes.find((classe) =>
                                        String(classe.id) === String(eleve.classeId))?.nom || eleve.classeId}
                            </td>

                            <td className="actions">
                                <Link className="button secondary small" to={"/eleves/" + eleve.id} >Voir</Link>

                                <Link className="button secondary small" to={"/eleves/" + eleve.id + "/edit"}>Modifier</Link>

                                <button className="button danger small" onClick={() => supprimer(eleve)}>Supprimer</button>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td colSpan="5" className="empty">Aucun élève trouvé </td>
                        </tr>
                    )}
                    </tbody>
                </table>

                <Pagination pagination={pagination} />
            </div>

        </div>
    );
}

