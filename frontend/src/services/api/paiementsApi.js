import api from "./Api.js";


export const paiementsApi = {
    getAll: (params) => api.get("/paiements", {params}),
    getByEleve: (eleveId, params) => api.get(`/paiements/eleve/${eleveId}`, {params}),
    getById: (id) => api.get(`/paiements/${id}`),
    getMonthly: (params) => api.get("/paiements/mensuels", { params }),
    create: (data) => api.post("/paiements", data),
    update: (id, data) => api.put(`/paiements/${id}`, data),
    updateStatus: (id, statut) =>
        api.patch(`/paiements/${id}/statut`, null, { params: { statut } }),
    addProof: (id, fichier) => {
        const formulaire = new FormData();
        formulaire.append("fichier", fichier);
        return api.post(`/paiements/${id}/justificatif`, formulaire);},

    viewProof: (id) => api.get(`/paiements/${id}/justificatif`, {responseType: "blob",}),
};