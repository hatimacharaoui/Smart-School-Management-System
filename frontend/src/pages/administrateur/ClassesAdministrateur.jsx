import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";

import usePagination, {PageVide,} from "../../hooks/usePagination";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

export default function ClassesAdministrateur() {
    const [pageClasses, setPageClasses] = useState(PageVide());
    const [eleves, setEleves] = useState([]);
    const [recherche, setRecherche] = useState("");
    const [erreur, setErreur] = useState("");
    const [message, setMessage] = useState("");
    const pagination = usePagination(pageClasses, 10, recherche);

    useEffect(() => {
        charger();
    }, [recherche, pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                classesApi.getAll({
                    recherche,
                    ...pagination.parametres,
                }),
                elevesApi.getAll({ size: 1000 }),
            ]);
            setPageClasses(reponses[0].data);
            setEleves(reponses[1].data.content);
        } catch (exception) {
            setErreur("Impossible de charger les classes ");
        }
    }

    async function supprimer(classe) {
        if (
            !window.confirm("Voulez-vous vraiment supprimer « " + classe.nom + " » ?")
        ) {
            return;
        }
        setErreur("");
        setMessage("");
        try {
            await classesApi.delete(classe.id);
            setMessage("Suppression effectuée avec succès.");
            await charger();
        } catch (exception) {
            setErreur(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Classes</h1>
                <Link className="button" to="/classes/add">
                    Ajouter une classe
                </Link>
            </header>
            {message && <p className="notice success">{message}</p>}
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <input
                    className="field search" value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher dans les classes"
                />
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Identifiant</th>
                        <th>Classe</th>
                        <th>Niveau</th>
                        <th>Nombre d’élèves</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((classe) => (
                        <tr key={classe.id}>
                            <td>{classe.id}</td>
                            <td>{classe.nom}</td>
                            <td>{classe.niveau}</td>
                            <td>{nombreEleves(eleves, classe.id)}</td>
                            <td className="actions">
                                <Link
                                    className="button secondary small"
                                    to={"/classes/" + classe.id + "/edit"}>Modifier</Link>
                                <button
                                    className="button danger small"
                                    onClick={() => supprimer(classe)}>Supprimer</button>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucune classe disponible.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function nombreEleves(eleves, classeId) {
    return eleves.filter((eleve) => eleve.classeId === classeId).length;
}
