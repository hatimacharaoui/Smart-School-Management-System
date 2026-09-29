# Smart School Management System

Application web de gestion scolaire destinée à faciliter le suivi des élèves, des enseignants, des parents, des classes, des notes, des présences, des devoirs, des paiements et des emplois du temps.

## Fonctionnalités principales

- Authentification sécurisée avec JWT et BCrypt.
- Gestion de quatre rôles : Administrateur, Enseignant, Élève et Parent.
- Gestion des élèves, enseignants, parents, classes et matières.
- Gestion des devoirs, notes et présences.
- Consultation des emplois du temps.
- Gestion des paiements et des justificatifs.
- Notifications selon le rôle de l’utilisateur.
- Documentation de l’API avec Swagger.
- Cache avec Spring Cache et Redis.
- Migrations de la base de données avec Flyway.

## Technologies utilisées

### Backend

- Java 21
- Spring Boot
- Spring Security et JWT
- Spring Data JPA et Hibernate
- MySQL
- Flyway
- Redis
- MapStruct et Lombok
- Maven

### Frontend

- React
- Vite
- React Router
- Axios
- React Hook Form et Yup
- Context API

### DevOps

- Docker et Docker Compose
- GitHub Actions
- SonarQube

## Structure du projet

```text
smartschool/
├── backend/            API Spring Boot
├── frontend/           Application React
├── .github/workflows/  Pipeline CI/CD
├── docker-compose.yml
└── sonar-project.properties
```

## Prérequis

- Java 21
- Maven
- Node.js 22 et npm
- MySQL 8
- Docker, facultatif

## Installation locale

### 1. Base de données

Créer une base MySQL nommée :

```sql
CREATE DATABASE smart_school;
```

Les tables et les données initiales sont créées automatiquement par Flyway au démarrage du backend.

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

Le backend est disponible sur :

```text
http://localhost:8080
```

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Le frontend est disponible sur :

```text
http://localhost:5173
```

## Configuration

Les principales variables d’environnement sont :

| Variable | Description | Valeur locale |
|---|---|---|
| `DB_URL` | URL MySQL | `jdbc:mysql://localhost:3306/smart_school` |
| `DB_USERNAME` | Utilisateur MySQL | `root` |
| `DB_PASSWORD` | Mot de passe MySQL | `root` |
| `JWT_SECRET` | Clé de signature JWT | Clé de développement |
| `FRONTEND_URL` | URL autorisée par CORS | `http://localhost:5173` |
| `VITE_API_URL` | URL de l’API utilisée par React | `http://localhost:8080/api` |

## Swagger

Après le démarrage du backend, la documentation de l’API est disponible sur :

```text
http://localhost:8080/swagger-ui.html
```

## Lancement avec Docker

Depuis la racine du projet :

```bash
docker compose up --build
```

Services disponibles :

- Frontend : `http://localhost:3000`
- Backend : `http://localhost:8080`
- Swagger : `http://localhost:8080/swagger-ui.html`
- SonarQube : `http://localhost:9000`

Pour arrêter les services :

```bash
docker compose down
```

## Compilation

Backend sans exécuter les tests :

```bash
cd backend
mvn -Dmaven.test.skip=true clean package
```

Frontend :

```bash
cd frontend
npm ci
npm run build
```

## Auteur

**Hatim Acharaoui**  
Développeur Full-Stack Java / Spring Boot / React
