import Accueil from "./pages/Accueil.jsx";
import Connexion from "./pages/Connexion.jsx";
import TableauDeBord from "./pages/TableauDeBord.jsx"
import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import Layout from "./components/Layout.jsx";


function App() {

  return (
      <Routes>
        <Route path="/" element={<Accueil />} />
          <Route path="/connexion" element={<Connexion />} />
          <Route element={<ProtectedRoute><Layout /></ProtectedRoute>} >
          <Route path="/TableauDeBord" element={<TableauDeBord />} />



          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
  );
}

export default App
