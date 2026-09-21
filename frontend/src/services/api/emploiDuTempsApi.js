import api from "./Api.js";

export const emploiDuTempsApi = {

    getAll: (params) => api.get("/api/emploi-du-temps", {params}),
}

