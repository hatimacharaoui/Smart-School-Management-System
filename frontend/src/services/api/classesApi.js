import api from "./Api.js";

export const classesApi = {
    getAll: (params) => api.get("/classes", {params}),

};