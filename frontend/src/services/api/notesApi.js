import api from "./Api.js";

export const notesApi = {
    getAll: (params) => api.get("/notes", { params }),
    getByEleve: (eleveId, params) => api.get(`/notes/eleve/${eleveId}`, {params}),
    createGroup: (data) => api.post("/notes/groupe", data),
    update: (id, data) => api.put(`/notes/${id}`, data),

}