import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import {classesApi} from "../../services/api/classesApi.js";
import {elevesApi} from "../../services/api/elevesApi.js";

export default function VoirEleveEnseignant() {
    const { id } = useParams();
    const [eleve, setEleve] = useState(null);
    const [classes, setClasses] = useState([]);
    const [erreur, setErreur] = useState("");

    useEffect(() => {
        charger();
    }, [id]);

    async function charger() {
        setErreur("");
        try {
            const reponses = await Promise.all([
                elevesApi.getById(id),
                classesApi.getAll({ size: 1000 }),
            ]);
            setEleve(reponses[0].data);
            setClasses(reponses[1].data.content);

        } catch (exception) {
            setErreur("Impossible de charger l’élève.");
        }
    }

    return (
        <div>
            <header className="page-header">
                <h1>Détails de l’élève</h1>
            </header>
            {erreur && <p className="notice">{erreur}</p>}
            {eleve && (
                <div className="card card-body form-card">
                    <div className="form-grid">
                        <Champ label="N élève" valeur={eleve.matricule} />
                        <Champ label="Prénom" valeur={eleve.prenom} />
                        <Champ label="Nom" valeur={eleve.nom} />
                        <Champ label="Email" valeur={eleve.email} />
                        <Champ label="Téléphone" valeur={eleve.telephone} />
                        <Champ label="Date de naissance" valeur={eleve.dateNaissance} />
                        <Champ label="Adresse" valeur={eleve.adresse} />
                        <Champ label="Classe" valeur={
                                classes.find((classe) => classe.id === eleve.classeId)?.nom ||
                                eleve.classeId } />
                    </div>
                    <div className="form-actions">
                        <Link className="button secondary" to="/eleves">Retour</Link>
                    </div>
                </div>
            )}
        </div>
    );
}

function Champ({ label, valeur }) {
    return (
        <div className="form-group">
            <label>{label}</label>
            <input className="field" value={valeur || ""} readOnly />
        </div>
    );
}
