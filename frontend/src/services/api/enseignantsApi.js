import api from "./Api.js";

export const enseignantsApi = {
    getAll: (params) => api.get("/enseignants", {params}),

};