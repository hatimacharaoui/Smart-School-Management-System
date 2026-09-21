import api from "./Api.js";

export const devoirsApi = {
    getAll: (params) => api.get("/devoirs"),
    getByEnseignant: (enseignantId, params) => api.get(`/devoirs/enseignant/${enseignantId}`, {params}),


};