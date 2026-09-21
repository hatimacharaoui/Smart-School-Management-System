import api from "./Api.js";

export const enseignantsApi = {
    getAll: (params) => api.get("/enseignants", {params}),
    getClasses: (id, params) => api.get(`/enseignants/${id}/classes`, {params}),


};