import api from "./Api.js";

export const usersApi = {
    getAll: (params) => api.get("/utilisateurs", { params }),
    getById: (id) => api.get(`/utilisateurs/${id}`),
    updateProfile: (id, data) => api.put(`/utilisateurs/${id}/profil`, data),
};