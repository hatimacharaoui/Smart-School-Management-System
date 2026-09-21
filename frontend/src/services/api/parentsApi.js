import api from "./Api.js";

export const parentsApi = {
    getAll: (params) => api.get("/parents", {params}),

};