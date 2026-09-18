export function carte({label, value}) {
    return (
        <div className="card stat-card">
            <span className="muted">{label}</span>
            <strong>{value}</strong>
        </div>
    );
}

