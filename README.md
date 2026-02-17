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

Note sur le démarrage : Le projet utilise des Docker Healthchecks. Le microservice ms-patient attendra que la base MySQL soit totalement opérationnelle avant de démarrer pour garantir une connexion stable.

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
- MySQL (ms-patient) : La base de données est peuplée via le script data.sql . La base de données Patient respecte la 3e Forme Normale (3NF) pour optimiser le stockage et garantir l'intégrité des données.
- MongoDB (ms-notes): La base de données est peuplée via le DataInitializer.
  
- ### 🗄️ Gestion de la persistance (MySQL)
L'application est configurée pour être opérationnelle dès le premier lancement (**Mode Démo**) :

**MySQL** (ms-patient) :

- `spring.jpa.hibernate.ddl-auto=create` : Les tables sont recréées à chaque démarrage.

- **Synchronisation des IDs** : Le script `data.sql` exécute un `TRUNCATE TABLE` (avec désactivation des `FOREIGN_KEY_CHECKS`). Cela garantit que les IDs patients repartent de *1*, assurant la cohérence avec les références stockées dans MongoDB.
Normalisation : La base respecte la 3NF (3ème Forme Normale) pour minimiser la redondance.

**MongoDB** (ms-notes) : Les notes cliniques sont injectées via un DataInitializer Java qui vérifie l'absence de données avant d'insérer le jeu de test.

**Note pour la Production** : Pour conserver les données, modifiez ddl-auto=update et passez `spring.sql.init.mode=never`. Commentez également les instructions `TRUNCATE` dans le script SQL.
**Note pour la Production** : Pour désactiver l'injection automatique en production, vous pouvez soit commenter l'annotation `@Component` (ou @Bean) du `DataInitializer`, soit utiliser un **Profile Spring** (ex: `@Profile("!prod")`) pour ne le charger qu'en environnement de développement.

## Securité et communication inter-services
- **API Gateway** : Seul point d'entrée public de l'application. Les microservices ne sont pas exposés directement.
- **Filtrage interne** : Un mécanisme de filtrage basé sur un **Header-Secret**(`X-Internal-Secret`) est implémenté. Toute requête directe vers un microservice qui ne provient pas de la Gateway ou d'un service autorisé est rejetée.
- **Service Discovery**: Utilisation d'**Eureka** pour une résolution de noms dynamique, évitant les adresses IP codées en dur.
- **Communication inter-services(OpenFeign**): Le projet utilise **SpringCloud OpenFeign** pour les appels entre microservices. Cela permet une communication **déclarative** (via des interfaces) rendans le code plus lisible et plus facile a maintenir qu'avec un `ResTemplate` classique. Couplé a **Eureka**, OpenFeign utilise le nom des services pour résoudre les adresses automatiquement.
  
## Tests et Qualité
- **Exécution locale** : Pour lancer les tests, utilisez la commande `mvn test`.
- **Intégration continue (CI)** : Le projet utilise GitHub Actions. Les tests sont automatiquement exécutés à chaque push sur la branche `main` pour garantir la stabilité et la non-régression du code.
