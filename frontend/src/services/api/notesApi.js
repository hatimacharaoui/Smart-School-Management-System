import api from "./Api.js";

export const notesApi = {
    getAll: () => api.get(),
    getByEleve: (eleveId, params) => api.get(`/notes/eleve/${eleveId}`, {params}),
    createGroup: (data) => api.post("/notes/groupe", data),
    update: (id, data) => api.put(`/notes/${id}`, data),

}