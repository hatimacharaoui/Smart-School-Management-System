import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import * as yup from "yup";
import {notesApi as usersApi} from "../../services/api/notesApi.js";
import {notificationsApi} from "../../services/api/notificationsApi.js";

const schema = yup.object({
    destinataireId: yup
        .number()
        .typeError("Le destinataire est obligatoire")
        .required("Le destinataire est obligatoire"),
    titre: yup
        .string()
        .trim()
        .max(180, "180 caractères maximum")
        .required("Le titre est obligatoire"),
    message: yup
        .string()
        .trim()
        .max(1000, "1000 caractères maximum")
        .required("Le message est obligatoire"),
    type: yup.string().required(),
    lue: yup.boolean().required(),
});

export default function FormulaireNotification() {
    const [role, setRole] = useState("PARENT");
    const [recherche, setRecherche] = useState("");
    const [utilisateurs, setUtilisateurs] = useState([]);
    const [messageErreur, setMessageErreur] = useState("");
    const {
        register,
        handleSubmit,
        resetField,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
        defaultValues: {
            destinataireId: "",
            titre: "",
            message: "",
            type: "ANNONCE",
            lue: false,
        },
    });
    const navigate = useNavigate();

    useEffect(() => {
        chargerUtilisateurs();
    }, [role, recherche]);

    async function chargerUtilisateurs() {
        try {
            const reponse = await usersApi.getAll({ role, recherche, size: 100 });
            setUtilisateurs(reponse.data.content);
            resetField("destinataireId");
        } catch (exception) {
            setMessageErreur("Impossible de charger les destinataires.");
        }
    }

    async function envoyer(donnees) {
        setMessageErreur("");
        try {
            await notificationsApi.create(donnees);
            navigate("/notifications");
        } catch (exception) {
            setMessageErreur("Impossible d'envoyer la notification.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Ajouter une notification</h1>
            </header>
            <form
                className="card card-body form-card" onSubmit={handleSubmit(envoyer)} noValidate
            >
                {messageErreur && <p className="notice">{messageErreur}</p>}
                <div className="form-grid">
                    <div className="form-group">
                        <label>Rôle du destinataire</label>
                        <select className="field" value={role}
                            onChange={(event) => setRole(event.target.value)}
                        >
                            <option value="PARENT">Parent</option>
                            <option value="ENSEIGNANT">Enseignant</option>
                            <option value="ELEVE">Élève</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Rechercher un destinataire</label>
                        <input className="field" value={recherche}
                            onChange={(event) => setRecherche(event.target.value)}
                            placeholder="Nom du destinataire"
                        />
                    </div>
                    <div className="form-group">
                        <label>Destinataire</label>
                        <select
                            className="field"
                            {...register("destinataireId", { valueAsNumber: true })}
                        >
                            <option value="">Sélectionner</option>
                            {utilisateurs.map((utilisateur) => (
                                <option value={utilisateur.id} key={utilisateur.id}>
                                    {utilisateur.nomComplet}
                                </option>
                            ))}
                        </select>
                        {errors.destinataireId && (
                            <small className="error-text">
                                {errors.destinataireId.message}
                            </small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Titre</label>
                        <input className="field" {...register("titre")} />
                        {errors.titre && (
                            <small className="error-text">{errors.titre.message}</small>
                        )}
                    </div>
                    <div className="form-group" style={{ gridColumn: "1/-1" }}>
                        <label>Message</label>
                        <textarea className="field" {...register("message")} />
                        {errors.message && (
                            <small className="error-text">{errors.message.message}</small>
                        )}
                    </div>
                </div>
                <div className="form-actions">
                    <button type="button" className="button secondary"
                        onClick={() => navigate("/notifications")}
                    >
                        Annuler</button>
                    <button className="button" disabled={isSubmitting}>
                        {isSubmitting ? "Envoi..." : "Envoyer la notification"}
                    </button>
                </div>
            </form>
        </div>
    );
}
