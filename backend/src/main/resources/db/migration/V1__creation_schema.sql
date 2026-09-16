CREATE TABLE utilisateur (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nom_complet VARCHAR(150) NOT NULL,
                             email VARCHAR(180) NOT NULL UNIQUE,
                             mot_de_passe VARCHAR(255) NOT NULL,
                             role VARCHAR(30) NOT NULL,
                             telephone VARCHAR(30),
                             reference_id BIGINT,
                             actif BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE administrateur (
                                id BIGINT PRIMARY KEY
);

CREATE TABLE matiere (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(100) NOT NULL UNIQUE,
                         coefficient INT NOT NULL,
                         actif BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE enseignant (
                            id BIGINT PRIMARY KEY,
                            prenom VARCHAR(100) NOT NULL,
                            nom VARCHAR(100) NOT NULL,
                            matiere_id BIGINT NOT NULL
);

CREATE TABLE parent (
                        id BIGINT PRIMARY KEY,
                        prenom VARCHAR(100) NOT NULL,
                        nom VARCHAR(100) NOT NULL
);

CREATE TABLE classe_scolaire (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 nom VARCHAR(30) NOT NULL UNIQUE,
                                 niveau VARCHAR(80) NOT NULL,
                                 enseignant_principal_id BIGINT
);

CREATE TABLE eleve (
                       id BIGINT PRIMARY KEY,
                       matricule VARCHAR(30) NOT NULL UNIQUE,
                       prenom VARCHAR(100) NOT NULL,
                       nom VARCHAR(100) NOT NULL,
                       date_naissance DATE,
                       adresse VARCHAR(255),
                       classe_id BIGINT NOT NULL,
                       parent_id BIGINT NOT NULL
);

CREATE TABLE classe_enseignant (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   enseignant_id BIGINT NOT NULL,
                                   classe_id BIGINT NOT NULL,
                                   CONSTRAINT uk_classe_enseignant UNIQUE (enseignant_id, classe_id)
);

CREATE TABLE devoir (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        titre VARCHAR(180) NOT NULL,
                        description VARCHAR(1000),
                        matiere_id BIGINT NOT NULL,
                        classe_id BIGINT NOT NULL,
                        enseignant_id BIGINT NOT NULL,
                        date_limite DATE NOT NULL,
                        statut VARCHAR(30) NOT NULL
);

CREATE TABLE note (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      eleve_id BIGINT NOT NULL,
                      devoir_id BIGINT NOT NULL,
                      enseignant_id BIGINT NOT NULL,
                      valeur DOUBLE NOT NULL,
                      valeur_maximale DOUBLE NOT NULL,
                      date DATE NOT NULL,
                      commentaire VARCHAR(500),
                      CONSTRAINT uk_eleve_devoir UNIQUE (eleve_id, devoir_id)
);

CREATE TABLE presence (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          eleve_id BIGINT NOT NULL,
                          classe_id BIGINT NOT NULL,
                          matiere_id BIGINT NOT NULL,
                          enseignant_id BIGINT NOT NULL,
                          date DATE NOT NULL,
                          statut VARCHAR(30) NOT NULL
);

CREATE TABLE paiement (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          eleve_id BIGINT NOT NULL,
                          parent_id BIGINT NOT NULL,
                          montant DOUBLE NOT NULL,
                          methode VARCHAR(80) NOT NULL,
                          date DATE NOT NULL,
                          statut VARCHAR(30) NOT NULL,
                          url_justificatif VARCHAR(500)
);

CREATE TABLE horaire_emploi_du_temp (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        jour VARCHAR(30) NOT NULL,
                                        heure_debut TIME NOT NULL,
                                        matiere_id BIGINT NOT NULL,
                                        classe_id BIGINT NOT NULL,
                                        enseignant_id BIGINT NOT NULL,
                                        salle VARCHAR(50) NOT NULL
);

CREATE TABLE notification (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              destinataire_id BIGINT NOT NULL,
                              titre VARCHAR(180) NOT NULL,
                              message VARCHAR(1000) NOT NULL,
                              type VARCHAR(30) NOT NULL,
                              date_creation DATETIME NOT NULL,
                              lue BOOLEAN NOT NULL DEFAULT FALSE,
                              entite_liee_id BIGINT
);

CREATE INDEX idx_eleve_classe ON eleve(classe_id);
CREATE INDEX idx_eleve_parent ON eleve(parent_id);
CREATE INDEX idx_devoir_classe ON devoir(classe_id);
CREATE INDEX idx_devoir_enseignant ON devoir(enseignant_id);
CREATE INDEX idx_presence_classe_date ON presence(classe_id, date);
CREATE INDEX idx_notification_destinataire ON notification(destinataire_id, date_creation);