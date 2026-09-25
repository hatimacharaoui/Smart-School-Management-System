-- Le mot de passe commun est 12345
INSERT INTO utilisateur (
    id, nom_complet, email, mot_de_passe, role, telephone, actif
) VALUES
      (1, 'Mohammed Alaoui', 'admin@smartschool.com',
       '{bcrypt}$2b$12$mu5kzuD6cZwNgul7uFVvk.hSNMCb6In9gsuoZV0i7NyQgKiYQ52K.',
       'ADMINISTRATEUR', '+212 6 12 34 56 78', TRUE),
      (2, 'Hatim Acharaoui', 'hatim.acharaoui@smartschool.ma',
       '{bcrypt}$2b$12$mu5kzuD6cZwNgul7uFVvk.hSNMCb6In9gsuoZV0i7NyQgKiYQ52K.',
       'ENSEIGNANT', '+212 6 10 10 10 01', TRUE),
      (14, 'Adam Acharaoui', 'adam.acharaoui@smartschool.ma',
       '{bcrypt}$2b$12$mu5kzuD6cZwNgul7uFVvk.hSNMCb6In9gsuoZV0i7NyQgKiYQ52K.',
       'ELEVE', '+212 6 30 01 01 00', TRUE),
      (194, 'Ahmed Acharaoui', 'ahmed.acharaoui@smartschool.com',
       '{bcrypt}$2b$12$mu5kzuD6cZwNgul7uFVvk.hSNMCb6In9gsuoZV0i7NyQgKiYQ52K.',
       'PARENT', '+212 6 20 00 00 01', TRUE);

INSERT INTO administrateur (id) VALUES (1);


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
      (2, 'Devoir en correction',
       'Un devoir de votre matière est maintenant en correction.',
       'DEVOIR', NOW(), FALSE, 2),
      (2, 'Annonce administrative',
       'Une réunion pédagogique est prévue vendredi à 16 h.',
       'ANNONCE', NOW(), FALSE, NULL),
      (14, 'Nouveau devoir',
       'Un nouveau devoir a été ajouté pour votre classe.',
       'DEVOIR', NOW(), FALSE, 14),
      (14, 'Devoir corrigé',
       'Un devoir corrigé est disponible.',
       'DEVOIR', NOW(), FALSE, 14),
      (14, 'Nouvelle note',
       'Une nouvelle note a été ajoutée.',
       'NOTE', NOW(), FALSE, 14),
      (194, 'Suivi de présence',
       'Un de vos enfants a une absence ou un retard récent.',
       'PRESENCE', NOW(), FALSE, 194),
      (194, 'Nouvelle note',
       'Une nouvelle note est disponible pour un de vos enfants.',
       'NOTE', NOW(), FALSE, 194),
      (194, 'Paiement en attente',
       'Un paiement scolaire est en attente de validation.',
       'PAIEMENT', NOW(), FALSE, 194),
      (194, 'Annonce administrative',
       'Consultez les nouvelles informations communiquées par l’administration.',
       'ANNONCE', NOW(), FALSE, NULL);
