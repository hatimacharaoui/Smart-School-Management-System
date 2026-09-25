INSERT INTO matiere (id, nom, coefficient, actif) VALUES
                                                      (1, 'Mathématiques', 5, TRUE),
                                                      (2, 'Français', 4, TRUE),
                                                      (3, 'Arabe', 4, TRUE),
                                                      (4, 'Anglais', 3, TRUE),
                                                      (5, 'Physique-Chimie', 4, TRUE),
                                                      (6, 'Sciences de la Vie et de la Terre', 4, TRUE),
                                                      (7, 'Informatique', 2, TRUE),
                                                      (8, 'Histoire-Géographie', 3, TRUE),
                                                      (9, 'Éducation islamique', 2, TRUE),
                                                      (10, 'Éducation Physique', 2, TRUE),
                                                      (11, 'Éducation artistique', 2, TRUE);

INSERT INTO utilisateur (
    id, nom_complet, email, mot_de_passe, role, telephone, actif
) VALUES
    (1, 'Mohammed Alaoui', 'admin@smartschool.com', '{noop}123456',
     'ADMINISTRATEUR', '+212 6 12 34 56 78', TRUE);

INSERT INTO administrateur (id) VALUES (1);

INSERT INTO utilisateur (
    id, nom_complet, email, mot_de_passe, role, telephone, actif
) VALUES
      (2, 'Sara Bennani', 'sara.bennani@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 01', TRUE),
      (3, 'Hassan Alaoui', 'hassan.alaoui@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 02', TRUE),
      (4, 'Nadia El Fassi', 'nadia.elfassi@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 03', TRUE),
      (5, 'Youssef Berrada', 'youssef.berrada@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 04', TRUE),
      (6, 'Amina Idrissi', 'amina.idrissi@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 05', TRUE),
      (7, 'Rachid Tazi', 'rachid.tazi@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 06', TRUE),
      (8, 'Khadija Amrani', 'khadija.amrani@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 07', TRUE),
      (9, 'Mehdi Lahlou', 'mehdi.lahlou@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 08', TRUE),
      (10, 'Salma Ouazzani', 'salma.ouazzani@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 09', TRUE),
      (11, 'Omar Cherkaoui', 'omar.cherkaoui@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 10', TRUE),
      (12, 'Meryem Naciri', 'meryem.naciri@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 11', TRUE),
      (13, 'Anas Skalli', 'anas.skalli@college.ma', '{noop}123456', 'ENSEIGNANT', '+212 6 10 10 10 12', TRUE);

INSERT INTO enseignant (id, prenom, nom, matiere_id) VALUES
                                                         (2, 'Sara', 'Bennani', 1),
                                                         (3, 'Hassan', 'Alaoui', 2),
                                                         (4, 'Nadia', 'El Fassi', 3),
                                                         (5, 'Youssef', 'Berrada', 4),
                                                         (6, 'Amina', 'Idrissi', 5),
                                                         (7, 'Rachid', 'Tazi', 6),
                                                         (8, 'Khadija', 'Amrani', 7),
                                                         (9, 'Mehdi', 'Lahlou', 8),
                                                         (10, 'Salma', 'Ouazzani', 9),
                                                         (11, 'Omar', 'Cherkaoui', 10),
                                                         (12, 'Meryem', 'Naciri', 11),
                                                         (13, 'Anas', 'Skalli', 1);

INSERT INTO classe_scolaire (id, nom, niveau, enseignant_principal_id) VALUES
                                                                           (1, '1AC-1', 'Première année collège', 2),
                                                                           (2, '1AC-2', 'Première année collège', 3),
                                                                           (3, '1AC-3', 'Première année collège', 4),
                                                                           (4, '2AC-1', 'Deuxième année collège', 5),
                                                                           (5, '2AC-2', 'Deuxième année collège', 6),
                                                                           (6, '2AC-3', 'Deuxième année collège', 7),
                                                                           (7, '3AC-1', 'Troisième année collège', 8),
                                                                           (8, '3AC-2', 'Troisième année collège', 9),
                                                                           (9, '3AC-3', 'Troisième année collège', 10);

INSERT INTO classe_enseignant (enseignant_id, classe_id) VALUES
                                                             (2,1), (3,1), (4,1), (11,1),
                                                             (2,2), (3,2), (5,2), (12,2),
                                                             (2,3), (4,3), (6,3), (13,3),
                                                             (5,4), (6,4), (7,4), (11,4),
                                                             (5,5), (6,5), (8,5), (12,5),
                                                             (5,6), (7,6), (9,6), (13,6),
                                                             (8,7), (9,7), (10,7), (11,7),
                                                             (8,8), (9,8), (3,8), (12,8),
                                                             (8,9), (10,9), (4,9), (13,9);

INSERT INTO utilisateur (
    id, nom_complet, email, mot_de_passe, role, telephone, actif
)
SELECT
    13 + ((classe.id - 1) * 20 + numero.valeur),
    CONCAT(
            CASE MOD(numero.valeur + classe.id, 12)
                WHEN 0 THEN 'Adam' WHEN 1 THEN 'Sara' WHEN 2 THEN 'Yassine'
                WHEN 3 THEN 'Salma' WHEN 4 THEN 'Omar' WHEN 5 THEN 'Aya'
                WHEN 6 THEN 'Mehdi' WHEN 7 THEN 'Imane' WHEN 8 THEN 'Anas'
                WHEN 9 THEN 'Meryem' WHEN 10 THEN 'Ilyas' ELSE 'Kawtar'
                END,
            ' ',
            CASE MOD(numero.valeur * 2 + classe.id, 12)
                WHEN 0 THEN 'Acharaoui' WHEN 1 THEN 'El Mansouri' WHEN 2 THEN 'Bensouda'
                WHEN 3 THEN 'Chraibi' WHEN 4 THEN 'Tazi' WHEN 5 THEN 'Benhaddou'
                WHEN 6 THEN 'Alaoui' WHEN 7 THEN 'Idrissi' WHEN 8 THEN 'Berrada'
                WHEN 9 THEN 'Amrani' WHEN 10 THEN 'El Fassi' ELSE 'Bennani'
                END
    ),
    CONCAT('eleve', (classe.id - 1) * 20 + numero.valeur, '@college.ma'),
    '{noop}123456',
    'ELEVE',
    CONCAT('+212 6 30 ', LPAD(classe.id, 2, '0'), ' ', LPAD(numero.valeur, 2, '0'), ' 00'),
    TRUE
FROM classe_scolaire classe
         CROSS JOIN (
    SELECT 1 valeur UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
) numero;

INSERT INTO eleve (
    id, matricule, prenom, nom, date_naissance, adresse, classe_id, parent_id
)
SELECT
    13 + ((classe.id - 1) * 20 + numero.valeur),
    CONCAT('ELV-2026-', LPAD((classe.id - 1) * 20 + numero.valeur, 3, '0')),
    CASE MOD(numero.valeur + classe.id, 12)
        WHEN 0 THEN 'Adam' WHEN 1 THEN 'Sara' WHEN 2 THEN 'Yassine' WHEN 3 THEN 'Salma'
        WHEN 4 THEN 'Omar' WHEN 5 THEN 'Aya' WHEN 6 THEN 'Mehdi' WHEN 7 THEN 'Imane'
        WHEN 8 THEN 'Anas' WHEN 9 THEN 'Meryem' WHEN 10 THEN 'Ilyas' ELSE 'Kawtar'
        END,
    CASE MOD(numero.valeur * 2 + classe.id, 12)
        WHEN 0 THEN 'Acharaoui' WHEN 1 THEN 'El Mansouri' WHEN 2 THEN 'Bensouda'
        WHEN 3 THEN 'Chraibi' WHEN 4 THEN 'Tazi' WHEN 5 THEN 'Benhaddou'
        WHEN 6 THEN 'Alaoui' WHEN 7 THEN 'Idrissi' WHEN 8 THEN 'Berrada'
        WHEN 9 THEN 'Amrani' WHEN 10 THEN 'El Fassi' ELSE 'Bennani'
        END,
    DATE_ADD('2012-01-01', INTERVAL ((classe.id - 1) * 20 + numero.valeur) DAY),
    CONCAT('Quartier scolaire, Béni Mellal - ', classe.nom),
    classe.id,
    CASE
        WHEN ((classe.id - 1) * 20 + numero.valeur) <= 160
            THEN 193 + CEIL(((classe.id - 1) * 20 + numero.valeur) / 2)
        ELSE 193 + (((classe.id - 1) * 20 + numero.valeur) - 80)
        END
FROM classe_scolaire classe
         CROSS JOIN (
    SELECT 1 valeur UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
) numero;

UPDATE utilisateur
SET nom_complet = 'Adam Acharaoui', email = 'adam1@college.ma'
WHERE id = 14;

UPDATE utilisateur
SET nom_complet = 'Sara El Mansouri', email = 'sara2@college.ma'
WHERE id = 15;

UPDATE utilisateur
SET nom_complet = 'Yassine Bensouda', email = 'yassine3@college.ma'
WHERE id = 16;

UPDATE eleve SET prenom = 'Adam', nom = 'Acharaoui' WHERE id = 14;
UPDATE eleve SET prenom = 'Sara', nom = 'El Mansouri' WHERE id = 15;
UPDATE eleve SET prenom = 'Yassine', nom = 'Bensouda' WHERE id = 16;

INSERT INTO utilisateur (
    id, nom_complet, email, mot_de_passe, role, telephone, actif
) VALUES
      (194, 'Ahmed Benali', 'ahmed.benali@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 01', TRUE),
      (195, 'Fatima El Mansouri', 'fatima.elmansouri@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 02', TRUE),
      (196, 'Karim Bensouda', 'karim.bensouda@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 03', TRUE),
      (197, 'Nadia Chraibi', 'nadia.chraibi@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 04', TRUE),
      (198, 'Omar Tazi', 'omar.tazi@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 05', TRUE),
      (199, 'Leila Benhaddou', 'leila.benhaddou@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 06', TRUE),
      (200, 'Amine Alaoui', 'amine.alaoui@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 07', TRUE),
      (201, 'Salma Idrissi', 'salma.idrissi@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 08', TRUE),
      (202, 'Ayoub Berrada', 'ayoub.berrada@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 09', TRUE),
      (203, 'Imane Amrani', 'imane.amrani@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 10', TRUE),
      (204, 'Mehdi El Fassi', 'mehdi.elfassi@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 11', TRUE),
      (205, 'Ghita Bennani', 'ghita.bennani@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 12', TRUE),
      (206, 'Zakaria Ouazzani', 'zakaria.ouazzani@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 13', TRUE),
      (207, 'Kawtar Lahlou', 'kawtar.lahlou@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 14', TRUE),
      (208, 'Anas Skalli', 'anas.skalli@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 15', TRUE),
      (209, 'Meryem Naciri', 'meryem.naciri@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 16', TRUE),
      (210, 'Ilyas Cherkaoui', 'ilyas.cherkaoui@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 17', TRUE),
      (211, 'Aya Berrada', 'aya.berrada@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 18', TRUE),
      (212, 'Yassine Tazi', 'yassine.tazi@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 19', TRUE),
      (213, 'Siham El Idrissi', 'siham.elidrissi@example.com', '{noop}123456', 'PARENT', '+212 6 20 00 00 20', TRUE);

INSERT INTO parent (id, prenom, nom) VALUES
                                         (194, 'Ahmed', 'Benali'),
                                         (195, 'Fatima', 'El Mansouri'),
                                         (196, 'Karim', 'Bensouda'),
                                         (197, 'Nadia', 'Chraibi'),
                                         (198, 'Omar', 'Tazi'),
                                         (199, 'Leila', 'Benhaddou'),
                                         (200, 'Amine', 'Alaoui'),
                                         (201, 'Salma', 'Idrissi'),
                                         (202, 'Ayoub', 'Berrada'),
                                         (203, 'Imane', 'Amrani'),
                                         (204, 'Mehdi', 'El Fassi'),
                                         (205, 'Ghita', 'Bennani'),
                                         (206, 'Zakaria', 'Ouazzani'),
                                         (207, 'Kawtar', 'Lahlou'),
                                         (208, 'Anas', 'Skalli'),
                                         (209, 'Meryem', 'Naciri'),
                                         (210, 'Ilyas', 'Cherkaoui'),
                                         (211, 'Aya', 'Berrada'),
                                         (212, 'Yassine', 'Tazi'),
                                         (213, 'Siham', 'El Idrissi');

INSERT INTO utilisateur (
    id, nom_complet, email, mot_de_passe, role, telephone, actif
)
SELECT
    eleve.id + 180,
    CONCAT(
            CASE MOD(eleve.id - 13, 12)
                WHEN 0 THEN 'Ahmed' WHEN 1 THEN 'Fatima' WHEN 2 THEN 'Karim'
                WHEN 3 THEN 'Nadia' WHEN 4 THEN 'Omar' WHEN 5 THEN 'Leila'
                WHEN 6 THEN 'Amine' WHEN 7 THEN 'Salma' WHEN 8 THEN 'Ayoub'
                WHEN 9 THEN 'Imane' WHEN 10 THEN 'Mehdi' ELSE 'Ghita'
                END,
            ' ', eleve.nom
    ),
    CONCAT('parent', eleve.id - 13, '@example.com'),
    '{noop}123456',
    'PARENT',
    CONCAT('+212 6 21 00 ', LPAD(eleve.id - 13, 2, '0')),
    TRUE
FROM eleve
WHERE eleve.id BETWEEN 34 AND 113;

INSERT INTO parent (id, prenom, nom)
SELECT
    eleve.id + 180,
    CASE MOD(eleve.id - 13, 12)
        WHEN 0 THEN 'Ahmed' WHEN 1 THEN 'Fatima' WHEN 2 THEN 'Karim'
        WHEN 3 THEN 'Nadia' WHEN 4 THEN 'Omar' WHEN 5 THEN 'Leila'
        WHEN 6 THEN 'Amine' WHEN 7 THEN 'Salma' WHEN 8 THEN 'Ayoub'
        WHEN 9 THEN 'Imane' WHEN 10 THEN 'Mehdi' ELSE 'Ghita'
        END,
    eleve.nom
FROM eleve
WHERE eleve.id BETWEEN 34 AND 113;

INSERT INTO devoir (
    titre, description, matiere_id, classe_id, enseignant_id, date_limite, statut
) VALUES
      ('Calcul numérique', 'Réviser les opérations et les priorités de calcul.', 1, 1, 2, DATE_SUB(CURDATE(), INTERVAL 8 DAY), 'CORRIGE'),
      ('Expression écrite', 'Rédiger un texte argumentatif de vingt lignes.', 2, 1, 3, CURDATE(), 'EN_CORRECTION'),
      ('Compréhension en arabe', 'Répondre aux questions du texte étudié.', 3, 1, 4, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'CORRIGE'),
      ('Endurance et vitesse', 'Évaluation pratique en course.', 10, 1, 11, DATE_ADD(CURDATE(), INTERVAL 12 DAY), 'A_VENIR'),
      ('Fractions et décimaux', 'Effectuer les exercices de la série quatre.', 1, 2, 2, CURDATE(), 'EN_CORRECTION'),
      ('Grammaire française', 'Réviser les compléments du verbe.', 2, 2, 3, CURDATE(), 'EN_CORRECTION'),
      ('Vocabulaire anglais', 'Préparer le vocabulaire de l’unité trois.', 4, 2, 5, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'CORRIGE'),
      ('Couleurs et formes', 'Créer une composition graphique simple.', 11, 2, 12, DATE_ADD(CURDATE(), INTERVAL 13 DAY), 'A_VENIR'),
      ('Géométrie plane', 'Construire les figures demandées.', 1, 3, 2, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 'A_VENIR'),
      ('Production écrite arabe', 'Rédiger un court récit en arabe.', 3, 3, 4, CURDATE(), 'EN_CORRECTION'),
      ('Matière et énergie', 'Répondre au questionnaire du chapitre.', 5, 3, 6, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'CORRIGE'),
      ('Problèmes mathématiques', 'Résoudre les quatre problèmes proposés.', 1, 3, 13, DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'A_VENIR'),
      ('Grammaire anglaise', 'Utiliser le présent accompli dans dix phrases.', 4, 4, 5, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 'A_VENIR'),
      ('Forces et mouvements', 'Exercices sur les forces et la vitesse.', 5, 4, 6, CURDATE(), 'EN_CORRECTION'),
      ('Cellule et respiration', 'Compléter le schéma de la cellule.', 6, 4, 7, DATE_SUB(CURDATE(), INTERVAL 8 DAY), 'CORRIGE'),
      ('Sports collectifs', 'Évaluation des règles et de la pratique.', 10, 4, 11, DATE_ADD(CURDATE(), INTERVAL 11 DAY), 'A_VENIR'),
      ('Compréhension orale en anglais', 'Écouter le dialogue et répondre aux questions.', 4, 5, 5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'A_VENIR'),
      ('Électricité', 'Préparer le montage électrique simple.', 5, 5, 6, CURDATE(), 'EN_CORRECTION'),
      ('Initiation à l’algorithmique', 'Créer un algorithme avec des conditions.', 7, 5, 8, DATE_SUB(CURDATE(), INTERVAL 9 DAY), 'CORRIGE'),
      ('Perspective', 'Dessiner une scène en perspective.', 11, 5, 12, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'A_VENIR'),
      ('Présentation orale en anglais', 'Présenter un sujet pendant trois minutes.', 4, 6, 5, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 'A_VENIR'),
      ('Écosystèmes', 'Comparer deux écosystèmes marocains.', 6, 6, 7, CURDATE(), 'EN_CORRECTION'),
      ('Le Maroc contemporain', 'Préparer une synthèse du chapitre.', 8, 6, 9, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'CORRIGE'),
      ('Calcul littéral', 'Simplifier les expressions proposées.', 1, 6, 13, DATE_ADD(CURDATE(), INTERVAL 16 DAY), 'A_VENIR'),
      ('Tableur et données', 'Créer un tableau avec des formules simples.', 7, 7, 8, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'A_VENIR'),
      ('Histoire du Maroc', 'Construire une frise chronologique.', 8, 7, 9, CURDATE(), 'EN_CORRECTION'),
      ('Valeurs et citoyenneté', 'Préparer une synthèse du cours.', 9, 7, 10, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'CORRIGE'),
      ('Athlétisme', 'Évaluation pratique du saut en longueur.', 10, 7, 11, DATE_ADD(CURDATE(), INTERVAL 12 DAY), 'A_VENIR'),
      ('Programmation simple', 'Écrire un programme avec une boucle.', 7, 8, 8, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 'A_VENIR'),
      ('Géographie du Maroc', 'Analyser une carte climatique.', 8, 8, 9, CURDATE(), 'EN_CORRECTION'),
      ('Lecture française', 'Présenter le livre étudié en classe.', 2, 8, 3, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'CORRIGE'),
      ('Arts visuels', 'Créer une affiche sur le patrimoine.', 11, 8, 12, DATE_ADD(CURDATE(), INTERVAL 13 DAY), 'A_VENIR'),
      ('Sécurité informatique', 'Répondre au questionnaire sur les mots de passe.', 7, 9, 8, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 'A_VENIR'),
      ('Éducation islamique', 'Mémoriser et expliquer le texte étudié.', 9, 9, 10, CURDATE(), 'EN_CORRECTION'),
      ('Expression orale arabe', 'Préparer un exposé de cinq minutes.', 3, 9, 4, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'CORRIGE'),
      ('Équations', 'Résoudre la série d’équations.', 1, 9, 13, DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'A_VENIR');

INSERT INTO note (
    eleve_id, devoir_id, enseignant_id, valeur, valeur_maximale, date, commentaire
)
SELECT
    eleve.id,
    devoir.id,
    devoir.enseignant_id,
    10 + MOD((eleve.id - 13) + devoir.id, 10),
    20,
    CURDATE(),
    CASE MOD(eleve.id - 13, 3)
        WHEN 0 THEN 'Très bon travail, continue ainsi.'
        WHEN 1 THEN 'Résultat satisfaisant, révise encore les points difficiles.'
        ELSE 'Bon effort, améliore la présentation.'
        END
FROM eleve
         JOIN devoir ON devoir.classe_id = eleve.classe_id
WHERE devoir.statut = 'CORRIGE';

INSERT INTO presence (
    eleve_id, classe_id, matiere_id, enseignant_id, date, statut
)
SELECT
    eleve.id,
    eleve.classe_id,
    enseignant.matiere_id,
    classe.enseignant_principal_id,
    CURDATE(),
    CASE MOD(eleve.id - 14, 20)
        WHEN 0 THEN 'ABSENT'
        WHEN 1 THEN 'EN_RETARD'
        ELSE 'PRESENT'
        END
FROM eleve
         JOIN classe_scolaire classe ON classe.id = eleve.classe_id
         JOIN enseignant ON enseignant.id = classe.enseignant_principal_id;

INSERT INTO presence (
    eleve_id, classe_id, matiere_id, enseignant_id, date, statut
)
SELECT
    eleve.id,
    eleve.classe_id,
    enseignant.matiere_id,
    classe.enseignant_principal_id,
    DATE_SUB(CURDATE(), INTERVAL 1 DAY),
    CASE MOD(eleve.id - 13, 15)
        WHEN 0 THEN 'ABSENT'
        WHEN 1 THEN 'EN_RETARD'
        ELSE 'PRESENT'
        END
FROM eleve
         JOIN classe_scolaire classe ON classe.id = eleve.classe_id
         JOIN enseignant ON enseignant.id = classe.enseignant_principal_id;

INSERT INTO paiement (
    eleve_id, parent_id, montant, methode, date, statut, url_justificatif
)
SELECT
    eleve.id,
    eleve.parent_id,
    1500,
    CASE MOD(eleve.id - 13, 3)
        WHEN 0 THEN 'Virement bancaire'
        WHEN 1 THEN 'Carte bancaire'
        ELSE 'Espèces'
        END,
    CASE MOD((eleve.id - 13) - 1, 3)
        WHEN 0 THEN CURDATE()
        WHEN 1 THEN DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
        ELSE DATE_SUB(CURDATE(), INTERVAL 2 MONTH)
        END,
    CASE MOD(eleve.id - 13, 3)
        WHEN 0 THEN 'EN_ATTENTE'
        WHEN 1 THEN 'VALIDE'
        ELSE 'REFUSE'
        END,
    CONCAT('/justificatifs/paiement-00', MOD((eleve.id - 13) - 1, 3) + 1, '.txt')
FROM eleve
WHERE eleve.id BETWEEN 14 AND 73;

INSERT INTO horaire_emploi_du_temp (
    jour, heure_debut, matiere_id, classe_id, enseignant_id, salle
)
SELECT
    jours.jour,
    horaires.heure_debut,
    enseignant.matiere_id,
    affectation.classe_id,
    affectation.enseignant_id,
    CONCAT('Salle ', affectation.classe_id, '0', horaires.numero)
FROM (
         SELECT
             classe_enseignant.*,
             ROW_NUMBER() OVER (PARTITION BY classe_id ORDER BY id) AS rang
         FROM classe_enseignant
     ) affectation
         JOIN enseignant ON enseignant.id = affectation.enseignant_id
         CROSS JOIN (
    SELECT 'Lundi' jour UNION ALL SELECT 'Mardi' UNION ALL SELECT 'Mercredi'
    UNION ALL SELECT 'Jeudi' UNION ALL SELECT 'Vendredi' UNION ALL SELECT 'Samedi'
) jours
         JOIN (
    SELECT 1 numero, '08:00:00' heure_debut
    UNION ALL SELECT 2, '10:00:00'
    UNION ALL SELECT 3, '14:00:00'
    UNION ALL SELECT 4, '16:00:00'
) horaires ON horaires.numero = affectation.rang;

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
) VALUES
      (1, 'Justificatif de paiement', 'Un parent a envoyé un justificatif de paiement.', 'PAIEMENT', NOW(), FALSE, 1),
      (1, 'Absence signalée', 'Un élève de 1AC-1 est absent aujourd’hui.', 'PRESENCE', NOW(), FALSE, 1),
      (1, 'Retard signalé', 'Un élève de 1AC-1 est arrivé en retard.', 'PRESENCE', NOW(), TRUE, 2),
      (1, 'État du collège', 'Les données scolaires ont été mises à jour.', 'ANNONCE', NOW(), FALSE, NULL);

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Devoir en correction',
       'Un devoir de votre matière est maintenant en correction.',
       'DEVOIR', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'ENSEIGNANT';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Annonce administrative',
       'Une réunion pédagogique est prévue vendredi à 16 h.',
       'ANNONCE', NOW(), FALSE, NULL
FROM utilisateur
WHERE role = 'ENSEIGNANT';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Nouveau devoir',
       'Un nouveau devoir a été ajouté pour votre classe.',
       'DEVOIR', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'ELEVE';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Devoir corrigé', 'Un devoir corrigé est disponible.',
       'DEVOIR', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'ELEVE';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Nouvelle note', 'Une nouvelle note a été ajoutée.',
       'NOTE', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'ELEVE';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Suivi de présence',
       'Un de vos enfants a une absence ou un retard récent.',
       'PRESENCE', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'PARENT';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Nouvelle note',
       'Une nouvelle note est disponible pour un de vos enfants.',
       'NOTE', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'PARENT';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Paiement en attente',
       'Un paiement scolaire est en attente de validation.',
       'PAIEMENT', NOW(), FALSE, id
FROM utilisateur
WHERE role = 'PARENT';

INSERT INTO notification (
    destinataire_id, titre, message, type, date_creation, lue, entite_liee_id
)
SELECT id, 'Annonce administrative',
       'Consultez les nouvelles informations communiquées par l’administration.',
       'ANNONCE', NOW(), FALSE, NULL
FROM utilisateur
WHERE role = 'PARENT';


UPDATE paiement
SET methode = 'Virement bancaire'
WHERE methode NOT IN ('Espèces', 'Virement bancaire');