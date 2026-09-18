import api from "./Api.js";

export const parentsApi = {
    getAll: (params) => api.get("api/parents", {params}),

};