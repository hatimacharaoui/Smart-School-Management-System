import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import {matieresApi} from "../../services/api/matieresApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {emploiDuTempsApi} from "../../services/api/emploiDuTempsApi.js";

const jours = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"];

export default function EmploiDuTempsEnseignant() {
    const { user } = useAuth();
    const [horaires, setHoraires] = useState([]);
    const [matieres, setMatieres] = useState([]);
    const [classes, setClasses] = useState([]);
    const [erreur, setErreur] = useState("");

    useEffect(() => {
        charger();
    }, [user.id]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                matieresApi.getAll({ size: 1000 }),
                classesApi.getAll({ size: 1000 }),
                emploiDuTempsApi.getByEnseignant(user.id, { size: 1000 }),
            ]);
            setMatieres(reponses[0].data.content);
            setClasses(reponses[1].data.content);
            setHoraires(reponses[2].data.content);
        } catch (exception) {
            setErreur("Impossible de charger l’emploi du temps ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Mon emploi du temps de la semaine</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="week-grid">
                {jours.map((jour) => {
                    const horairesJour = horaires.filter(
                        (horaire) => horaire.jour === jour,
                    );
                    return (
                        <section className="card day-column" key={jour}>
                            <h3>{jour}</h3>
                            {horairesJour.map((horaire) => (
                                <article className="schedule-item" key={horaire.id}>
                                    <strong>
                                        {String(horaire.heureDebut).slice(0, 5)} ·{" "}
                                        {libelle(matieres, horaire.matiereId)}
                                    </strong>
                                    <div>
                                        {libelle(classes, horaire.classeId)} · {horaire.salle}
                                    </div>
                                </article>
                            ))}
                            {horairesJour.length === 0 && (
                                <p className="empty">Aucun cours</p>
                            )}
                        </section>
                    );
                })}
            </div>
        </div>
    );
}

function libelle(liste, id) {
    return liste.find((element) => element.id === id)?.nom || id;
}
