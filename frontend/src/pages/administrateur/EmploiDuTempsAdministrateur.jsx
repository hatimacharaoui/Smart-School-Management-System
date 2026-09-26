import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";

import usePagination, {PageVide,} from "../../hooks/usePagination";
import {emploiDuTempsApi} from "../../services/api/emploiDuTempsApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {classesApi} from "../../services/api/classesApi.js";

const jours = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"];

export default function EmploiDuTempsAdministrateur() {
    const [pageHoraires, setPageHoraires] = useState(PageVide());
    const [matieres, setMatieres] = useState([]);
    const [enseignants, setEnseignants] = useState([]);
    const [classes, setClasses] = useState([]);
    const [jourSelectionne, setJourSelectionne] = useState("");
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(pageHoraires, 10, jourSelectionne);

    useEffect(() => {
        charger();
    }, [jourSelectionne, pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                emploiDuTempsApi.getAll({
                    jour: jourSelectionne || undefined,
                    ...pagination.parametres,
                }),
                matieresApi.getAll({ size: 1000 }),
                enseignantsApi.getAll({ size: 1000 }),
                classesApi.getAll({ size: 1000 }),
            ]);
            setPageHoraires(reponses[0].data);
            setMatieres(reponses[1].data.content);
            setEnseignants(reponses[2].data.content);
            setClasses(reponses[3].data.content);
        } catch (exception) {
            setErreur("Impossible de charger l’emploi du temps.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Emploi du temps</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <select
                    className="field search" value={jourSelectionne}
                    onChange={(event) => setJourSelectionne(event.target.value)}
                >
                    <option value="">Tous les jours</option>
                    {jours.map((jour) => (
                        <option key={jour}>{jour}</option>
                    ))}
                </select>
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Jour</th>
                        <th>Heure</th>
                        <th>Matière</th>
                        <th>Classe</th>
                        <th>Enseignant</th>
                        <th>Salle</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((horaire) => (
                        <tr key={horaire.id}>
                            <td>{horaire.jour}</td>
                            <td>{String(horaire.heureDebut).slice(0, 5)}</td>
                            <td>{libelle(matieres, horaire.matiereId)}</td>
                            <td>{libelle(classes, horaire.classeId)}</td>
                            <td>{nomEnseignant(enseignants, horaire.enseignantId)}</td>
                            <td>{horaire.salle}</td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucun horaire trouvé</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function libelle(liste, id) {
    const element = liste.find((valeur) => valeur.id === id);
    return element ? element.nom : id;
}

function nomEnseignant(enseignants, id) {
    const enseignant = enseignants.find((element) => element.id === id);
    return enseignant ? enseignant.prenom + " " + enseignant.nom : id;
}
