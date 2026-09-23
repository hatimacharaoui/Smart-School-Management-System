import api from "./Api.js";

export const devoirsApi = {
    getAll: (params) => api.get("/devoirs", {params}),
    getByEnseignant: (enseignantId, params) => api.get(`/devoirs/enseignant/${enseignantId}`, {params}),
    getByClasse: (classeId, params) => api.get(`/devoirs/classes/${classeId}`, {params}),


};