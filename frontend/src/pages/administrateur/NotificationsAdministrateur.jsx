import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination";
import { useAuth } from "../../context/AuthContext";
import usePagination, {PageVide,} from "../../hooks/usePagination";
import {notificationsApi} from "../../services/api/notificationsApi.js";

export default function NotificationsAdministrateur() {
    const { user } = useAuth();
    const [pageNotifications, setPageNotifications] = useState(PageVide());
    const [statut, setStatut] = useState("");
    const [erreur, setErreur] = useState("");
    const pagination = usePagination(pageNotifications, 10, statut);

    useEffect(() => {
        charger();
    }, [user.id, statut, pagination.numeroPage, pagination.taillePage]);

    async function charger() {
        setErreur("");
        try {
            let lue;
            if (statut === "LUE") lue = true;
            if (statut === "NON_LUE") lue = false;
            const reponse = await notificationsApi.getByUser(user.id, {lue, ...pagination.parametres,});
            setPageNotifications(reponse.data);
        } catch (exception) {
            setErreur("Impossible de charger les notifications ");
        }
    }

    async function marquerCommeLue(notification) {
        try {
            await notificationsApi.markAsRead(notification.id);
            await charger();
        } catch (exception) {
            setErreur("Impossible de modifier la notification ");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Notifications</h1>
                <Link className="button" to="/notifications/add">
                    Ajouter une notification
                </Link>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            <div className="filters">
                <select
                    className="field search" value={statut}
                    onChange={(event) => setStatut(event.target.value)}
                >
                    <option value="">Tous les statuts</option>
                    <option value="NON_LUE">Non lues</option>
                    <option value="LUE">Lues</option>
                </select>
            </div>
            <div className="card table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Titre</th>
                        <th>Message</th>
                        <th>Type</th>
                        <th>Date</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pagination.elementsPage.map((notification) => (
                        <tr key={notification.id}>
                            <td>{notification.titre}</td>
                            <td>{notification.message}</td>
                            <td>{libelleType(notification.type)}</td>
                            <td>{formaterDate(notification.dateCreation)}</td>
                            <td>
                  <span className={notification.lue ? "badge" : "badge amber"}>
                    {notification.lue ? "Lue" : "Non lue"}
                  </span>
                            </td>
                            <td className="actions">
                                {!notification.lue ? (
                                    <button
                                        className="button secondary small"
                                        onClick={() => marquerCommeLue(notification)}
                                    >Marquer comme lue</button>
                                ) : (
                                    <span className="muted">Aucune action</span>
                                )}
                            </td>
                        </tr>
                    ))}
                    {pagination.totalElements === 0 && (
                        <tr>
                            <td>Aucune notification trouvée</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <Pagination pagination={pagination} />
            </div>
        </div>
    );
}

function formaterDate(date) {
    return String(date).replace("T", " ").slice(0, 16);
}

function libelleType(type) {
    if (type === "NOTE") return "Note";
    if (type === "DEVOIR") return "Devoir";
    if (type === "PRESENCE") return "Présence";
    if (type === "PAIEMENT") return "Paiement";
    if (type === "UTILISATEUR") return "Utilisateur";
    return "Annonce";
}
