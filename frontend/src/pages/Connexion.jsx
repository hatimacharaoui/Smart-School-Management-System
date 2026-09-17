import React, {useState} from 'react';
import {Link, useNavigate} from "react-router-dom";
import * as yup from "yup";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/src/index.ts";
import {useAuth} from "../context/AuthContext.jsx";

const schema = yup.object({
    email: yup
        .string()
        .email("Adresse email invalide")
        .required("L'adresse email est obligatoire"),
    motDePasse: yup
        .string()
        .min(6, "Le mot de passe contient au moins 6 caractères")
        .required("Le mot de passe est obligatoire"),
});

export default function Connexion() {

    const [error, setError] = useState("");

    const {
        register,
        handleSubmit,
        formState: {errors, isSubmitting}
    } = useForm({
        resolver: yupResolver(schema), defaultValues: {email: "", motDePasse: ""}
    });

    const {connexion} = useAuth();
    const navigate = useNavigate();

    async function connecter(values) {
        setError("");
        try {
            await connexion(values.email, values.motDePasse);
            navigate("/TableauDeBord");
        } catch (exception) {
            setError("Email ou mot de passe incorrect");
        }
    }

    return (
        <div className="login-page">
            <div className="login-card">
                <div className="landing-ecole">
                    <span className="landing-logo">
                        <img
                            src="/logo-school.png"
                            alt="Logo école"
                            className="landing-logo-img" />
                    </span>
                        <div>
                            <strong>Collège Ibn Khaldoun</strong>
                            <small>Béni Mellal</small>
                        </div>
                </div>
                <h2>Connexion</h2>
                <p className="muted">
                    Saisissez vos identifiants pour accéder à votre espace.
                </p>

                {error && <p className="badge red">{error}</p>}

                <form onSubmit={handleSubmit(connecter)}>
                    <div className="form-group">
                        <label>Adresse email</label>
                        <input className="field" type="email" autoComplete="email" {...register("email")}/>
                        {errors.email && (
                            <small className="error-text">{errors.email.message}</small>
                        )}
                    </div>
                    <div className="form-group">
                        <label>Mot de passe</label>
                        <input className="field" type="password" autoComplete="current-password" {...register("motDePasse")}/>
                        {errors.motDePasse && (
                            <small className="error-text">{errors.motDePasse.message}</small>
                        )}
                    </div>
                    <button className="button" disabled={isSubmitting}>
                        {isSubmitting ? "connexion..." : "Se connecter"}
                    </button>
                    <Link className="back-link" to="/">
                        Retour à l'accueil
                    </Link>
                </form>
            </div>
        </div>
    );
}
