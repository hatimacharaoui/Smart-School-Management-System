import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
});


api.interceptors.request.use(function (config) {
    const token = localStorage.getItem("token");

    if (token) {
        config.headers.Authorization = "Bearer " + token;
    }
    return config;
});


const messageStatut= {
    401: "Votre session a expiré. Veuillez vous reconnecter.",
    403: "Vous n’avez pas l’autorisation d’effectuer cette action.",
    404: "La ressource demandée est introuvable.",
    500: "Une erreur interne est survenue. Réessayez plus tard.",
};

api.interceptors.response.use(
    function (response) {
        return response;
    },

    function (error) {
        const statut = error.response?.status;
        const messageBackend = error.response?.data?.message;

        const message = messageBackend || messageStatut[statut] || "Impossible de contacter le serveur.";

        error.messageUtilisateur = message;

        if (statut === 401) {
            localStorage.clear();

            if (window.location.pathname !== "/connexion") {
                window.location.href = "/connexion";
            }
        }

        return Promise.reject(error);
    },
);

export function obtenirMessageErreur(error) {
    return (
        error.messageUtilisateur || "Une erreur inattendue est survenue."
    );
}

export default api;

