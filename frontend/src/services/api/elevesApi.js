import api from "./Api.js";

export const elevesApi = {
    getAll: () => api.get("/eleves"),

}