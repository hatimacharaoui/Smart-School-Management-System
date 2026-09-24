import api from "./Api.js";

export const enseignantsApi = {
    getAll: (params) => api.get("/enseignants", {params}),
    getById: (id) => api.get(`/enseignants/${id}`),
    getClasses: (id, params) => api.get(`/enseignants/${id}/classes`, {params}),
    updateClasses: (id, classeIds) => api.put(`/enseignants/${id}/classes`, { classeIds }),
    create: (data) => api.post("/enseignants", data),
    update: (id, data) => api.put(`/enseignants/${id}`, data),
    delete: (id) => api.delete(`/enseignants/${id}`),


};