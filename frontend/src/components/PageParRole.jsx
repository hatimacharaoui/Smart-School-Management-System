import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function PageParRole({
                                        administrateur: Administrateur,
                                        enseignant: Enseignant,
                                        eleve: Eleve,
                                        parent: Parent,
                                    }) {
    const { user } = useAuth();

    if (user.role === "ADMINISTRATEUR" && Administrateur) {
        return <Administrateur />;
    }
    if (user.role === "ENSEIGNANT" && Enseignant) {
        return <Enseignant />;
    }
    if (user.role === "ELEVE" && Eleve) {
        return <Eleve />;
    }
    if (user.role === "PARENT" && Parent) {
        return <Parent />;
    }

    return <Navigate to="/tableau-de-bord" replace />;
}
