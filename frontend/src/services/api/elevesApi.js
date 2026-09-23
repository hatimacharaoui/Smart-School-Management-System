import api from "./Api.js";

export const elevesApi = {
    getAll: () => api.get("/eleves"),
    getByParent: (parentId, params) => api.get(`api/eleves/parent/${parentId}`, {params}),

}