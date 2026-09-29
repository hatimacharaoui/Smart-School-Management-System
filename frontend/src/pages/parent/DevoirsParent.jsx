import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";

import { useAuth } from "../../context/AuthContext";
import usePagination, {PageVide,} from "../../hooks/usePagination";
import {matieresApi} from "../../services/api/matieresApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";

export default function DevoirsParent() {
    const { user } = useAuth();
    const [pageDevoirs, setPageDevoirs] = useState(PageVide());
    const [classeId, setClasseId] = useState("");
    const [recherche, setRecherche] = useState("");
    const [statut, setStatut] = useState("");
    const [matieres, setMatieres] = useState([]);
    const [enseignants, setEnseignants] = useState([]);
    const [classes, setClasses] = useState([]);
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(
        pageDevoirs,
        10,
        recherche + "-" + statut + "-" + classeId,
    );

    useEffect(() => {
        preparer();
    }, [user.id]);

    useEffect(() => {
        if (classeId) chargerDevoirs();
    }, [
        classeId,
        recherche,
        statut,
        pagination.numeroPage,
        pagination.taillePage,
    ]);

    async function preparer() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({ size: 1000 }),
                enseignantsApi.getAll({ size: 1000 }),
                classesApi.getAll({ size: 1000 }),
            ]);
            setMatieres(reponses[0].data.content);
            setEnseignants(reponses[1].data.content);
            setClasses(reponses[2].data.content);
            let enfantId = localStorage.getItem("enfantSelectionneId");
            if (!enfantId) {
                const enfants = await elevesApi.getByParent(user.id, { size: 1000 });
                enfantId = enfants.data.content[0]?.id;
                if (enfantId) localStorage.setItem("enfantSelectionneId", enfantId);
            }
            if (!enfantId) throw new Error("Aucun enfant lié au parent");
            const eleve = await elevesApi.getById(enfantId);
            setClasseId(eleve.data.classeId);
        } catch (exception) {
            setErreur("Impossible de préparer les devoirs ");
        }
    }

    async function chargerDevoirs() {
        setErreur("");
        try {
            const reponse = await devoirsApi.getByClasse(classeId, {
                recherche, statut: statut || undefined, ...pagination.parametres,});
            setPageDevoirs(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les devoirs ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Devoirs de mon enfant</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <input
                    className="field search" value={recherche}
                    onChange={(event) => setRecherche(event.target.value)}
                    placeholder="Rechercher un devoir..."
                />
                <select
                    className="field search" value={statut}
                    onChange={(event) => setStatut(event.target.value)}
                >
                    <option value="">Tous les statuts</option>
                    <option value="A_VENIR">À venir</option>
                    <option value="EN_CORRECTION">En correction</option>
                    <option value="CORRIGE">Corrigé</option>
                </select>
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Devoir</th>
                        <th>Matière</th>
                        <th>Enseignant</th>
                        <th>Classe</th>
                        <th>Date</th>
                        <th>Statut</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((devoir) => (
                        <tr key={devoir.id}>
                            <td>{devoir.titre}</td>
                            <td>{nomMatiere(matieres, devoir.matiereId)}</td>
                            <td>{nomEnseignant(enseignants, devoir.enseignantId)}</td>
                            <td>{nomClasse(classes, devoir.classeId)}</td>
                            <td>{devoir.dateLimite}</td>
                            <td>
                                <span className="badge">{libelleStatut(devoir.statut)}</span>
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucun devoir trouvé</td>
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
    return (
        matieres.find((matiere) => String(matiere.id) === String(id))?.nom || id
    );
}

function nomEnseignant(enseignants, id) {
    const enseignant = enseignants.find(
        (element) => String(element.id) === String(id),
    );
    return enseignant ? enseignant.prenom + " " + enseignant.nom : id;
}

function nomClasse(classes, id) {
    return classes.find((classe) => String(classe.id) === String(id))?.nom || id;
}

function libelleStatut(statut) {
    if (statut === "A_VENIR") return "À venir";
    if (statut === "EN_CORRECTION") return "En correction";
    return "Corrigé";
}
