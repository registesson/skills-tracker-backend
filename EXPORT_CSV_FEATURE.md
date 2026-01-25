# Export des Compétences en CSV

## Description

Cette fonctionnalité permet aux utilisateurs d'exporter leurs compétences au format CSV pour les analyser dans d'autres outils (Excel, Google Sheets, etc.).

## Endpoint API

### GET /api/skills/export/csv

Exporte toutes les compétences de l'utilisateur connecté au format CSV.

#### Authentification
Requiert un token JWT valide dans l'en-tête `Authorization: Bearer <token>`

#### Réponse

**Status Code:** 200 OK

**Content-Type:** text/csv

**Headers:**
- `Content-Disposition: attachment; filename="mes-competences.csv"`
- `Cache-Control: must-revalidate, post-check=0, pre-check=0`

#### Format du fichier CSV

Le fichier CSV contient les colonnes suivantes :

| Colonne | Description | Type |
|---------|-------------|------|
| nom | Nom de la compétence | String |
| categorie | Catégorie de la compétence | Enum (PROGRAMMING, FRAMEWORK, DATABASE, etc.) |
| niveau | Niveau actuel de la compétence | Enum (BEGINNER, ELEMENTARY, INTERMEDIATE, ADVANCED, EXPERT) |
| temps_total_minutes | Temps total passé sur cette compétence (somme de toutes les sessions d'apprentissage) | Integer |

#### Exemple de fichier CSV généré

```csv
nom,categorie,niveau,temps_total_minutes
Java,PROGRAMMING,INTERMEDIATE,150
React,FRAMEWORK,BEGINNER,45
PostgreSQL,DATABASE,ADVANCED,200
"Spring Boot, avec injection",FRAMEWORK,INTERMEDIATE,180
```

**Note:** Les noms contenant des virgules, guillemets ou retours à la ligne sont automatiquement échappés selon la norme CSV (RFC 4180).

## Utilisation

### Avec cURL

```bash
curl -X GET "http://localhost:8081/api/skills/export/csv" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -o mes-competences.csv
```

### Avec JavaScript (Frontend)

```javascript
async function exportSkills() {
  const token = localStorage.getItem('jwt_token');
  
  const response = await fetch('http://localhost:8081/api/skills/export/csv', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  if (response.ok) {
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'mes-competences.csv';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }
}
```

## Implémentation Technique

### Architecture

La fonctionnalité est implémentée en utilisant le pattern Service/Controller :

1. **SkillExportService** : Service responsable de la génération du fichier CSV
   - Récupère les compétences de l'utilisateur depuis le repository
   - Calcule le temps total pour chaque compétence
   - Génère le contenu CSV avec échappement approprié

2. **SkillController** : Endpoint REST pour déclencher l'export
   - Authentifie l'utilisateur
   - Appelle le service d'export
   - Configure les en-têtes HTTP pour le téléchargement

### Classes modifiées/créées

- ✅ `com.skillstracker.application.skill.SkillExportService` (nouveau)
- ✅ `com.skillstracker.infrastructure.web.controllers.SkillController` (modifié)
- ✅ `com.skillstracker.application.skill.SkillExportServiceTest` (nouveau)

## Tests

Les tests unitaires couvrent les cas suivants :
- Export de compétences avec sessions d'apprentissage
- Export de compétences sans sessions (temps = 0)
- Calcul correct du temps total
- Échappement CSV pour les noms avec caractères spéciaux
- Gestion d'une liste vide de compétences

Pour exécuter les tests :

```bash
mvn test -Dtest=SkillExportServiceTest
```

## Critères d'acceptation ✓

- ✅ Format CSV standard (RFC 4180)
- ✅ Colonnes: nom, catégorie, niveau, temps total (en minutes)
- ✅ Téléchargement direct avec Content-Disposition
- ✅ Échappement CSV pour les caractères spéciaux
- ✅ Authentification requise
- ✅ Tests unitaires complets

## Notes de développement

### Encodage
Le fichier CSV est généré en UTF-8 pour supporter les caractères accentués et internationaux.

### Performance
Pour de grandes quantités de compétences (>1000), le temps de génération reste acceptable car :
- Le calcul du temps total utilise les streams Java (optimisé)
- La génération se fait en mémoire avec ByteArrayOutputStream
- Pas de requêtes SQL supplémentaires (fetch eager des sessions)

### Évolutions futures possibles

1. **Paramètres de filtrage** : Permettre de filtrer par catégorie ou niveau
2. **Format alternatif** : Ajouter un export en JSON ou Excel
3. **Colonnes supplémentaires** : Date de création, dernière mise à jour, nombre de ressources
4. **Compression** : Pour les exports volumineux, générer un fichier ZIP
5. **Export asynchrone** : Pour de très grandes bases de données, utiliser un job asynchrone avec notification email

