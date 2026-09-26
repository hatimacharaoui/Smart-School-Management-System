import api from "./clientApi.js"

export const notificationsApi = {
    getByUser: (userId, params) => api.get(`/notifications/utilisateur/${userId}`, {params}),
    create: (data) => api.post("/notifications", data),
    markAsRead: (id) => api.patch(`/notifications/${id}/lue`),

}