import React, {useEffect, useState} from 'react';
import {useAuth} from "../../context/AuthContext.jsx";
import {elevesApi} from "../../services/api/elevesApi.js";
import {notificationsApi} from "../../services/api/notificationsApi.js";
import {matieresApi} from "../../services/api/matieresApi.js";
import {classesApi} from "../../services/api/classesApi.js";
import {notesApi} from "../../services/api/notesApi.js";
import {devoirsApi} from "../../services/api/devoirsApi.js";
import {paiementsApi} from "../../services/api/paiementsApi.js";
import {presencesApi} from "../../services/api/presencesApi.js";
import {Carte} from "../../components/Elements.jsx";


export default function TableauDeBordParent() {
    const  { user } = useAuth();

    const [enfants, setEnfants] = useState([]);
    const [enfantId, setEnfantId] = useState("");
    const [classes, setClasses] = useState([]);
    const [matieres, setMatieres] = useState([]);
    const [donnees, setDonnees] = useState({
        enfant: null,
        notes: [],
        devoirs: [],
        paiements: [],
        presences: [],
        notifications: 0
    });
    const [erreur, setErreur] = useState("");


    useEffect(() => {
        chargerApi();
    }, []);

    useEffect(() => {
        if(enfantId) {
            chargerEnfant(enfantId);
        }
    }, [enfantId]);

    async function chargerApi() {
        try {
            const reponses = await Promise.all([
                elevesApi.getByParent(user.id, {size: 10}),
                notificationsApi.getByUser(user.id, {lue: false, size: 1}),
                matieresApi.getAll({size: 100}),
                classesApi.getAll({size: 100}),
            ]);

            const listeEnfants = reponses[0].data.content;
            setEnfants(listeEnfants);
            setMatieres(reponses[2].data.content);
            setClasses(reponses[3].data.content);
            setDonnees((data) => ({...data, notifications: reponses[1].data.totalElements}))

            if(listeEnfants.length > 0) {
                const memorise = localStorage.getItem("enfantSelectionneId");
                const existe = listeEnfants.some((enfant) => String(enfant.id) === String(memorise) );

                setEnfantId(existe ? String(memorise) : String(listeEnfants[0].id) );
            }

        } catch (exception) {
            setErreur(exception.messageUtilisateur || "Impossible de charger les données");
        }
    }

    async function chargerEnfant(id) {
        try {
            const enfant = enfants.find((enf) => String(enf.id) === String(id));

            if(!enfant) return;

            localStorage.setItem("enfantSelectionneId", id);

            const reponses = await Promise.all([
                notesApi.getByEleve(id, { size: 100, sort: "date,desc"}),
                devoirsApi.getByClasse(enfant.classeId, {size: 100}),
                paiementsApi.getByEleve(id, {size: 5, sort: "date,desc"}),
                presencesApi.getByEleve(id, { size: 5, sort: "date,desc"})
            ]);

            setDonnees((data) => ({
                ...data, enfant,
                notes: reponses[0].data.content,
                devoirs: reponses[1].data.content,
                paiements: reponses[2].data.content,
                presences: reponses[3].data.content,

            }))
        } catch (exception) {
            setErreur(exception.messageUtilisateur || "Impossible de charger l’enfant");
        }}


    const devoirsAVenir = donnees.devoirs.filter(
        (devoir) => devoir.statut === "A_VENIR");
    const presence = donnees.presences.filter(
        (p) => p.statut !== "PRESENT");


    return (
        <div>
            <header className="page-header">
                <div>
                    <h1>Tableau de bord parent</h1>
                    <p className="muted">Suivi de l’enfant sélectionné</p>
                </div>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <select className="field search" value={enfantId}
                onChange={(event) => setEnfantId(event.target.value)}
                >
                    {enfants.map((enfant) => (
                        <option value={enfant.id} key={enfant.id}>
                            {enfant.prenom + " " + enfant.nom}
                        </option>
                    ))}
                </select>
            </div>

            <div className="card card-body child-file">
                <h2>Fiche de l’enfant</h2>
                <div className="child-file-grid">
                    <div>
                        <span className="muted">Nom complet</span>
                        <strong>{donnees.enfant ? donnees.enfant.prenom + " " + donnees.enfant.nom : "—"}</strong>
                    </div>
                    <div>
                        <span className="muted">Matricule</span>
                        <strong>{donnees.enfant?.matricule || "—"}</strong>
                    </div>
                    <div>
                        <span className="muted">Classe</span>
                        <strong>{classes.find((classe) => classe.id === donnees.enfant?.classeId)?.nom}</strong>
                    </div>
                </div>
            </div>


                <div className="stats">
                    <Carte label="Devoirs à venir" value={devoirsAVenir.length} />
                    <Carte label="Notes disponibles" value={donnees.notes.length} />
                    <Carte label="Notifications non lues" value={donnees.notifications} />
                </div>

                <section className="card dashboard-section">
                    <div className="card-body">
                        <h2>Absences et retards</h2>
                    </div>
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Matière</th>
                                <th>Statut</th>
                            </tr>
                            </thead>
                            <tbody>
                            {presence.map((p) => (
                                <tr key={p.id}>
                                    <td>{p.date}</td>
                                    <td>{matieres.find((matiere) => matiere.id === p.matiereId)?.nom}</td>
                                    <td>{p.statut}</td>
                                </tr>
                            ))}
                            {presence.length === 0 && (
                                <tr>
                                    <td>Aucune absence et aucun retard</td>
                                </tr>
                            )}
                            </tbody>
                        </table>
                    </div>
                </section>

            <section className="card dashboard-section">
                <div className="card-body">
                    <h2>Notes récentes</h2>
                </div>
                <div className="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Devoir</th>
                            <th>Matière</th>
                            <th>Note</th>
                            <th>Commentaire</th>
                            <th>Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        {donnees.notes.slice(0, 5).map((note) => {
                            const devoir = donnees.devoirs.find((element) => element.id === note.devoirId);

                            return (
                                <tr key={note.id}>
                                    <td>{devoir?.titre || `Devoir ${note.devoirId}`}</td>
                                    <td>{matieres.find((matiere) => matiere.id === devoir?.matiereId)?.nom || "—"}</td>
                                    <td>
                                        <span>
                                            {note.valeur}/{note.valeurMaximale}
                                        </span>
                                    </td>
                                    <td>{note.commentaire || "—"}</td>
                                    <td>{note.date}</td>
                                </tr>
                            );
                        })}
                        <tr><td>
                            {donnees.notes.length === 0 && "Aucune note disponible"}
                        </td></tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section className="card dashboard-section">
                <div className="card-body">
                    <h2>Paiements</h2>
                </div>
                <div className="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Montant</th>
                            <th>Méthode</th>
                            <th>Statut</th>
                        </tr>
                        </thead>
                        <tbody>
                        {donnees.paiements.map((paiement) => (
                            <tr key={paiement.id}>
                                <td>{paiement.date}</td>
                                <td>{paiement.montant} DH</td>
                                <td>{paiement.methode}</td>
                                <td>{paiement.statut} </td>
                            </tr>
                        ))}
                        {donnees.paiements.length === 0 && (
                            <tr>
                                <td>Aucun paiement disponible</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </section>

        </div>
    );
}
