import api from "./Api.js";


export const paiementsApi = {
    getAll: (params) => api.get("/paiements", {params}),
    getByEleve: (eleveId, params) => api.get(`/paiements/eleve/${eleveId}`, {params}),

};