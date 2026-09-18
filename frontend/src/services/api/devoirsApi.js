import api from "./Api.js";

export const devoirsApi = {
    getAll: (params) => api.get("/devoirs"),

};