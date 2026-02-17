-- ATTENTION : La ligne suivante est utilisée UNIQUEMENT pour le développement
-- afin de garantir un jeu de données propre à chaque démarrage.
-- À supprimer/commenter en environnement de production pour préserver la persistance.
-- Desactivation de la sécurité
SET FOREIGN_KEY_CHECKS = 0;

-- On vide la table et on remet l'ID à 1
TRUNCATE TABLE patient;

-- On réactive la sécurité
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO patient (last_name, first_name, birthdate, genre, address, phone_number) VALUES
                                                                                         ('TestNone', 'Test', '1966-12-31', 'FEMININ', '1 Brookside St', '100-222-3333'),
                                                                                         ('TestBorderline', 'Test', '1945-06-24', 'MASCULIN', '2 High St', '200-333-4444'),
                                                                                         ('TestInDanger', 'Test', '2004-06-18', 'MASCULIN', '3 Club Road', '300-444-5555'),
                                                                                         ('TestEarlyOnset', 'Test', '2002-06-28', 'FEMININ', '4 Valley Dr', '400-555-6666');