import { useEffect, useState } from "react";
import Pagination from "../../components/Pagination";

import usePagination, {PageVide,} from "../../hooks/usePagination";
import {elevesApi} from "../../services/api/elevesApi.js";
import {parentsApi} from "../../services/api/parentsApi.js";
import {paiementsApi} from "../../services/api/paiementsApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

export default function PaiementsAdministrateur() {
    const [pagePaiements, setPagePaiements] = useState(PageVide());
    const [eleves, setEleves] = useState([]);
    const [parents, setParents] = useState([]);
    const [mois, setMois] = useState(moisActuel());
    const [statut, setStatut] = useState("");
    const [modifications, setModifications] = useState({});
    const [message, setMessage] = useState("");
    const pagination = usePagination(
        pagePaiements,
        10,
        mois + "-" + statut,
    );

    useEffect(() => {
        chargerReferences();
    }, []);

    useEffect(() => {
        chargerPaiements();
    }, [mois, statut, pagination.numeroPage, pagination.taillePage]);

    async function chargerReferences() {
        try {
            const reponses = await Promise.all([
                elevesApi.getAll({ size: 1000, sort: "id,asc" }),
                parentsApi.getAll({ size: 1000, sort: "id,asc" }),
            ]);
            setEleves(reponses[0].data.content);
            setParents(reponses[1].data.content);
        } catch (exception) {
            setMessage("Impossible de charger les élèves et les parents ");
        }
    }

    async function chargerPaiements() {
        setMessage("");
        try {
            const [annee, numeroMois] = mois.split("-").map(Number);
            const reponse = await paiementsApi.getMonthly({
                annee, mois: numeroMois, statut: statut || undefined, sort: "id,asc", ...pagination.parametres,
            });
            setPagePaiements(reponse.data);
            setModifications({});
        } catch (exception) {
            setMessage(obtenirMessageErreur(exception));
        }
    }

    function changerChamp(paiement, champ, valeur) {
        const cle = clePaiement(paiement);
        setModifications((anciennesValeurs) => ({
            ...anciennesValeurs,
            [cle]: {
                montant:
                    anciennesValeurs[cle]?.montant ?? String(paiement.montant ?? 1500),
                methode:
                    anciennesValeurs[cle]?.methode ?? paiement.methode ?? "Espèces",
                [champ]: valeur,
            },
        }));
    }

    function valeurChamp(paiement, champ) {
        const valeurs = modifications[clePaiement(paiement)];
        if (valeurs && valeurs[champ] !== undefined) return valeurs[champ];
        return paiement[champ];
    }

    async function enregistrer(paiement) {
        setMessage("");
        const montant = Number(valeurChamp(paiement, "montant"));
        const methode = valeurChamp(paiement, "methode");
        if (!montant || montant <= 0) {
            setMessage("Le montant doit être positif ");
            return;
        }

        const donnees = {
            ...paiement, montant, methode, statut: paiement.statut || "EN_ATTENTE", date: paiement.date || mois + "-01"};

        try {
            let confirmation;
            if (paiement.id) {
                await paiementsApi.update(paiement.id, donnees);
                confirmation = "Le paiement a été modifié.";
            } else {
                await paiementsApi.create(donnees);
                confirmation = "Le paiement en attente a été enregistré.";
            }
            await chargerPaiements();
            setMessage(confirmation);
        } catch (exception) {
            setMessage(obtenirMessageErreur(exception));
        }
    }

    async function modifierStatut(paiement, nouveauStatut) {
        try {
            await paiementsApi.updateStatus(paiement.id, nouveauStatut);
            await chargerPaiements();
            setMessage("Le statut a été modifié ");
        } catch (exception) {
            setMessage(obtenirMessageErreur(exception));
        }
    }

    async function voirJustificatif(paiement) {
        if (!paiement.urlJustificatif) {
            setMessage("Aucun justificatif disponible.");
            return;
        }

        if (paiement.urlJustificatif.startsWith("/")) {
            window.open(paiement.urlJustificatif, "_blank");
            return;
        }

        const fenetre = window.open("", "_blank");
        try {
            const reponse = await paiementsApi.viewProof(paiement.id);
            const url = URL.createObjectURL(reponse.data);
            if (fenetre) fenetre.location.href = url;
            window.setTimeout(() => URL.revokeObjectURL(url), 60000);
        } catch (exception) {
            if (fenetre) fenetre.close();
            setMessage("Impossible d’ouvrir le justificatif.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Paiements mensuels</h1>
            </header>
            {message && <p className="notice">{message}</p>}
            <div className="filters">
                <input
                    className="field search" type="month" value={mois}
                    onChange={(event) => setMois(event.target.value)}
                    aria-label="Mois des paiements"
                />
                <select className="field search" value={statut}
                    onChange={(event) => setStatut(event.target.value)}
                    aria-label="Filtrer par statut"
                >
                    <option value="">Tous les statuts</option>
                    <option value="VALIDE">Validé</option>
                    <option value="EN_ATTENTE">En attente</option>
                    <option value="REFUSE">Refusé</option>
                </select>
                <button className="button secondary" onClick={chargerPaiements}>
                    Actualiser</button>
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Élève</th>
                        <th>Parent</th>
                        <th>Mois</th>
                        <th>Montant</th>
                        <th>Méthode</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((paiement) => (
                        <tr key={clePaiement(paiement)}>
                            <td>{nomEleve(eleves, paiement.eleveId)}</td>
                            <td>{nomParent(parents, paiement.parentId)}</td>
                            <td>{libelleMois(mois)}</td>
                            <td>
                                <input
                                    className="field payment-edit-field"
                                    type="number" min="0.01" step="0.01"
                                    value={valeurChamp(paiement, "montant")}
                                    onChange={(event) =>
                                        changerChamp(paiement, "montant", event.target.value)
                                    }
                                    aria-label="Montant"
                                />
                            </td>
                            <td>
                                <select
                                    className="field payment-method-field"
                                    value={valeurChamp(paiement, "methode")}
                                    onChange={(event) =>
                                        changerChamp(paiement, "methode", event.target.value)
                                    }
                                    aria-label="Méthode"
                                >
                                    <option value="Espèces">Espèces</option>
                                    <option value="Virement bancaire">Virement bancaire</option>
                                </select>
                            </td>
                            <td>{libelleStatut(paiement.statut)}</td>
                            <td className="actions">
                                <button
                                    className="button secondary small"
                                    onClick={() => enregistrer(paiement)}
                                >
                                    {paiement.id ? "Modifier" : "Enregistrer"}
                                </button>
                                {paiement.urlJustificatif && (
                                    <button
                                        className="button secondary small"
                                        onClick={() => voirJustificatif(paiement)}
                                    >
                                        Voir le justificatif</button>
                                )}
                                {paiement.id && paiement.statut !== "VALIDE" && (
                                    <button
                                        className="button small"
                                        onClick={() => modifierStatut(paiement, "VALIDE")}
                                    >
                                        Valider</button>
                                )}
                                {paiement.id && paiement.statut !== "REFUSE" && (
                                    <button
                                        className="button danger small"
                                        onClick={() => modifierStatut(paiement, "REFUSE")}
                                    >
                                        Refuser</button>
                                )}
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucun élève trouvé.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function clePaiement(paiement) {
    if (paiement.id) return "paiement-" + paiement.id;
    return "eleve-" + paiement.eleveId + "-" + paiement.date;
}

function moisActuel() {
    const date = new Date();
    return (
        date.getFullYear() + "-" + String(date.getMonth() + 1).padStart(2, "0")
    );
}

function libelleMois(mois) {
    const noms = [
        "Janvier",
        "Février",
        "Mars",
        "Avril",
        "Mai",
        "Juin",
        "Juillet",
        "Août",
        "Septembre",
        "Octobre",
        "Novembre",
        "Décembre",
    ];
    const [annee, numero] = mois.split("-");
    return noms[Number(numero) - 1] + " " + annee;
}

function nomEleve(eleves, id) {
    const eleve = eleves.find((element) => element.id === id);
    if (eleve) return eleve.prenom + " " + eleve.nom;
    return "Élève #" + id;
}

function nomParent(parents, id) {
    const parent = parents.find((element) => element.id === id);
    if (parent) return parent.prenom + " " + parent.nom;
    return "Parent #" + id;
}

function libelleStatut(statut) {
    if (statut === "VALIDE") return "Validé";
    if (statut === "REFUSE") return "Refusé";
    return "En attente";
}
