import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import {matieresApi} from "../../services/api/matieresApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {presencesApi} from "../../services/api/presencesApi.js";
import {emploiDuTempsApi} from "../../services/api/emploiDuTempsApi.js";

export default function PresencesParent() {
    const { user } = useAuth();
    const [classes, setClasses] = useState([]);
    const [matieres, setMatieres] = useState([]);
    const [enfant, setEnfant] = useState(null);
    const [presences, setPresences] = useState([]);
    const [horaires, setHoraires] = useState([]);
    const [date, setDate] = useState(dateAujourdhui());
    const [message, setMessage] = useState("");

    useEffect(() => {
        charger();
    }, [user.id]);

    async function charger() {
        setMessage("");
        try {
            const references = await Promise.all([
                classesApi.getAll({ size: 1000 }),
                matieresApi.getAll({ size: 1000 }),
            ]);
            setClasses(references[0].data.content);
            setMatieres(references[1].data.content);

            let enfantId = localStorage.getItem("enfantSelectionneId");
            if (!enfantId) {
                const enfants = await elevesApi.getByParent(user.id, { size: 1000 });
                enfantId = enfants.data.content[0]?.id;
                if (enfantId) localStorage.setItem("enfantSelectionneId", enfantId);
            }
            if (!enfantId) throw new Error("Aucun enfant lié au parent");

            const reponseEnfant = await elevesApi.getById(enfantId);
            setEnfant(reponseEnfant.data);
            const reponses = await Promise.all([
                presencesApi.getByEleve(enfantId, { size: 1000 }),
                emploiDuTempsApi.getByClasse(reponseEnfant.data.classeId, {
                    size: 1000,
                }),
            ]);
            setPresences(reponses[0].data.content);
            setHoraires(reponses[1].data.content);
        } catch (exception) {
            setMessage("Impossible de charger les présences ");
        }
    }

    const jour = nomJour(date);
    const coursDuJour = horaires
        .filter((horaire) => horaire.jour === jour)
        .sort((a, b) => String(a.heureDebut).localeCompare(String(b.heureDebut)));
    const presencesDuJour = presences.filter(
        (presence) => presence.date === date,
    );
    const lignes = coursDuJour.map((horaire) => ({
        id: "cours-" + horaire.id,
        heure: String(horaire.heureDebut).slice(0, 5),
        matiereId: horaire.matiereId,
        presence: presencesDuJour.find(
            (presence) => presence.matiereId === horaire.matiereId,
        ),
    }));

    presencesDuJour.forEach((presence) => {
        if (!lignes.some((ligne) => ligne.matiereId === presence.matiereId)) {
            lignes.push({
                id: "presence-" + presence.id,
                heure: "—",
                matiereId: presence.matiereId,
                presence,
            });
        }
    });

    return (
        <div>
            <header className="page-header">
                <h1>Présences de mon enfant</h1>
            </header>
            {message && <p className="notice">{message}</p>}
            <div className="filters">
                <input
                    className="field search" type="date" value={date}
                    onChange={(event) => setDate(event.target.value)}
                />
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Élève</th>
                        <th>Classe</th>
                        <th>Date</th>
                        <th>Heure</th>
                        <th>Matière</th>
                        <th>Statut</th>
                    </tr>
                    </thead>
                    <tbody>
                    {lignes.map((ligne) => (
                        <tr key={ligne.id}>
                            <td>{enfant ? enfant.prenom + " " + enfant.nom : "—"}</td>
                            <td>{nomClasse(classes, enfant?.classeId)}</td>
                            <td>{date}</td>
                            <td>{ligne.heure}</td>
                            <td>{nomMatiere(matieres, ligne.matiereId)}</td>
                            <td>
                                <BadgeStatut statut={ligne.presence?.statut} />
                            </td>
                        </tr>
                    ))}
                    {lignes.length === 0 && (
                        <tr>
                            <td>Aucun cours pour cette date</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

function BadgeStatut({ statut }) {
    if (statut === "PRESENT") return <span className="badge">Présent</span>;
    if (statut === "ABSENT") return <span className="badge red">Absent</span>;
    if (statut === "EN_RETARD") {
        return <span className="badge amber">En retard</span>;
    }
    return <span className="badge gray">Non enregistré</span>;
}

function nomClasse(classes, id) {
    return classes.find((classe) => classe.id === id)?.nom || id || "—";
}

function nomMatiere(matieres, id) {
    return matieres.find((matiere) => matiere.id === id)?.nom || id || "—";
}

function nomJour(date) {
    const jours = [
        "Dimanche",
        "Lundi",
        "Mardi",
        "Mercredi",
        "Jeudi",
        "Vendredi",
        "Samedi",
    ];
    return jours[new Date(date + "T00:00:00").getDay()];
}

function dateAujourdhui() {
    const date = new Date();
    const annee = date.getFullYear();
    const mois = String(date.getMonth() + 1).padStart(2, "0");
    const jour = String(date.getDate()).padStart(2, "0");
    return annee + "-" + mois + "-" + jour;
}
