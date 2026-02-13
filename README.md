# MEDILABO SOLUTIONS - Système de gestion du risque diabète
Le projet est une application de santé composée de microservices permettant de gérer les fiches patient, de suivre leurs notes cliniques et de calculer leur niveau de risque de diabète en fonction de déclencheurs spécifiques.

## Technologies utilisées
- Backend : Java 21, Spring Boot 3.x
- Frontend : Vue.js 3
- Architecture : Microservices avec Spring Cloud (Gateway, Eureka, OpenFeign)
- Bases de données : MySQL (gestion des informations patient) et MongoDB (gestion des notes cliniques)
- DevOps : Docker, Docker Compose, GitHub Actions

## Architecture du système
L'application est découpée en 6 services principaux :
- eureka-server : Service Discovery - Port 8761
- ms-gateway : Point d'entrée unique de l'application - Port 8088
- ms-patient : Gestion des données des patients - Port 9001
- ms-notes : Gestion des données concernant les notes cliniques - Port 9002
- ms-reports : Service de calcul du risque de diabète - Port 9003
- medilabo-ui : Interface utilisateur - Port 8080

## Installation et Lancement
Pré-requis
- Docker et Docker Compose
- Java 21 et Maven (pour la compilation)

### Backend (Spring Boot)
1. Configuration : Après avoir cloné le projet, créez le fichier .env à la racine du projet (un modèle est fourni dans le fichier .env.example).
Modifiez les variables selon votre mode d'exécution :
### ⚙️ Configuration de l'hôte (`.env`)
| Variable | Local (Sans Docker) | Docker |
| :--- | :--- | :--- |
| `DB_HOST` | `localhost` | `mysql-db` |
| `MONGO_HOST` | `localhost` | `mongo-db` |
| `EUREKA_HOST` | `localhost` | `eureka-server` |

Note IntelliJ : Pour charger ces variables sans Docker, il est recommandé d'utiliser le plugin EnvFile et de l'activer dans la configuration de lancement de chaque microservice en pointant vers ce fichier `.env`

2.Compilation : Générez les fichiers exécutables pour chaque microservice à l'aide de la commande suivante : `mvn clean package -DskipTests`

### Frontend (Vue.js)
Dépendances : Installez les packages nécessaires au fonctionnement de l'interface : `npm install`

### Déploiement via Docker
Une fois le backend compilé et le frontend préparé, lancez l'infrastructure complète avec la commande : `docker-compose up --build`

## Initialisation des données
L'application est configurée pour être opérationnelle dès le premier lancement grâce à un système d'initialisation automatique :
- MySQL (ms-patient) : La base de données est peuplée via le script data.sql.
Note technique : **Afin de garantir un jeu de données intègre à chaque démarrage en environnement de développement, une commande `DELETE` est exécutée avant l'insertion. Cette commande doit être retirée ou commentée pour un passage en production afin de garantir la persistance des données.**
- MongoDB (ms-notes) : Les notes cliniques sont injectées par le DataInitializer Java. Celui-ci vérifie si la base de données est vide avant de générer les notes de test.

## Tests et Qualité
- Exécution locale : Pour lancer les tests, utilisez la commande mvn test.
- Intégration continue (CI) : Le projet utilise GitHub Actions. Les tests sont automatiquement exécutés à chaque push sur la branche main pour garantir la stabilité et la non-régression du code.
