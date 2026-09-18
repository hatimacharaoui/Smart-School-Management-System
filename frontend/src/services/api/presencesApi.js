import api from "./Api.js";

export const presencesApi = {
    getPresence: (params) => api.get("/presences/absent-retard", {params}),

};