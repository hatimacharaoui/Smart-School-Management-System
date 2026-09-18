import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import TableauDeBordAdministrateur from "./TableauDeBordAdministrateur.jsx";
import TableauDeBordEnseignant from "./TableauDeBordEnseignant.jsx";
import TableauDeBordParent from "./TableauDeBordParent.jsx";

export default function TableauDeBord() {
    const { user } = useAuth();

    if (user.role === "ADMINISTRATEUR") {
        return <TableauDeBordAdministrateur />;
    }

    if (user.role === "ENSEIGNANT") {
        return <TableauDeBordEnseignant />;
    }

    if (user.role === "PARENT") {
        return <TableauDeBordParent />;
    }

    if (user.role === "ELEVE") {
        return <Navigate to="/devoirs" replace />;
    }
}