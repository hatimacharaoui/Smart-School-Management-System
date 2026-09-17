import Accueil from "./pages/Accueil.jsx";
import Connexion from "./pages/Connexion.jsx";
import TableauDeBord from "./pages/TableauDeBord.jsx"
import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute.jsx";


function App() {

  return (
      <Routes>
        <Route path="/" element={<Accueil />} />
          <Route path="/connexion" element={<Connexion />} />
          <Route path="/TableauDeBord" element={<ProtectedRoute><TableauDeBord /></ProtectedRoute>} />

          <Route path="*" element={<Navigate to="/" replace />}></Route>
      </Routes>
  );
}

export default App
