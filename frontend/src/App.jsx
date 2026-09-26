import Accueil from "./pages/Accueil.jsx";
import Connexion from "./pages/Connexion.jsx";
import TableauDeBord from "./pages/TableauDeBord.jsx"
import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import Layout from "./components/Layout.jsx";
import PageParRole from "./components/PageParRole.jsx";
import ProfilAdministrateur from "./pages/administrateur/ProfilAdministrateur.jsx";
import TableauDeBordAdministrateur from "./pages/administrateur/TableauDeBordAdministrateur.jsx";
import ElevesAdministrateur from "./pages/administrateur/ElevesAdministrateur.jsx";
import FormulaireEleve from "./pages/administrateur/FormulaireEleve.jsx";
import FicheEleveAdministrateur from "./pages/administrateur/FicheEleveAdministrateur.jsx";
import EnseignantsAdministrateur from "./pages/administrateur/EnseignantsAdministrateur.jsx";
import FormulaireEnseignant from "./pages/administrateur/FormulaireEnseignant.jsx";
import ParentsAdministrateur from "./pages/administrateur/ParentsAdministrateur.jsx";
import FormulaireParent from "./pages/administrateur/FormulaireParent.jsx";
import ClassesAdministrateur from "./pages/administrateur/ClassesAdministrateur.jsx";
import FormulaireRessource from "./pages/administrateur/FormulaireRessource.jsx";
import MatieresAdministrateur from "./pages/administrateur/MatieresAdministrateur.jsx";
import NotesAdministrateur from "./pages/administrateur/NotesAdministrateur.jsx";
import PresencesAdministrateur from "./pages/administrateur/PresencesAdministrateur.jsx";
import FormulaireNotification from "./pages/administrateur/FormulaireNotification.jsx";
import PaiementsAdministrateur from "./pages/administrateur/PaiementsAdministrateur.jsx";
import EmploiDuTempsAdministrateur from "./pages/administrateur/EmploiDuTempsAdministrateur.jsx";
import DevoirsAdministrateur from "./pages/administrateur/DevoirsAdministrateur.jsx";
import NotificationsAdministrateur from "./pages/administrateur/NotificationsAdministrateur.jsx";


function App() {

    return (
        <>
            <Routes>
                <Route path="/" element={<Accueil/>}/>
                <Route path="/connexion" element={<Connexion/>}/>
                <Route
                    element={
                        <ProtectedRoute>
                            <Layout/>
                        </ProtectedRoute>
                    }
                >
                    <Route
                        path="/tableau-de-bord"
                        element={
                            <PageParRole
                                administrateur={TableauDeBordAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/eleves"
                        element={
                            <PageParRole
                                administrateur={ElevesAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/eleves/add"
                        element={<PageParRole administrateur={FormulaireEleve}/>}
                    />
                    <Route
                        path="/eleves/:id"
                        element={
                            <PageParRole
                                administrateur={FicheEleveAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/eleves/:id/edit"
                        element={<PageParRole administrateur={FormulaireEleve}/>}
                    />
                    <Route
                        path="/enseignants"
                        element={<PageParRole administrateur={EnseignantsAdministrateur}/>}
                    />
                    <Route
                        path="/enseignants/add"
                        element={<PageParRole administrateur={FormulaireEnseignant}/>}
                    />
                    <Route
                        path="/enseignants/:id/edit"
                        element={<PageParRole administrateur={FormulaireEnseignant}/>}
                    />
                    <Route
                        path="/parents"
                        element={<PageParRole administrateur={ParentsAdministrateur}/>}
                    />
                    <Route
                        path="/parents/add"
                        element={<PageParRole administrateur={FormulaireParent}/>}
                    />
                    <Route
                        path="/parents/:id/edit"
                        element={<PageParRole administrateur={FormulaireParent}/>}
                    />
                    <Route
                        path="/classes"
                        element={<PageParRole administrateur={ClassesAdministrateur}/>}
                    />
                    <Route
                        path="/classes/add"
                        element={
                            <PageParRole
                                administrateur={() => <FormulaireRessource type="classes"/>}
                            />
                        }
                    />
                    <Route
                        path="/classes/:id/edit"
                        element={
                            <PageParRole
                                administrateur={() => <FormulaireRessource type="classes"/>}
                            />
                        }
                    />
                    <Route
                        path="/matieres"
                        element={
                            <PageParRole
                                administrateur={MatieresAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/matieres/add"
                        element={
                            <PageParRole
                                administrateur={() => <FormulaireRessource type="matieres"/>}
                            />
                        }
                    />
                    <Route
                        path="/matieres/:id/edit"
                        element={
                            <PageParRole
                                administrateur={() => <FormulaireRessource type="matieres"/>}
                            />
                        }
                    />
                    <Route
                        path="/notes"
                        element={
                            <PageParRole
                                administrateur={NotesAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/presences"
                        element={
                            <PageParRole
                                administrateur={PresencesAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/devoirs"
                        element={
                            <PageParRole
                                administrateur={DevoirsAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/emploi-du-temps"
                        element={
                            <PageParRole
                                administrateur={EmploiDuTempsAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/paiements"
                        element={
                            <PageParRole
                                administrateur={PaiementsAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/notifications"
                        element={
                            <PageParRole
                                administrateur={NotificationsAdministrateur}
                            />
                        }
                    />
                    <Route
                        path="/notifications/add"
                        element={<PageParRole administrateur={FormulaireNotification}/>}
                    />
                    <Route
                        path="/profil"
                        element={
                            <PageParRole
                                administrateur={ProfilAdministrateur}
                            />
                        }
                    />
                </Route>
                <Route path="*" element={<Navigate to="/"/>}/>
            </Routes>
        </>
    );
}

export default App
