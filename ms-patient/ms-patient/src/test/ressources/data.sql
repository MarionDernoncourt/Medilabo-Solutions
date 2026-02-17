-- Désactive l'intégrité référentielle pour le TRUNCATE (équivalent H2 de FOREIGN_KEY_CHECKS)
SET REFERENTIAL_INTEGRITY FALSE;

-- Vide la table et remet l'auto-incrément à 1
TRUNCATE TABLE patient;

-- Réactive l'intégrité
SET REFERENTIAL_INTEGRITY TRUE;

-- Tes 4 patients de test (vérifie que les noms de colonnes correspondent à ton entité)
INSERT INTO patient (first_name, last_name, birth_date, gender, address, phone_number)
VALUES ('TestNone', 'Test', '1966-12-31', 'F', '1 Variable Code', '100-222-3333');

INSERT INTO patient (first_name, last_name, birth_date, gender, address, phone_number)
VALUES ('TestBorderline', 'Test', '1945-06-24', 'M', '2 High Hills', '200-333-4444');

INSERT INTO patient (first_name, last_name, birth_date, gender, address, phone_number)
VALUES ('TestInDanger', 'Test', '2004-06-18', 'M', '3 Short Lane', '300-444-5555');

INSERT INTO patient (first_name, last_name, birth_date, gender, address, phone_number)
VALUES ('TestEarlyOnset', 'Test', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');