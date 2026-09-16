import { useState } from 'react'
import Accueil from "./pages/Accueil.jsx";
import Connexion from "./pages/Connexion.jsx";
import TableauDeBord from "./pages/TableauDeBord.jsx"
import { Routes, Route } from "react-router-dom";


function App() {


  return (
      <Routes>
        <Route path="/" element={<Accueil />} />
          <Route path="/connexion" element={<Connexion />} />
          <Route path="/TableauDeBord" element={<TableauDeBord />} />


      </Routes>
  );
}

export default App
