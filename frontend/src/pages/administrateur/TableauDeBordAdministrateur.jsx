import React from 'react';
import {useEffect,useState} from "react";
import {Carte} from "../../components/Elements.jsx";
import {classesApi} from "../../services/api/classesApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";
import {enseignantsApi} from "../../services/api/enseignantsApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";
import {presencesApi} from "../../services/api/presencesApi.js";
import {paiementsApi} from "../../services/api/paiementsApi.js";
import {useAuth} from "../../context/AuthContext.jsx";
import {notificationsApi} from "../../services/api/notificationsApi.js";


export default function TableauDeBordAdministrateur() {

    const {user} = useAuth();
    const [eleves, setEleves] = useState([]);
    const [classes, setClasses] = useState([]);
    const [presences, setPresences] = useState([]);

    const [total, setTotal] = useState({});
    const [erreur, setErreur] = useState("");

    useEffect(() => {
        charger();
    }, []);

    async function charger() {
        try {
            const responses = await Promise.all([
                elevesApi.getAll({size: 1000}),
                enseignantsApi.getAll({size: 1}),
                classesApi.getAll({size: 1000}),
                presencesApi.getPresence({size: 10, sort: "date,desc"}),
                matieresApi.getAll({size: 1}),
                devoirsApi.getAll({statut: "EN-CORRECTION", size: 1}),
                paiementsApi.getAll({statut: "EN-ATTENTE", size: 1}),
                notificationsApi.getByUser(user.id, { lue: false, size: 1})
            ]);

            setEleves(responses[0].data.content);
            setClasses(responses[2].data.content);
            setPresences(responses[3].data.content);

            setTotal({
                eleves: responses[0].data.totalElements,
                enseignants: responses[1].data.totalElements,
                classes: responses[2].data.totalElements,
                presences: responses[3].data.totalElements,
                matieres: responses[4].data.totalElements,
                devoirs: responses[5].data.totalElements,
                paiements: responses[6].data.totalElements,
                notifications: responses[7].data.totalElements,
            });

        } catch (exception) {
            setErreur(
            exception.messageUtilisateur || "Impossible de charger les données.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <div>
                    <h1>Tableau de bord</h1>
                </div>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="stats">
                <Carte label="Elèves" value={total.eleves || 0} />
                <Carte label="Enseignants" value={total.enseignants || 0} />
                <Carte label="Classes" value={total.className || 0} />
                <Carte label="Matières" value={total.matieres || 0} />
                <Carte label="Absences et retards" value={total.presences || 0} />
                <Carte label="Devoirs en correction" value={total.devoirs || 0} />
                <Carte label="Paiements en attente" value={total.paiements || 0} />
                <Carte label="Notifications non lues" value={total.notifications || 0} />
            </div>
            <section className="card dashboard-section">
                <div className="card-body">
                    <h2>Elèves absents et en retard</h2>
                </div>
                <div className="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Elève</th>
                            <th>Classe</th>
                            <th>Date</th>
                            <th>Statut</th>
                        </tr>
                        </thead>
                        <tbody>
                        {presences.map((presence) => {
                            const eleve = eleves.find((etudiant) => etudiant.id === presence.eleveId);

                            return (
                                <tr key={presence.id}>
                                    <td>
                                        {eleve ? eleve.prenom + " " + eleve.nom : `Elève #${presence.eleveId}`}
                                    </td>
                                    <td>{classes.find((classe) => classe.id === presence.classeId)?.nom || "—"}</td>
                                    <td>{presence.date}</td>
                                    <td>{presence.statut}</td>
                                </tr>
                            );
                        })
                        }
                        <tr>
                            <td>
                        {presences.length === 0 && "Aucune absence, aucun retard"}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    );
}

