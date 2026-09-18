import {
    Bell,
    BookOpen,
    CalendarDays,
    ClipboardCheck,
    CreditCard,
    FileText,
    GraduationCap,
    LayoutDashboard,
    LogOut,
    School,
    UserRound,
    Users,
} from "lucide-react";
import {NavLink, Outlet} from "react-router-dom";
import {useAuth} from "../context/AuthContext.jsx";

const menus = {
    ADMINISTRATEUR: [
        { to: "/tableau-de-bord", label: "Tableau de bord", icon: LayoutDashboard },
        { to: "/eleves", label: "Élèves", icon: Users },
        { to: "/enseignants", label: "Enseignants", icon: BookOpen },
        { to: "/parents", label: "Parents", icon: UserRound },
        { to: "/classes", label: "Classes", icon: School },
        { to: "/matieres", label: "Matières", icon: BookOpen },
        { to: "/notes", label: "Notes", icon: ClipboardCheck },
        { to: "/presences", label: "Présences", icon: ClipboardCheck },
        { to: "/devoirs", label: "Devoirs", icon: FileText },
        { to: "/emploi-du-temps", label: "Emploi du temps", icon: CalendarDays },
        { to: "/paiements", label: "Paiements", icon: CreditCard },
        { to: "/notifications", label: "Notifications", icon: Bell },
        { to: "/profil", label: "Mon profil", icon: UserRound }
    ],
    ENSEIGNANT: [
        { to: "/tableau-de-bord", label: "Tableau de bord", icon: LayoutDashboard },
        { to: "/eleves", label: "Élèves", icon: Users },
        { to: "/notes", label: "Notes", icon: ClipboardCheck },
        { to: "/presences", label: "Présences", icon: ClipboardCheck },
        { to: "/devoirs", label: "Devoirs", icon: FileText },
        { to: "/emploi-du-temps", label: "Emploi du temps", icon: CalendarDays },
        { to: "/notifications", label: "Notifications", icon: Bell },
        { to: "/profil", label: "Mon profil", icon: UserRound }
    ],
    ELEVE: [
        { to: "/notes", label: "Mes notes", icon: ClipboardCheck },
        { to: "/matieres", label: "Matières", icon: BookOpen },
        { to: "/devoirs", label: "Devoirs", icon: FileText },
        { to: "/emploi-du-temps", label: "Emploi du temps", icon: CalendarDays },
        { to: "/notifications", label: "Notifications", icon: Bell },
        { to: "/profil", label: "Mon profil", icon: UserRound }
    ],
    PARENT: [
        { to: "/tableau-de-bord", label: "Tableau de bord", icon: LayoutDashboard },
        { to: "/notes", label: "Notes", icon: ClipboardCheck },
        { to: "/matieres", label: "Matières", icon: BookOpen },
        { to: "/devoirs", label: "Devoirs", icon: FileText },
        { to: "/presences", label: "Présences", icon: ClipboardCheck },
        { to: "/emploi-du-temps", label: "Emploi du temps", icon: CalendarDays },
        { to: "/paiements", label: "Paiements", icon: CreditCard },
        { to: "/notifications", label: "Notifications", icon: Bell },
        { to: "/profil", label: "Mon profil", icon: UserRound }
    ],
};

export default function Layout() {
    const {user, deconnexion} = useAuth();
    const items = menu[user.role] || [];

    return (
        <div className="app-school">
            <aside className="sidebar">
                <div className="landing-ecole" >
                    <span className="landing-logo">
                        <img src="/logo-school.png" alt="Logo école" className="landing-logo-img" />
                    </span>
                    <div>
                        <strong>Collège Ibn Khaldoun</strong>
                        <small>Béni Mellal</small>
                    </div>
                </div>
                <nav>
                    {items.map((item) => {
                        return (
                            <NavLink key={item.to} to={item.to}
                                     className={({isActive}) => isActive? "nav-link active" : "nav-link"}>
                                <item.icon size={18}/>
                            </NavLink>
                        );
                    })}
                </nav>
                <button className="logout" onClick={deconnexion}>
                    <LogOut size={18}/>
                    Déconnexion
                </button>
            </aside>
            <section className="workspace">
                <header className="topbar">
                    <Bell size={18}/>
                    <span className="identity">
                        <strong>{user.nomComplet}</strong>
                        <small>{libelleRole(user.role)}</small>
                    </span>
                </header>
                <main className="content">
                    <Outlet />
                </main>
            </section>
        </div>
    );
}

function libelleRole(role) {
    if (role === "ADMINISTRATEUR") {
        return "Administrateur";
    } else if (role === "ENSEIGNANT") {
        return "Enseignant";
    } else if (role === "ELEVE") {
        return "Élève";
    } else {
        return "Parent";
    }
}











