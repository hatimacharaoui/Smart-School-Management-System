import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import * as yup from "yup";
import {paiementsApi} from "../../services/api/paiementsApi.js";

const typesAcceptes = ["application/pdf", "image/png", "image/jpeg"];

const schema = yup.object({
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

export default function FormulaireJustificatifPaiement() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [paiement, setPaiement] = useState(null);
    const [message, setMessage] = useState("");
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
    });

    useEffect(() => {
        charger();
    }, [id]);

    async function charger() {
        try {
            const reponse = await paiementsApi.getById(id);
            setPaiement(reponse.data);
        } catch (exception) {
            setMessage("Impossible de charger le paiement ");
        }
    }

    async function enregistrer(valeurs) {
        setMessage("");
        try {
            await paiementsApi.addProof(id, valeurs.fichier[0]);
            navigate("/paiements");
        } catch (exception) {
            const messageApi = exception.response?.data?.message;
            setMessage(messageApi || "Impossible d’ajouter le justificatif ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Ajouter un justificatif de paiement</h1>
            </header>
            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)}
                noValidate
            >
                {message && <p className="notice">{message}</p>}
                {paiement && (
                    <div className="summary-box">
                        <strong>Paiement #{paiement.id}</strong>
                        <span>
              {paiement.montant} DH · {paiement.date} · {paiement.methode}
            </span>
                    </div>
                )}
                <div className="form-group">
                    <label>Justificatif</label>
                    <input
                        className="field" type="file" accept=".pdf,.png,.jpg,.jpeg"
                        {...register("fichier")}
                    />
                    {errors.fichier && (
                        <small className="error-text">{errors.fichier.message}</small>
                    )}
                    <small className="muted">
                        Formats : PDF, PNG, JPG ou JPEG
                    </small>
                </div>
                <div className="form-actions">
                    <button
                        className="button secondary"
                        type="button"
                        onClick={() => navigate("/paiements")}
                    >
                        Annuler
                    </button>
                    <button className="button" disabled={isSubmitting}>
                        {isSubmitting ? "Enregistrement..." : "Enregistrer le justificatif"}
                    </button>
                </div>
            </form>
        </div>
    );
}
