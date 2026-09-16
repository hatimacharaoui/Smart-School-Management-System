import {
    BookOpen,
    Clock,
    GraduationCap,
    Mail,
    MapPin,
    Menu,
    Phone,
    School,
    ShieldCheck,
    Users,
    X,
} from "lucide-react";
import {useState} from "react";
import { Link } from "react-router-dom";

const roles = [
    {
        title: "Administration",
        text: "Gérer les élèves, les enseignants, les classes, les Parents et l'organisation du collège.",
        icon: ShieldCheck,
    },
    {
        title: "Enseignant",
        text: "Saisir les notes, publier les devoirs et consulter l'emploi du temps.",
        icon: BookOpen,
    },
    {
        title: "Élève",
        text: "Consulter les notes, les devoirs, les matières et l'emploi du temps.",
        icon: GraduationCap,
    },
    {
        title: "Parent",
        text: "Suivre la scolarité, les présences, les résultats et les paiements des enfants.",
        icon: Users,
    },
];

export default function Accueil() {

    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <div className="landing">
            <header className="landing-nav">
                <a className="landing-ecole" href="#accueil">
                    <span className="landing-logo">
                        <img
                            src="/logo-school.png"
                            alt="Logo école"
                            className="landing-logo-img" />
                    </span>
                    <div>
                        <strong>Collège Ibn Khaldoun</strong>
                        <small>Béni Mellal</small>
                    </div>
                </a>

                <nav className={menuOpen ? "landing-links open" : "landing-links"}>
                    <a href="#accueil">Accueil</a>
                    <a href="#apropos">A propos</a>
                    <a href="#connexion">Espace de connexion</a>
                    <a href="#contact">Contact</a>

                    <Link className="button" to="/connexion">
                        Se connecter
                    </Link>
                </nav>
                <button className="menu-button" onClick={() => setMenuOpen(!menuOpen)}>
                    {menuOpen ? <X /> : <Menu />}
                </button>
            </header>

            <main>
                <section id="accueil" className="landing-hero">
                    <div>
                        <span className="title">
                            Collège privé - Béni Mellal
                        </span>
                        <h1>Collège Ibn Khaldoun</h1>
                        <p>
                            Une plateforme numérique moderne pour faciliter la gestion scolaire,
                            améliorer le suivi des élèves et renforcer la communication entre
                            l’administration, les enseignants et les parents.
                        </p>

                        <div className="hero-actions">
                            <Link className="button" to="/connexion">
                                Accéder à mon espace
                            </Link>
                            <a className="button secondary" href="#contact">
                                Nous contacter
                            </a>
                        </div>
                    </div>

                    <div className="hero-photo">
                        <img src="/ecole.png" alt="Établissement scolaire"/>
                    </div>
                </section>

                <section className="landing-section">
                    <h2>Le collège en chiffres</h2>
                    <div className="landing-stats">
                        <article>
                            <Users />
                            <strong>180</strong>
                            <span>Elèves</span>
                        </article>
                        <article>
                            <BookOpen />
                            <strong>12</strong>
                            <span>Enseignants</span>
                        </article>
                        <article>
                            <School />
                            <strong>9</strong>
                            <span>Classes</span>
                        </article>
                    </div>
                </section>

                <section id="apropos" className="landing-section alternate">
                    <span className="title">À propos</span>
                    <h3>Un établissement engagé pour la réussite des élèves</h3>
                    <p className="section-intro">
                        Le Collège Ibn Khaldoun accompagne les élèves de la première à la troisième
                        année du collège à travers un cadre éducatif structuré, moderne et orienté
                        vers l’excellence.
                    </p>
                    <div className="feature-grid">
                        <article>
                            <ShieldCheck />
                            <div>
                                <strong>Suivi personnalisé</strong>
                                <p>Une vision claire des notes, devoirs et présences.</p>
                            </div>
                        </article>
                        <article>
                            <BookOpen />
                            <div>
                                <strong>Équipe pédagogique</strong>
                                <p>
                                    Des enseignants disponibles et un apprentissage structuré.
                                </p>
                            </div>
                        </article>
                        <article>
                            <School />
                            <div>
                                <strong>Outils numériques</strong>
                                <p>
                                    Une plateforme accessible aux quatre profils de
                                    l'établissement.
                                </p>
                            </div>
                        </article>
                        <article>
                            <Users />
                            <div>
                                <strong>Communication</strong>
                                <p>
                                    Des notifications utiles pour les élèves et leurs parents.
                                </p>
                            </div>
                        </article>
                    </div>
                </section>

                <section id="connexion" className="landing-section">
                    <span className="title">Plateforme numérique</span>
                    <h3>Espace de connexion</h3>
                    <p className="section-intro">
                        Chaque membre de la communauté scolaire dispose d'un espace adapté à
                        ses besoins.
                    </p>
                    <div className="role-grid">
                        {roles.map(({ title, text, icon: Icon }) => (
                            <article key={title}>
                                     <span>
                                     <Icon />
                                     </span>
                                <strong>{title}</strong>
                                <p>{text}</p>
                            </article>
                        ))}
                    </div>
                    <div className="center-action">
                        <Link className="button" to="/connexion">
                            Ouvrir l'espace de connexion
                        </Link>
                    </div>
                </section>

                <section id="contact" className="landing-section alternate">
                    <span className="title">Contact</span>
                    <h2>Contactez-nous</h2>
                    <div className="contact-grid">
                        <article>
                            <MapPin />
                            <strong>Adresse</strong>
                            <span>24, Avenue Hassan II, Béni Mellal 23000, Maroc</span>
                        </article>
                        <article>
                            <Phone />
                            <strong>Téléphone</strong>
                            <span>+212 6 61 23 45 67</span>
                        </article>
                        <article>
                            <Mail />
                            <strong>Email</strong>
                            <span>contact@college-ibnkhaldoun.ma</span>
                        </article>
                    </div>
                    <p className="hours">
                        <Clock size={17} /> Lundi – Vendredi : 8h00 – 17h30 · Samedi : 8h00
                        – 12h30
                    </p>
                </section>

                <footer className="landing-footer">
                    <div>
                        <div className="landing-brand">
                            <span>
                                <GraduationCap />
                            </span>
                            <div>
                                <strong>Collège Ibn Khaldoun</strong><br/>
                                <small>Béni Mellal</small>
                            </div>
                        </div>
                    </div>
                    <div className="footer-links">
                        <strong>Liens </strong>
                        <a href="#accueil">Accueil</a>
                        <a href="#apropos">À propos</a>
                        <a href="#connexion">Espace de connexion</a>
                    </div>
                    <div className="footer-links">
                        <strong>Contact</strong>
                        <span>24, Avenue Hassan II</span>
                        <span>+212 5 23 48 56 78</span>
                        <span>contact@college-ibnkhaldoun.ma</span>
                    </div>
                    <p>
                        2026 Collège Ibn Khaldoun.
                        <br /> Tous droits réservés.
                    </p>
                </footer>
            </main>

        </div>
    );
}