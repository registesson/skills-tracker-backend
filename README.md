# Skills Tracker Backend

API REST pour la gestion des compétences et sessions d'apprentissage.

## 📋 Description

Application Spring Boot permettant de :
- Gérer un portfolio de compétences
- Suivre les sessions d'apprentissage
- Exporter les données en format CSV
- Authentification sécurisée par JWT

## 🚀 Technologies

- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Security** (authentification JWT)
- **Spring Data JPA** (persistence)
- **PostgreSQL** (base de données production)
- **H2** (base de données dev/test)
- **SpringDoc OpenAPI 2.8.5** (documentation API)
- **Lombok** (réduction du code boilerplate)
- **Maven** (gestion de dépendances)

## 📦 Prérequis

- Java 21 ou supérieur
- Maven 3.9+
- PostgreSQL 15+ (pour production)

## 🔧 Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd skills-tracker-backend
```

### 2. Configuration de la base de données

#### PostgreSQL (Production)

```bash
# Créer la base de données
createdb skillstracker

# Créer l'utilisateur (si nécessaire)
psql -c "CREATE USER user WITH PASSWORD 'password';"
psql -c "GRANT ALL PRIVILEGES ON DATABASE skillstracker TO user;"
```

### 3. Configuration de l'application

Copier et adapter le fichier de configuration :

```bash
cp src/main/resources/application.properties src/main/resources/application-prod.properties
```

**⚠️ Important pour la production :** Modifier les valeurs suivantes dans `application-prod.properties` :

```properties
# Database (utiliser vos vraies informations)
spring.datasource.url=jdbc:postgresql://your-db-host:5432/skillstracker
spring.datasource.username=your-db-user
spring.datasource.password=your-secure-password

# JWT (générer une nouvelle clé secrète)
jwt.secret=YOUR_PRODUCTION_JWT_SECRET_KEY
jwt.expiration=86400000

# Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Logging
logging.level.com.skillstracker=INFO
logging.level.org.springframework.security=WARN

# CORS (adapter selon votre domaine frontend)
cors.allowed.origins=https://your-production-domain.com
```

### 4. Génération d'une clé JWT sécurisée

```bash
# Linux/macOS
openssl rand -base64 64

# Ou avec Java
java -cp target/skills-tracker-backend-0.0.1-SNAPSHOT.jar \
  -Dloader.main=com.skillstracker.JwtKeyGenerator \
  org.springframework.boot.loader.launch.PropertiesLauncher
```

## 🏃 Démarrage

### Développement

```bash
# Compiler et lancer les tests
mvn clean test

# Démarrer l'application en mode développement
mvn spring-boot:run
```

L'application sera accessible sur : `http://localhost:8081`

### Production

```bash
# Compiler le JAR
mvn clean package -DskipTests

# Lancer avec le profil production
java -jar target/skills-tracker-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 📚 Documentation API

Une fois l'application démarrée, la documentation Swagger est accessible à :

- **Swagger UI** : http://localhost:8081/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8081/v3/api-docs

### Endpoints principaux

#### Authentification
- `POST /api/auth/register` - Créer un compte
- `POST /api/auth/login` - Se connecter

#### Compétences
- `GET /api/skills` - Lister toutes les compétences
- `POST /api/skills` - Créer une compétence
- `GET /api/skills/{id}` - Détails d'une compétence
- `PUT /api/skills/{id}` - Modifier une compétence
- `DELETE /api/skills/{id}` - Supprimer une compétence
- `GET /api/skills/export/csv` - Exporter en CSV

#### Sessions d'apprentissage
- `GET /api/learning-sessions` - Lister les sessions
- `POST /api/learning-sessions` - Créer une session
- `GET /api/learning-sessions/{id}` - Détails d'une session
- `PUT /api/learning-sessions/{id}` - Modifier une session
- `DELETE /api/learning-sessions/{id}` - Supprimer une session

## 🔐 Authentification

L'API utilise JWT (JSON Web Token) pour l'authentification.

### 1. Créer un compte

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 2. Se connecter

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123"
  }'
```

### 3. Utiliser le token

```bash
curl -X GET http://localhost:8081/api/skills \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🧪 Tests

```bash
# Tous les tests
mvn test

# Tests spécifiques
mvn test -Dtest=SkillExportServiceTest
mvn test -Dtest=SkillServiceTest

# Avec coverage
mvn test jacoco:report
```

## 📊 Export CSV

Documentation complète : [README_EXPORT_CSV.md](./README_EXPORT_CSV.md)

Fonctionnalité d'export des compétences au format CSV :
- Format standard RFC 4180
- Colonnes : nom, catégorie, niveau, temps_total_minutes
- Téléchargement direct
- Authentification requise

```bash
curl -X GET http://localhost:8081/api/skills/export/csv \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -o mes-competences.csv
```

## 🐳 Docker (À venir)

```bash
# Build
docker build -t skills-tracker-backend .

# Run
docker-compose up -d
```

## 🔒 Sécurité

### Checklist de sécurité pour la production

- [ ] Changer la clé JWT secrète
- [ ] Utiliser HTTPS uniquement
- [ ] Configurer CORS correctement
- [ ] Désactiver `spring.jpa.show-sql`
- [ ] Utiliser `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Utiliser des mots de passe forts pour la base de données
- [ ] Activer les logs de sécurité
- [ ] Configurer les rate limits (à implémenter)
- [ ] Utiliser des variables d'environnement pour les secrets
- [ ] Mettre à jour régulièrement les dépendances

### Variables d'environnement recommandées

```bash
export DB_URL=jdbc:postgresql://localhost:5432/skillstracker
export DB_USERNAME=your_user
export DB_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret
export JWT_EXPIRATION=86400000
export CORS_ALLOWED_ORIGINS=https://your-domain.com
```

Utilisation dans `application-prod.properties` :

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
```

## 📁 Structure du projet

```
src/
├── main/
│   ├── java/com/skillstracker/
│   │   ├── application/          # Services métier
│   │   │   ├── auth/            # Gestion JWT
│   │   │   ├── learningSession/ # Sessions d'apprentissage
│   │   │   ├── skill/           # Gestion des compétences
│   │   │   └── user/            # Gestion des utilisateurs
│   │   ├── config/              # Configuration Spring
│   │   ├── domain/              # Entités métier
│   │   │   ├── LearningSession/
│   │   │   ├── skill/
│   │   │   └── user/
│   │   └── infrastructure/      # Couche infrastructure
│   │       ├── persistence/     # Repositories
│   │       └── web/            # Controllers & DTOs
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/skillstracker/
        ├── application/
        └── infrastructure/
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📝 License

Ce projet est sous licence MIT.

## 👥 Auteurs

Skills Tracker Team

## 📞 Support

Pour toute question : support@skillstracker.com

## 🗺️ Roadmap

- [x] Authentification JWT
- [x] CRUD Compétences
- [x] CRUD Sessions d'apprentissage
- [x] Export CSV
- [ ] Import CSV
- [ ] Statistiques et dashboards
- [ ] Notifications par email
- [ ] API GraphQL
- [ ] Gestion des équipes
- [ ] Objectifs d'apprentissage

