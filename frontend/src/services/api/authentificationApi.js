import api from "./Api.js";

export const authentificationApi = {
    login: function (data) {
        return api.post("/auth/connexion", data);
    },
};