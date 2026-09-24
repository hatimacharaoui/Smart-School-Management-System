import api from "./Api.js";

export const matieresApi = {
    getAll: (params) => api.get("/matieres", {params}),
    getById: (id) => api.get(`/matieres/${id}`),
    create: (data) => api.post("/matieres", data),
    update: (id, data) => api.put(`/matieres/${id}`, data),
    delete: (id) => api.delete(`/matieres/${id}`)

};