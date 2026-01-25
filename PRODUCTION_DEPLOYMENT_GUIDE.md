# 🔒 Guide de Déploiement en Production - Skills Tracker Backend

## ✅ Checklist Pré-Déploiement

### 1. Configuration Sécurité

- [ ] **Générer une nouvelle clé JWT** (ne jamais utiliser celle du dépôt)
  ```bash
  openssl rand -base64 64
  ```
  
- [ ] **Configurer les variables d'environnement** (ne pas utiliser les valeurs par défaut)
  - `DB_URL`
  - `DB_USERNAME`
  - `DB_PASSWORD`
  - `JWT_SECRET`
  - `CORS_ALLOWED_ORIGINS`

- [ ] **Désactiver Swagger en production**
  ```properties
  springdoc.api-docs.enabled=false
  springdoc.swagger-ui.enabled=false
  ```

- [ ] **Configurer CORS correctement** (uniquement les domaines autorisés)

- [ ] **Désactiver les logs SQL**
  ```properties
  spring.jpa.show-sql=false
  ```

- [ ] **Utiliser `ddl-auto=validate`** au lieu de `update`
  ```properties
  spring.jpa.hibernate.ddl-auto=validate
  ```

### 2. Base de Données

- [ ] **Créer la base de données PostgreSQL**
- [ ] **Créer un utilisateur dédié avec des droits limités**
- [ ] **Tester la connexion**
- [ ] **Configurer les sauvegardes automatiques**
- [ ] **Créer les index nécessaires pour les performances**

### 3. Tests

- [ ] **Tous les tests unitaires passent**
  ```bash
  mvn clean test
  ```

- [ ] **Build réussi**
  ```bash
  mvn clean package
  ```

- [ ] **Tests d'intégration OK**

### 4. Documentation

- [ ] **README.md complet**
- [ ] **Documentation API à jour** (Swagger/OpenAPI)
- [ ] **Instructions de déploiement claires**

### 5. Monitoring

- [ ] **Activer Spring Actuator** (health, metrics)
- [ ] **Configurer les logs** (fichiers, rotation)
- [ ] **Configurer les alertes** (optionnel)

---

## 🚀 Déploiement

### Option 1 : Déploiement JAR

#### 1. Compiler l'application

```bash
mvn clean package -DskipTests
```

#### 2. Copier le JAR sur le serveur

```bash
scp target/skills-tracker-backend-0.0.1-SNAPSHOT.jar user@server:/opt/skillstracker/
```

#### 3. Créer un fichier de configuration production

Sur le serveur, créer `/opt/skillstracker/application-prod.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skillstracker
spring.datasource.username=skilltracker_user
spring.datasource.password=STRONG_PASSWORD_HERE
jwt.secret=YOUR_GENERATED_JWT_SECRET
cors.allowed.origins=https://your-domain.com
```

#### 4. Lancer l'application

```bash
java -jar skills-tracker-backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.config.location=file:./application-prod.properties
```

#### 5. Créer un service systemd (recommandé)

Créer `/etc/systemd/system/skillstracker.service` :

```ini
[Unit]
Description=Skills Tracker Backend
After=network.target postgresql.service

[Service]
Type=simple
User=skillstracker
WorkingDirectory=/opt/skillstracker
ExecStart=/usr/bin/java \
  -Xmx512m -Xms256m \
  -jar /opt/skillstracker/skills-tracker-backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.config.location=file:/opt/skillstracker/application-prod.properties
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=skillstracker

[Install]
WantedBy=multi-user.target
```

Activer et démarrer :

```bash
sudo systemctl daemon-reload
sudo systemctl enable skillstracker
sudo systemctl start skillstracker
sudo systemctl status skillstracker
```

---

### Option 2 : Déploiement Docker

#### 1. Préparer les variables d'environnement

Copier `.env.example` vers `.env` et éditer :

```bash
cp .env.example .env
nano .env
```

#### 2. Build et lancer

```bash
# Build l'image
docker-compose build

# Lancer les conteneurs
docker-compose up -d

# Vérifier les logs
docker-compose logs -f backend

# Vérifier la santé
docker-compose ps
```

#### 3. Arrêter

```bash
docker-compose down
```

#### 4. Mettre à jour

```bash
# Pull le nouveau code
git pull

# Rebuild
docker-compose build

# Restart
docker-compose up -d
```

---

### Option 3 : Déploiement Cloud

#### Heroku

```bash
# Login
heroku login

# Créer l'app
heroku create skillstracker-backend

# Ajouter PostgreSQL
heroku addons:create heroku-postgresql:hobby-dev

# Configurer les variables
heroku config:set JWT_SECRET=your_secret
heroku config:set CORS_ALLOWED_ORIGINS=https://your-frontend.com

# Déployer
git push heroku main

# Vérifier
heroku logs --tail
heroku open
```

#### AWS Elastic Beanstalk

```bash
# Installer EB CLI
pip install awsebcli

# Initialiser
eb init -p docker skillstracker-backend

# Créer l'environnement
eb create skillstracker-prod

# Configurer les variables
eb setenv JWT_SECRET=your_secret DB_URL=your_db_url

# Déployer
eb deploy

# Vérifier
eb status
eb logs
```

---

## 🔍 Vérification Post-Déploiement

### 1. Health Check

```bash
curl https://your-domain.com/actuator/health
```

Réponse attendue :
```json
{"status":"UP"}
```

### 2. Test de connexion

```bash
# Register
curl -X POST https://your-domain.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPassword123",
    "firstName": "Test",
    "lastName": "User"
  }'

# Login
curl -X POST https://your-domain.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPassword123"
  }'
```

### 3. Test API protégée

```bash
curl -X GET https://your-domain.com/api/skills \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Vérifier les logs

```bash
# Systemd
sudo journalctl -u skillstracker -f

# Docker
docker-compose logs -f backend

# Fichier
tail -f /var/log/skillstracker/application.log
```

---

## 🔧 Configuration Nginx (Reverse Proxy)

### Installation

```bash
sudo apt install nginx
```

### Configuration

Créer `/etc/nginx/sites-available/skillstracker` :

```nginx
server {
    listen 80;
    server_name api.your-domain.com;

    # Redirection HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name api.your-domain.com;

    # SSL Certificates (Let's Encrypt)
    ssl_certificate /etc/letsencrypt/live/api.your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.your-domain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # Security Headers
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;

    # Proxy vers le backend
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Rate limiting
    limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;
    limit_req zone=api_limit burst=20 nodelay;

    # Logs
    access_log /var/log/nginx/skillstracker-access.log;
    error_log /var/log/nginx/skillstracker-error.log;
}
```

Activer :

```bash
sudo ln -s /etc/nginx/sites-available/skillstracker /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Certificat SSL (Let's Encrypt)

```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d api.your-domain.com
sudo certbot renew --dry-run
```

---

## 📊 Monitoring

### 1. Logs

Configurer la rotation des logs dans `/etc/logrotate.d/skillstracker` :

```
/var/log/skillstracker/*.log {
    daily
    rotate 14
    compress
    delaycompress
    notifempty
    create 0640 skillstracker skillstracker
    sharedscripts
    postrotate
        systemctl reload skillstracker
    endscript
}
```

### 2. Métriques

Accéder aux métriques via Actuator :

```bash
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/health
```

### 3. Alertes (optionnel)

Intégrer avec :
- Prometheus + Grafana
- ELK Stack (Elasticsearch, Logstash, Kibana)
- CloudWatch (AWS)
- Datadog
- New Relic

---

## 🔄 Mise à Jour

### 1. Sauvegarder la base de données

```bash
pg_dump -U skilltracker_user skillstracker > backup_$(date +%Y%m%d).sql
```

### 2. Tester en staging

```bash
# Déployer sur environnement de test d'abord
```

### 3. Déployer en production

```bash
# Systemd
sudo systemctl stop skillstracker
# Remplacer le JAR
sudo systemctl start skillstracker

# Docker
docker-compose pull
docker-compose up -d
```

### 4. Vérifier

```bash
curl https://your-domain.com/actuator/health
```

---

## 🚨 Rollback

En cas de problème :

```bash
# Systemd - restaurer l'ancien JAR
sudo systemctl stop skillstracker
# Copier l'ancien JAR
sudo systemctl start skillstracker

# Docker - utiliser l'ancien tag
docker-compose down
docker-compose up -d skillstracker-backend:previous-version

# Base de données - restaurer le backup
psql -U skilltracker_user skillstracker < backup_20260125.sql
```

---

## 📞 Support

En cas de problème, vérifier :
1. Les logs de l'application
2. Les logs de la base de données
3. Les logs Nginx
4. La connectivité réseau
5. Les variables d'environnement

---

## 🔐 Checklist Sécurité Finale

- [ ] Clé JWT unique et sécurisée
- [ ] HTTPS activé (SSL/TLS)
- [ ] CORS configuré correctement
- [ ] Swagger désactivé en production
- [ ] Logs SQL désactivés
- [ ] Mots de passe forts pour DB
- [ ] Utilisateur non-root pour l'application
- [ ] Firewall configuré
- [ ] Rate limiting activé
- [ ] Security headers configurés
- [ ] Backups automatiques configurés
- [ ] Monitoring en place
- [ ] Plan de rollback testé

