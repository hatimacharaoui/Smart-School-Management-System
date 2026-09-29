import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import {elevesApi} from "../../services/api/elevesApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";
import {paiementsApi} from "../../services/api/paiementsApi.js";


export default function PaiementsParent() {
    const { user } = useAuth();
    const [annee, setAnnee] = useState(String(new Date().getFullYear()));
    const [enfants, setEnfants] = useState([]);
    const [eleveId, setEleveId] = useState("");
    const [paiements, setPaiements] = useState([]);
    const [message, setMessage] = useState("");
    const moisAnnee = creerMois(annee);

    useEffect(() => {
        chargerEnfants();
    }, [user.id]);

    useEffect(() => {
        if (eleveId) chargerPaiements();
    }, [eleveId, annee]);

    async function chargerEnfants() {
        setMessage("");
        try {
            const reponse = await elevesApi.getByParent(user.id, { size: 1000 });
            const liste = reponse.data.content;
            setEnfants(liste);
            if (liste.length === 0) {
                setMessage("Aucun enfant n’est associé à ce compte.");
                return;
            }

            const selectionSauvegardee = Number(
                localStorage.getItem("enfantSelectionneId"),
            );
            const enfantSauvegarde = liste.find(
                (enfant) => enfant.id === selectionSauvegardee,
            );
            const selection = enfantSauvegarde?.id || liste[0].id;
            setEleveId(String(selection));
            localStorage.setItem("enfantSelectionneId", String(selection));
        } catch (exception) {
            setMessage(obtenirMessageErreur(exception));
        }
    }

    async function chargerPaiements() {
        setMessage("");
        try {
            const reponse = await paiementsApi.getByEleve(eleveId, {
                dateDebut: annee + "-01-01",
                dateFin: annee + "-12-31",
                page: 0,
                size: 1000,
                sort: "date,asc",
            });
            setPaiements(reponse.data.content);
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
            setMessage("Impossible d’ouvrir le justificatif ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Paiements mensuels</h1>
            </header>
            {message && <p className="notice">{message}</p>}
            <div className="filters">
                <select
                    className="field search"
                    value={eleveId}
                    onChange={(event) => {
                        setEleveId(event.target.value);
                        localStorage.setItem("enfantSelectionneId", event.target.value);
                    }}
                >
                    {enfants.map((enfant) => (
                        <option value={enfant.id} key={enfant.id}>
                            {enfant.prenom} {enfant.nom}
                        </option>
                    ))}
                </select>
                <input
                    className="field search" type="number" min="2020" max="2100" value={annee}
                    onChange={(event) => setAnnee(event.target.value)}
                    aria-label="Année"
                />
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Mois</th>
                        <th>Montant</th>
                        <th>Méthode</th>
                        <th>Statut</th>
                        <th>Justificatif</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {moisAnnee.map((moisCourant) => {
                        const paiement = paiements.find((element) =>
                            String(element.date).startsWith(moisCourant.valeur),
                        );
                        return (
                            <tr key={moisCourant.valeur}>
                                <td>{moisCourant.libelle}</td>
                                <td>{paiement ? paiement.montant + " DH" : "—"}</td>
                                <td>{paiement?.methode || "—"}</td>
                                <td>
                                    {paiement ? libelleStatut(paiement.statut) : "À payer"}
                                </td>
                                <td>
                                    {paiement?.urlJustificatif ? "Justificatif ajouté" : "Aucun justificatif"}
                                </td>
                                <td className="actions">
                                    {!paiement && eleveId && (
                                        <Link
                                            className="button small"
                                            to={
                                                "/paiements/ajouter?eleveId=" + eleveId + "&mois=" + moisCourant.valeur
                                            }
                                        >Ajouter un paiement</Link>
                                    )}
                                    {paiement?.urlJustificatif && (
                                        <button
                                            className="button secondary small"
                                            onClick={() => voirJustificatif(paiement)}
                                        >Voir le justificatif</button>
                                    )}
                                    {paiement && !paiement.urlJustificatif && (
                                        <Link
                                            className="button small"
                                            to={"/paiements/" + paiement.id + "/justificatif"}
                                        >Ajouter le justificatif</Link>
                                    )}
                                </td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

function libelleStatut(statut) {
    if (statut === "VALIDE") return "Validé";
    if (statut === "REFUSE") return "Refusé";
    return "En attente";
}

function creerMois(annee) {
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
    return noms.map((libelle, index) => ({
        libelle,
        valeur: annee + "-" + String(index + 1).padStart(2, "0"),
    }));
}
