import api from "./Api.js";

export const presencesApi = {
    getPresence: (params) => api.get("/presences/absent-retard", {params}),
    getByEleve: (eleveId, params) => api.get(`/presences/eleve/${eleveId}`, {params}),
    getAlerts: (params) => api.get("/presences/alertes", { params }),
    getByClasse: (classeId, params) => api.get(`/presences/classe/${classeId}`, { params }),
    create: (data) => api.post("/presences", data),
    createGroup: (data) => api.post("/presences/groupe", data),

};