import api from "./Api.js";

export const emploiDuTempsApi = {

    getAll: (params) => api.get("/api/emploi-du-temps", {params}),
    getByClasse: (classeId, params) => api.get(`/emploi-du-temps/classe/${classeId}`, { params }),
    getByEnseignant: (enseignantId, params) => api.get(`/emploi-du-temps/enseignant/${enseignantId}`, { params }),
    create: (data) => api.post("/emploi-du-temps", data),
    update: (id, data) => api.put(`/emploi-du-temps/${id}`, data),
}

