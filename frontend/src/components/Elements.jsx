export function Carte({label, value}) {
    return (
        <div className="card stat">
            <span className="muted">{label}</span>
            <strong>{value}</strong>
        </div>
    );
}


export function nomJourAujourdhui() {
    const jours = [
        "Dimanche",
        "Lundi",
        "Mardi",
        "Mercredi",
        "Jeudi",
        "Vendredi",
        "Samedi",
    ];
    return jours[new Date().getDay()];
}


