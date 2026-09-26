import { useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import * as yup from "yup";
import { useAuth } from "../../context/AuthContext";
import {usersAPI as usersApi} from "../../services/api/usersApi.js";
import {obtenirMessageErreur} from "../../services/api/Api.js";

const schema = yup.object({
    nomComplet: yup.string().trim().required("Le nom complet est obligatoire"),
    email: yup
        .string()
        .email("Adresse email invalide")
        .required("L'adresse email est obligatoire"),
    telephone: yup.string().max(30, "30 caractères maximum"),
});

export default function ProfilAdministrateur() {
    const { user, deconnexion, mettreAJourUtilisateur } = useAuth();
    const [modification, setModification] = useState(false);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: yupResolver(schema),
        defaultValues: {
            nomComplet: user.nomComplet || "",
            email: user.email || "",
            telephone: user.telephone || "",
        },
    });

    async function enregistrer(valeurs) {
        setMessage("");
        try {
            await usersApi.updateProfile(user.id, valeurs);
            const emailModifie = valeurs.email !== user.email;
            mettreAJourUtilisateur(valeurs);

            if (emailModifie) {
                deconnexion();
                navigate("/connexion");
                return;
            }

            setMessage("Profil modifié avec succès.");
            setModification(false);
        } catch (exception) {
            setMessage(obtenirMessageErreur(exception));
        }
    }

    function annuler() {
        reset({
            nomComplet: user.nomComplet || "",
            email: user.email || "",
            telephone: user.telephone || "",
        });
        setModification(false);
        setMessage("");
    }

    return (
        <div>
            <header className="page-header">
                <h1>Mon profil</h1>
                {!modification && (
                    <button className="button" onClick={() => setModification(true)}>
                        Modifier mon profil</button>
                )}
            </header>
            {message && <p className="notice">{message}</p>}
            <form
                className="card card-body form-card"
                onSubmit={handleSubmit(enregistrer)} noValidate
            >
                <div className="form-grid">
                    <div className="form-group">
                        <label>Nom complet</label>
                        <input
                            className="field" readOnly={!modification}{...register("nomComplet")}
                        />
                        {errors.nomComplet && (
                            <small className="error-text">{errors.nomComplet.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Adresse email</label>
                        <input
                            className="field" type="email" readOnly={!modification}{...register("email")}
                        />
                        {errors.email && (
                            <small className="error-text">{errors.email.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Téléphone</label>
                        <input className="field" readOnly={!modification}
                            {...register("telephone")}
                        />
                        {errors.telephone && (
                            <small className="error-text">{errors.telephone.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Rôle</label>
                        <input className="field" value="Administrateur" disabled />
                    </div>
                </div>
                {modification && (
                    <div className="form-actions">
                        <button
                            className="button secondary" type="button" onClick={annuler}
                        >Annuler</button>
                        <button className="button" disabled={isSubmitting}>
                            {isSubmitting ? "Enregistrement..." : "Enregistrer"}
                        </button>
                    </div>
                )}
            </form>
        </div>
    );
}
