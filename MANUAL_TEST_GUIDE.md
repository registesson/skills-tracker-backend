# Guide de Test Manuel - Export CSV

## Prérequis
- Le serveur backend doit être lancé: `mvn spring-boot:run`
- Port par défaut: 8081

## Scénario de Test Complet

### Étape 1: Créer un utilisateur (si nécessaire)

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Jean",
    "lastName": "Dupont"
  }'
```

**Résultat attendu:** Statut 201 Created avec un token JWT

### Étape 2: Se connecter et récupérer le token

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Résultat attendu:** 
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

Copiez le token pour les étapes suivantes.

### Étape 3: Créer quelques compétences

#### Compétence 1: Java
```bash
curl -X POST http://localhost:8081/api/skills \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "name": "Java",
    "category": "PROGRAMMING",
    "currentLevel": "INTERMEDIATE"
  }'
```

#### Compétence 2: Spring Boot
```bash
curl -X POST http://localhost:8081/api/skills \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "name": "Spring Boot",
    "category": "FRAMEWORK",
    "currentLevel": "ADVANCED"
  }'
```

#### Compétence 3: PostgreSQL
```bash
curl -X POST http://localhost:8081/api/skills \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "name": "PostgreSQL",
    "category": "DATABASE",
    "currentLevel": "INTERMEDIATE"
  }'
```

#### Compétence 4: Avec caractères spéciaux
```bash
curl -X POST http://localhost:8081/api/skills \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "name": "React, avec Hooks",
    "category": "FRAMEWORK",
    "currentLevel": "BEGINNER"
  }'
```

### Étape 4: Ajouter des sessions d'apprentissage

Pour chaque compétence, récupérez son ID depuis la réponse de création, puis créez des sessions.

```bash
# Session pour Java (remplacer SKILL_ID)
curl -X POST http://localhost:8081/api/learning-sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "skillName": "Java",
    "sessionDate": "2026-01-20",
    "durationMinutes": 60,
    "notes": "Étude des streams",
    "resourcesUsed": "Documentation officielle"
  }'

# Session supplémentaire pour Java
curl -X POST http://localhost:8081/api/learning-sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "skillName": "Java",
    "sessionDate": "2026-01-21",
    "durationMinutes": 90,
    "notes": "Pratique avec des exercices",
    "resourcesUsed": "Exercism.io"
  }'

# Session pour Spring Boot
curl -X POST http://localhost:8081/api/learning-sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "skillName": "Spring Boot",
    "sessionDate": "2026-01-22",
    "durationMinutes": 120,
    "notes": "Création d API REST",
    "resourcesUsed": "Spring Boot Documentation"
  }'

# Session pour PostgreSQL
curl -X POST http://localhost:8081/api/learning-sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "skillName": "PostgreSQL",
    "sessionDate": "2026-01-23",
    "durationMinutes": 45,
    "notes": "Optimisation de requêtes",
    "resourcesUsed": "PostgreSQL Tutorial"
  }'
```

### Étape 5: Exporter les compétences en CSV

```bash
curl -X GET http://localhost:8081/api/skills/export/csv \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -o mes-competences.csv
```

**Résultat attendu:**
- Statut: 200 OK
- Fichier `mes-competences.csv` créé dans le répertoire courant

### Étape 6: Vérifier le contenu du CSV

```bash
cat mes-competences.csv
```

**Contenu attendu:**
```csv
nom,categorie,niveau,temps_total_minutes
Java,PROGRAMMING,INTERMEDIATE,150
Spring Boot,FRAMEWORK,ADVANCED,120
PostgreSQL,DATABASE,INTERMEDIATE,45
"React, avec Hooks",FRAMEWORK,BEGINNER,0
```

### Étape 7: Vérifier les headers HTTP

```bash
curl -I -X GET http://localhost:8081/api/skills/export/csv \
  -H "Authorization: Bearer VOTRE_TOKEN"
```

**Headers attendus:**
```
HTTP/1.1 200 OK
Content-Type: text/csv
Content-Disposition: form-data; name="attachment"; filename="mes-competences.csv"
Cache-Control: must-revalidate, post-check=0, pre-check=0
```

## Tests de Cas Limites

### Test 1: Export sans compétences
Créez un nouvel utilisateur et essayez d'exporter directement.

**Résultat attendu:**
```csv
nom,categorie,niveau,temps_total_minutes
```
(Seulement l'en-tête)

### Test 2: Export sans authentification
```bash
curl -X GET http://localhost:8081/api/skills/export/csv
```

**Résultat attendu:** Statut 401 Unauthorized

### Test 3: Export avec token invalide
```bash
curl -X GET http://localhost:8081/api/skills/export/csv \
  -H "Authorization: Bearer token_invalide"
```

**Résultat attendu:** Statut 401 Unauthorized

### Test 4: Compétence avec nom contenant des guillemets
```bash
curl -X POST http://localhost:8081/api/skills \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -d '{
    "name": "Framework \"moderne\"",
    "category": "FRAMEWORK",
    "currentLevel": "BEGINNER"
  }'
```

Puis exportez et vérifiez que les guillemets sont correctement échappés.

## Vérification dans Excel/Google Sheets

1. Ouvrez le fichier `mes-competences.csv`
2. Si vous utilisez Excel:
   - Allez dans Données > Importer depuis un fichier texte/CSV
   - Choisissez l'encodage UTF-8
   - Vérifiez que les colonnes sont bien séparées
3. Si vous utilisez Google Sheets:
   - Fichier > Importer > Sélectionnez le CSV
   - Type de séparateur: Virgule
   - Vérifiez l'affichage

## Critères de Validation

### ✅ Format CSV
- [ ] L'en-tête contient les 4 colonnes: nom, categorie, niveau, temps_total_minutes
- [ ] Chaque ligne correspond à une compétence
- [ ] Les valeurs sont séparées par des virgules
- [ ] Les noms avec virgules sont entre guillemets

### ✅ Calcul du Temps
- [ ] Le temps total est la somme de toutes les sessions
- [ ] Les compétences sans session ont un temps de 0
- [ ] Le temps est en minutes

### ✅ Téléchargement
- [ ] Le fichier est téléchargé automatiquement (avec un navigateur)
- [ ] Le nom du fichier est "mes-competences.csv"
- [ ] Le Content-Type est "text/csv"

### ✅ Sécurité
- [ ] Sans token, l'accès est refusé (401)
- [ ] Avec un token invalide, l'accès est refusé (401)
- [ ] Chaque utilisateur ne voit que ses propres compétences

### ✅ Encodage
- [ ] Les caractères accentués s'affichent correctement
- [ ] Les caractères spéciaux sont préservés

## Résolution de Problèmes

### Le fichier CSV est vide
- Vérifiez que vous avez créé des compétences
- Vérifiez le token JWT
- Consultez les logs du serveur

### Caractères bizarres
- Assurez-vous d'ouvrir le CSV avec l'encodage UTF-8
- Dans Excel, utilisez l'import de données plutôt que l'ouverture directe

### Erreur 401
- Vérifiez que le token n'est pas expiré
- Vérifiez le format: `Authorization: Bearer <token>`

### Erreur 500
- Consultez les logs du serveur
- Vérifiez la connexion à la base de données

## Checklist Finale

- [ ] Export fonctionne avec authentification
- [ ] Export bloqué sans authentification
- [ ] Calcul du temps correct
- [ ] Échappement CSV fonctionnel
- [ ] Encodage UTF-8 correct
- [ ] Nom de fichier correct
- [ ] Headers HTTP corrects
- [ ] Cas limite: liste vide
- [ ] Cas limite: compétences sans sessions
- [ ] Cas limite: noms avec caractères spéciaux

---

**Date de test:** _______________  
**Testeur:** _______________  
**Résultat global:** ☐ PASS   ☐ FAIL  
**Commentaires:** _______________________________________________

