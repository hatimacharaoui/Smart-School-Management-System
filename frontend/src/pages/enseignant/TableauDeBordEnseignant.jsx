import React, {useEffect, useState} from 'react';
import {useAuth} from "../../context/AuthContext.jsx";
import {Carte, nomJourAujourdhui} from "../../components/Elements.jsx";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {emploiDuTempsApi} from "../../services/api/emploiDuTempsApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";
import {notificationsApi} from "../../services/api/notificationsApi.js";


export default function TableauDeBordEnseignant() {
    const {user} =useAuth();

    const [devoirs, setDevoirs] = useState([]);
    const [classeLie, setClasseLie] = useState([]);
    const [horaires, setHoraires] = useState([]);
    const [classes, setClasses] = useState([]);
    const [matieres, setMatieres] = useState([]);

    const [total, setTotal] = useState({});
    const [erreur, setErreur] = useState("");

    useEffect(() => {
        charger();
    }, []);

    async function charger(){
        try {
            const enseignantId = user.id;
            const reponses = await Promise.all([
                devoirsApi.getByEnseignant(enseignantId, {statut: "EN_CORRECTION", size: 10}),
                enseignantsApi.getClasses(enseignantId, {size: 100}),
                emploiDuTempsApi.getAll({jour: nomJourAujourdhui(), enseignantId, size: 10, sort: "heureDebut,asc"}),
                notificationsApi.getByUser(user.id, {lue: false, size: 1}),
                classesApi.getAll({ size: 100}),
                matieresApi.getAll( {size: 100})
            ]);

            setDevoirs(reponses[0].data.content);
            setClasseLie(reponses[1].data.content);
            setHoraires(reponses[2].data.content);
            setClasses(reponses[4].data.content);
            setMatieres(reponses[5].data.content);

            setTotal( {
                devoirs: reponses[0].data.totalElements,
                classes: reponses[1].data.totalElements,
                cours: reponses[2].data.totalElements,
                notifications: reponses[3].data.totalElements
            });

        } catch (exception) {
            setErreur(exception.messageUtilisateur || "Impossible de charger les données.");
        }
    }

    const mesClasses = classeLie.map((lien) => classes.find((classe) =>classe.id === lien.classeId)).filter(Boolean);

    return (
        <div>
            {erreur && <p className="notice">{erreur}</p>}

            <div className="stats">
                <Carte label="Devoirs en correction" value={total.devoirs || 0 }/>
                <Carte label="Mes classes" value={total.classes || 0 }/>
                <Carte label="Notification non lues" value={total.notifications || 0 }/>
                <Carte label="Cours aujourd'hui" value={total.cours || 0 }/>
            </div>

            <section className="card dashboard-section">
                <div className="card-body">
                    <h2>Emploi du temps — aujourd’hui</h2>
                </div>
                <div className="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Heure</th>
                            <th>Matière</th>
                            <th>Classe</th>
                            <th>Salle</th>
                        </tr>
                        </thead>
                        <tbody>
                        {horaires.map((horaire) => (
                            <tr key={horaire.id}>
                                <td>{String(horaire.heureDebut).slice(0, 5)}</td>
                                <td>{matieres.find((matiere) => matiere.id === horaire.matiereId)?.nom || "—"}</td>
                                <td>{classes.find((classe) => classe.id === horaire.classeId)?.nom || "—"}</td>
                                <td>{horaire.salle}</td>
                            </tr>
                        ))}
                        <tr><td>
                        {horaires.length === 0 && "Aucune cours aujourd'hui"}
                        </td></tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section className="card dashboard-section">
                <div className="card-body">
                    <h2>Mes classes</h2>
                </div>
                <div className="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Classe</th>
                            <th>Niveau</th>
                        </tr>
                        </thead>
                        <tbody>
                        {mesClasses.map((classe) => (
                            <tr key={classe.id}>
                                <td>{classe.nom}</td>
                                <td>{classe.niveau}</td>
                            </tr>
                        ))}
                        <tr><td>
                            {mesClasses.length === 0 && "Aucune classe"}
                        </td></tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section className="card dashboard-section">
                <div className="card-body">
                    <h2>Devoirs à corriger</h2>
                </div>
                <div className="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Devoir</th>
                            <th>Classe</th>
                            <th>Date limite</th>
                            <th>Statut</th>
                        </tr>
                        </thead>
                        <tbody>
                        {devoirs.map((devoir) => (
                            <tr key={devoir.id}>
                                <td>{devoir.titre}</td>
                                <td>{classes.find((classe) => classe.id === devoir.classeId)?.nom || "—"}</td>
                                <td>{devoir.dateLimite}</td>
                                <td>En correction</td>
                            </tr>
                        ))}
                        <tr><td>
                            {devoirs.length === 0 && "Aucune devoir en correction"}
                        </td></tr>
                        </tbody>
                    </table>
                </div>
            </section>

        </div>
    );
}

