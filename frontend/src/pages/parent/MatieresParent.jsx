import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";
import usePagination, {PageVide,} from "../../hooks/usePagination";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";

export default function MatieresParent() {
    const [pageMatieres, setPageMatieres] = useState(PageVide());
    const [enseignants, setEnseignants] = useState([]);
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(pageMatieres, 10, "matieres");

    useEffect(() => {
        charger();
    }, [pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                matieresApi.getAll(pagination.parametres),
                enseignantsApi.getAll({ size: 1000 }),
            ]);
            setPageMatieres(reponses[0].data);
            setEnseignants(reponses[1].data.content);
        } catch (exception) {
            setErreur("Impossible de charger les matières.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Matières</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Identifiant</th>
                        <th>Matière</th>
                        <th>Coefficient</th>
                        <th>Enseignants</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((matiere) => (
                        <tr key={matiere.id}>
                            <td>{matiere.id}</td>
                            <td>{matiere.nom}</td>
                            <td>{matiere.coefficient}</td>
                            <td>{nomsEnseignants(enseignants, matiere.id)}</td>
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
