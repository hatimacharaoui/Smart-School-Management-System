import api from "./Api.js";

export const devoirsApi = {
    getAll: (params) => api.get("/devoirs", {params}),
    getByEnseignant: (enseignantId, params) => api.get(`/devoirs/enseignant/${enseignantId}`, {params}),
    getByClasse: (classeId, params) => api.get(`/devoirs/classes/${classeId}`, {params}),
    getById: (id) => api.get(`/devoirs/${id}`),
    create: (data) => api.post("/devoirs", data),
    update: (id, data) => api.put(`/devoirs/${id}`, data),
    updateStatus: (id, statut) => api.patch(`/devoirs/${id}/statut`, null, { params: { statut } }),
    delete: (id) => api.delete(`/devoirs/${id}`),

};