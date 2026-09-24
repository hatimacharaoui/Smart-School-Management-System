import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import { useAuth } from "../../context/AuthContext";
import usePagination, {
    PageVide,
} from "../../hooks/usePagination.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";

export default function ElevesEnseignant() {
    const { user } = useAuth();
    const [pageEleves, setPageEleves] = useState(PageVide());
    const [recherche, setRecherche] = useState("");
    const [classes, setClasses] = useState([]);
    const [classeId, setClasseId] = useState("");
    const [erreur, setErreur] = useState("");
    const pagination = usePagination( pageEleves, 10, recherche + "-" + classeId );

    useEffect(() => {

        chargerClasses();
    }, [user.id]);

    useEffect(() => {

        if (classeId) chargerEleves();
    }, [recherche, classeId, pagination.numeroPage, pagination.taillePage]);

    async function chargerClasses() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                enseignantsApi.getClasses(user.id, { size: 1000 }),
                classesApi.getAll({ size: 1000 }),
            ]);
            const ids = reponses[0].data.content.map((lien) => lien.classeId);
            const classesEnseignant = reponses[1].data.content.filter((classe) =>
                ids.includes(classe.id),
            );
            setClasses(classesEnseignant);
            setClasseId(classesEnseignant[0]?.id || "");

        } catch (exception) {
            setErreur("Impossible de charger les classes");
        }
    }

    async function chargerEleves() {
        setErreur("");
        try {
            const reponse = await elevesApi.getByClasse(classeId, {
                recherche, ...pagination.parametres,
            });
            setPageEleves(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les élèves.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Elèves</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <select className="field search" value={classeId}
                    onChange={(event) => setClasseId(event.target.value)}
                >
                    {classes.map((classe) => (
                        <option value={classe.id} key={classe.id}>
                            {classe.nom}
                        </option>
                    ))}
                </select>

                <input className="field search" value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher un élève..." />
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
                            <td>
                                {classes.find((classe) => String(classe.id) === String(eleve.classeId))?.nom || eleve.classeId}
                            </td>
                            <td className="actions">
                                <Link className="button secondary small" to={"/eleves/" + eleve.id} >
                                    Voir
                                </Link>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td colSpan="5" className="empty">Aucun élève trouvé</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}
