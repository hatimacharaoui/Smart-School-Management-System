import { createContext, useContext, useState } from "react";
import {jwtDecode} from "jwt-decode";
import {authentificationApi} from "../services/api/authentificationApi.js";

const AuthContext = createContext(null);

function chargerUtilisateur() {
    const token = localStorage.getItem("token");
    const userEnregistre = localStorage.getItem("user");

    if (!token || !userEnregistre) {
        return null;
    }

    try {
        const contenuToken = jwtDecode(token);

        if ( contenuToken.exp && contenuToken.exp * 1000 < Date.now()) {
            localStorage.clear();
            return null;
        }

        return JSON.parse(userEnregistre);
    } catch {
        localStorage.clear();
        return null;
    }
}

export function AuthProvider({ children }) {
    const [user, setUser] = useState(chargerUtilisateur);

    async function connexion(email, motDePasse) {
        const response = await authentificationApi.login({
            email: email,
            motDePasse: motDePasse,
        });

        localStorage.setItem("token", response.data.token);
        localStorage.setItem("user", JSON.stringify(response.data));

        setUser(response.data);

        return response.data;
    }

    function deconnexion() {
        localStorage.clear();
        setUser(null);
    }

    function updateUtilisateur(donnees) {
        const userMisAJour = {...user, ...donnees};

        localStorage.setItem("user", JSON.stringify(userMisAJour));
        setUser(userMisAJour);
    }

    return (
        <AuthContext.Provider value={{
            user: user,
            connexion: connexion,
            deconnexion: deconnexion,
            mettreAJourUtilisateur: updateUtilisateur
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const contexte = useContext(AuthContext);

    if (!contexte) {
        throw new Error("useAuth doit être utilisé dans AuthProvider");
    }

    return contexte;
}