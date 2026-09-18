import api from "./Api.js";

export const matieresApi = {
    getAll: (params) => api.get("/matieres", {params}),

};