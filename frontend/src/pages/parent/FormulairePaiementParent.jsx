import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as yup from "yup";

import { useAuth } from "../../context/AuthContext";
import {elevesApi} from "../../services/api/elevesApi.js";
import {paiementsApi} from "../../services/api/paiementsApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

const typesAcceptes = ["application/pdf", "image/png", "image/jpeg"];

const schema = yup.object({
    montant: yup
        .number()
        .typeError("Le montant est obligatoire")
        .positive("Le montant doit être positif")
        .required("Le montant est obligatoire"),
    methode: yup.string().trim().required("La méthode est obligatoire"),
    fichier: yup
        .mixed()
        .test(
            "obligatoire",
            "Sélectionnez un justificatif",
            (fichiers) => fichiers && fichiers.length > 0,
        )
        .test(
            "format",
            "Utilisez un fichier PDF, PNG, JPG ou JPEG",
            (fichiers) =>
                !fichiers?.length || typesAcceptes.includes(fichiers[0].type),
        )
        .test(
            "taille",
            "Le fichier ne doit pas dépasser 5 Mo",
            (fichiers) => !fichiers?.length || fichiers[0].size <= 5 * 1024 * 1024,
        ),
});

export default function FormulairePaiementParent() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [parametres] = useSearchParams();
    const eleveId = Number(parametres.get("eleveId"));
    const mois = parametres.get("mois") || "";
    const [eleve, setEleve] = useState(null);
    const [message, setMessage] = useState("");
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
        defaultValues: {
            montant: 1500,
            methode: "Virement bancaire",
            fichier: null,
        },
    });

    useEffect(() => {
        verifierEnfant();
    }, [eleveId, user.id]);

    async function verifierEnfant() {
        try {
            const reponse = await elevesApi.getByParent(user.id, { size: 1000 });
            const enfant = reponse.data.content.find(
                (element) => element.id === eleveId,
            );
            if (!enfant || !/^\d{4}-\d{2}$/.test(mois)) {
                navigate("/paiements", { replace: true });
                return;
            }
            setEleve(enfant);
        } catch (exception) {
            navigate("/paiements", { replace: true });
        }
    }

    async function enregistrer(valeurs) {
        setMessage("");
        try {
            const reponse = await paiementsApi.create({
                eleveId,
                parentId: user.id,
                montant: valeurs.montant,
                methode: valeurs.methode,
                date: mois + "-01",
                statut: "EN_ATTENTE",
                urlJustificatif: null,
            });
            await paiementsApi.addProof(reponse.data.id, valeurs.fichier[0]);
            navigate("/paiements");
        } catch (exception) {
            setMessage(obtenirMessageErreur(exception));
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Ajouter un paiement</h1>
            </header>
            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                {message && <p className="notice erreur">{message}</p>}
                <div className="summary-box">
                    <strong>
                        {eleve ? eleve.prenom + " " + eleve.nom : "Chargement..."}
                    </strong>
                    <span>Mois : {libelleMois(mois)}</span>
                </div>
                <div className="form-grid">
                    <div className="form-group">
                        <label>Montant</label>
                        <input
                            className="field"
                            type="number"
                            step="0.01"
                            {...register("montant", { valueAsNumber: true })}
                        />
                        {errors.montant && (
                            <small className="error-text">{errors.montant.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Méthode</label>
                        <select className="field" {...register("methode")}>
                            <option value="Espèces">Espèces</option>
                            <option value="Virement bancaire">Virement bancaire</option>
                        </select>
                        {errors.methode && (
                            <small className="error-text">{errors.methode.message}</small>
                        )}
                    </div>
                    <div className="form-group" style={{ gridColumn: "1/-1" }}>
                        <label>Justificatif</label>
                        <input
                            className="field" type="file" accept=".pdf,.png,.jpg,.jpeg"
                            {...register("fichier")}
                        />
                        {errors.fichier && (
                            <small className="error-text">{errors.fichier.message}</small>
                        )}
                        <small className="muted">
                            Formats : PDF, PNG, JPG ou JPEG · 5 Mo maximum.
                        </small>
                    </div>
                </div>
                <div className="form-actions">
                    <button type="button" className="button secondary"
                        onClick={() => navigate("/paiements")}
                    >Annuler</button>
                    <button className="button" disabled={isSubmitting || !eleve}>
                        {isSubmitting ? "Enregistrement..." : "Ajouter le paiement"}
                    </button>
                </div>
            </form>
        </div>
    );
}

function libelleMois(mois) {
    if (!/^\d{4}-\d{2}$/.test(mois)) return "—";
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
