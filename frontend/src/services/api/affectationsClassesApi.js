import api from "./Api.js";

export const affectationsClassesApi = {
    getAll: (params) => api.get("/affectations-classes", {params})
};