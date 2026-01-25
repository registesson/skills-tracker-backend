# ⚡ Commandes Rapides - Skills Tracker Backend

## 🛠️ Développement

### Démarrer l'application
```bash
mvn spring-boot:run
```

### Compiler
```bash
mvn clean compile
```

### Tests
```bash
# Tous les tests
mvn test

# Test spécifique
mvn test -Dtest=SkillExportServiceTest

# Avec couverture
mvn test jacoco:report
```

### Build
```bash
mvn clean package
```

---

## 🐳 Docker

### Build et démarrer
```bash
docker-compose up -d
```

### Voir les logs
```bash
docker-compose logs -f backend
```

### Arrêter
```bash
docker-compose down
```

### Rebuild
```bash
docker-compose build --no-cache
docker-compose up -d
```

### Shell dans le conteneur
```bash
docker exec -it skillstracker-backend sh
```

---

## 🔍 Vérifications

### Health check
```bash
curl http://localhost:8080/actuator/health
```

### Métriques
```bash
curl http://localhost:8080/actuator/metrics
```

### Test API

#### Register
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123456",
    "firstName": "Test",
    "lastName": "User"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123456"
  }'
```

#### Get Skills (avec token)
```bash
TOKEN="votre_token_jwt"
curl -X GET http://localhost:8081/api/skills \
  -H "Authorization: Bearer $TOKEN"
```

#### Export CSV
```bash
TOKEN="votre_token_jwt"
curl -X GET http://localhost:8081/api/skills/export/csv \
  -H "Authorization: Bearer $TOKEN" \
  -o mes-competences.csv
```

---

## 🗄️ Base de Données

### PostgreSQL local

#### Créer la base
```bash
createdb skillstracker
```

#### Se connecter
```bash
psql -d skillstracker
```

#### Backup
```bash
pg_dump skillstracker > backup_$(date +%Y%m%d).sql
```

#### Restore
```bash
psql skillstracker < backup_20260125.sql
```

### Docker PostgreSQL

#### Se connecter au conteneur
```bash
docker exec -it skillstracker-db psql -U skilltracker_user -d skillstracker
```

#### Backup depuis Docker
```bash
docker exec skillstracker-db pg_dump -U skilltracker_user skillstracker > backup.sql
```

---

## 🔐 Sécurité

### Générer clé JWT
```bash
openssl rand -base64 64
```

### Variables d'environnement
```bash
export DB_URL=jdbc:postgresql://localhost:5432/skillstracker
export DB_USERNAME=skilltracker_user
export DB_PASSWORD=your_password
export JWT_SECRET=$(openssl rand -base64 64)
export CORS_ALLOWED_ORIGINS=https://your-domain.com
```

---

## 📊 Logs

### Voir les logs (dev)
```bash
tail -f logs/application.log
```

### Logs systemd (production)
```bash
sudo journalctl -u skillstracker -f
```

### Logs Docker
```bash
docker-compose logs -f
docker-compose logs -f backend
docker-compose logs -f postgres
```

---

## 🚀 Déploiement

### Script automatique
```bash
# Développement
./deploy.sh dev

# Staging
./deploy.sh staging

# Production
./deploy.sh prod
```

### Manuel avec JAR
```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/skills-tracker-backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

### Manuel avec Docker
```bash
# Build l'image
docker build -t skillstracker-backend .

# Run
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://host:5432/db \
  -e JWT_SECRET=your_secret \
  --name skillstracker-backend \
  skillstracker-backend
```

---

## 🔄 Mise à jour

### Git pull et rebuild
```bash
git pull
mvn clean package -DskipTests
docker-compose build
docker-compose up -d
```

### Rollback Docker
```bash
docker-compose down
docker-compose up -d skillstracker-backend:previous-tag
```

---

## 🧹 Nettoyage

### Maven
```bash
mvn clean
```

### Docker
```bash
# Arrêter et supprimer les conteneurs
docker-compose down

# Supprimer les volumes
docker-compose down -v

# Nettoyer les images inutilisées
docker system prune -a
```

### Logs
```bash
rm -rf logs/*.log
```

---

## 🔧 Dépannage

### Port déjà utilisé
```bash
# Trouver le processus
lsof -i :8080

# Tuer le processus
kill -9 <PID>

# Ou changer le port
export PORT=8081
```

### Problème de connexion DB
```bash
# Vérifier que PostgreSQL tourne
pg_isready

# Vérifier les connexions
psql -h localhost -U user -d skillstracker
```

### Problème Docker
```bash
# Reconstruire tout
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d

# Voir les logs détaillés
docker-compose logs --tail=100 backend
```

### Reset complet
```bash
# Arrêter tout
docker-compose down -v

# Nettoyer Maven
mvn clean

# Supprimer les logs
rm -rf logs/

# Rebuild depuis zéro
mvn clean package
docker-compose up -d
```

---

## 📚 Documentation

### Swagger UI (dev)
http://localhost:8081/swagger-ui.html

### OpenAPI JSON
http://localhost:8081/v3/api-docs

### Actuator Endpoints
- http://localhost:8080/actuator/health
- http://localhost:8080/actuator/info
- http://localhost:8080/actuator/metrics

---

## 💡 Astuces

### Watch mode (recompilation auto)
```bash
mvn spring-boot:run -Dspring-boot.run.fork=false
```

### Debug mode
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### Profils Spring
```bash
# Dev (H2 in-memory)
mvn spring-boot:run

# Production (PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Variables d'environnement rapides
```bash
# Créer un fichier .env.local
cat > .env.local << EOF
export DB_URL=jdbc:postgresql://localhost:5432/skillstracker
export DB_USERNAME=user
export DB_PASSWORD=password
export JWT_SECRET=$(openssl rand -base64 64)
EOF

# Sourcer
source .env.local
```

---

## 🎯 Raccourcis Utiles

```bash
# Alias à ajouter dans ~/.zshrc ou ~/.bashrc
alias st-dev='cd /path/to/skills-tracker-backend && mvn spring-boot:run'
alias st-test='cd /path/to/skills-tracker-backend && mvn test'
alias st-build='cd /path/to/skills-tracker-backend && mvn clean package'
alias st-docker='cd /path/to/skills-tracker-backend && docker-compose up -d'
alias st-logs='cd /path/to/skills-tracker-backend && docker-compose logs -f backend'
```

