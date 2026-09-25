import api from "./Api.js";

export const parentsApi = {
    getAll: (params) => api.get("/parents", {params}),
    getById: (id) => api.get(`/parents/${id}`),
    create: (data) => api.post("/parents", data),
    update: (id, data) => api.put(`/parents/${id}`, data),
    delete: (id) => api.delete(`/parents/${id}`),

};