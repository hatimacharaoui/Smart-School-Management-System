import api from "./clientApi.js"

export const notificationsApi = {
    getByUser: (userId, params) => api.get(`/notifications/utilisateur/${userId}`, {params}),

}