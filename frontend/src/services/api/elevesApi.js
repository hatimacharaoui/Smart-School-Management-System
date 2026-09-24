import api from "./Api.js";

export const elevesApi = {
    getAll: () => api.get("/eleves"),
    getByParent: (parentId, params) => api.get(`api/eleves/parent/${parentId}`, {params}),
    getById: (id) => api.get(`/eleves/${id}`),
    getByClasse: (classeId, params) => api.get(`/eleves/classe/${classeId}`, { params }),
    create: (data) => api.post("/eleves", data),
    update: (id, data) => api.put(`/eleves/${id}`, data),
    delete: (id) => api.delete(`/eleves/${id}`),

}