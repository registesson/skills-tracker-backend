# Exemples d'Utilisation - Export CSV

Ce document fournit des exemples pratiques pour utiliser la fonctionnalité d'export CSV des compétences.

## 1. Test Manuel avec cURL

### Prérequis
- Le serveur backend doit être démarré (`mvn spring-boot:run`)
- Vous devez avoir un token JWT valide

### Obtenir un token JWT (si nécessaire)

```bash
# S'enregistrer
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'

# Se connecter
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### Exporter les compétences en CSV

```bash
# Remplacez YOUR_JWT_TOKEN par votre token
curl -X GET "http://localhost:8081/api/skills/export/csv" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -o mes-competences.csv

# Vérifier le contenu du fichier
cat mes-competences.csv
```

## 2. Intégration Frontend React

### Composant React avec bouton d'export

```jsx
import React, { useState } from 'react';
import { Download } from 'lucide-react';

const SkillExportButton = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleExport = async () => {
    setLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem('jwt_token');
      
      const response = await fetch('http://localhost:8081/api/skills/export/csv', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        throw new Error('Échec de l\'export');
      }

      // Créer un blob et déclencher le téléchargement
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'mes-competences.csv';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);

      // Notification de succès (optionnel)
      console.log('Export réussi !');
    } catch (err) {
      setError(err.message);
      console.error('Erreur lors de l\'export:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <button 
        onClick={handleExport}
        disabled={loading}
        className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
      >
        <Download size={20} />
        {loading ? 'Export en cours...' : 'Exporter en CSV'}
      </button>
      {error && (
        <div className="mt-2 text-red-600 text-sm">
          Erreur: {error}
        </div>
      )}
    </div>
  );
};

export default SkillExportButton;
```

## 3. Intégration avec Vue.js

### Composant Vue.js

```vue
<template>
  <div>
    <button 
      @click="exportSkills"
      :disabled="loading"
      class="export-button"
    >
      <span v-if="loading">Export en cours...</span>
      <span v-else>📥 Exporter en CSV</span>
    </button>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'SkillExportButton',
  data() {
    return {
      loading: false,
      error: null
    };
  },
  methods: {
    async exportSkills() {
      this.loading = true;
      this.error = null;

      try {
        const token = localStorage.getItem('jwt_token');
        
        const response = await fetch('http://localhost:8081/api/skills/export/csv', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (!response.ok) {
          throw new Error('Échec de l\'export');
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'mes-competences.csv';
        link.click();
        window.URL.revokeObjectURL(url);

        this.$emit('export-success');
      } catch (err) {
        this.error = err.message;
        this.$emit('export-error', err);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.export-button {
  padding: 0.5rem 1rem;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
}

.export-button:hover:not(:disabled) {
  background-color: #2563eb;
}

.export-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.error-message {
  margin-top: 0.5rem;
  color: #dc2626;
  font-size: 0.875rem;
}
</style>
```

## 4. Utilisation avec Angular

### Service Angular

```typescript
// skill-export.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SkillExportService {
  private apiUrl = 'http://localhost:8081/api/skills';

  constructor(private http: HttpClient) {}

  exportSkillsToCsv(): Observable<Blob> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get(`${this.apiUrl}/export/csv`, {
      headers,
      responseType: 'blob'
    });
  }

  downloadCsvFile(blob: Blob, filename: string = 'mes-competences.csv'): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}
```

### Composant Angular

```typescript
// skill-export-button.component.ts
import { Component } from '@angular/core';
import { SkillExportService } from './skill-export.service';

@Component({
  selector: 'app-skill-export-button',
  template: `
    <button 
      (click)="exportSkills()"
      [disabled]="loading"
      class="export-btn">
      {{ loading ? 'Export en cours...' : 'Exporter en CSV' }}
    </button>
    <div *ngIf="error" class="error">{{ error }}</div>
  `,
  styles: [`
    .export-btn {
      padding: 0.5rem 1rem;
      background-color: #3b82f6;
      color: white;
      border: none;
      border-radius: 0.375rem;
      cursor: pointer;
    }
    .export-btn:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
    .error {
      color: #dc2626;
      margin-top: 0.5rem;
    }
  `]
})
export class SkillExportButtonComponent {
  loading = false;
  error: string | null = null;

  constructor(private exportService: SkillExportService) {}

  exportSkills(): void {
    this.loading = true;
    this.error = null;

    this.exportService.exportSkillsToCsv().subscribe({
      next: (blob) => {
        this.exportService.downloadCsvFile(blob);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors de l\'export';
        this.loading = false;
        console.error('Export error:', err);
      }
    });
  }
}
```

## 5. Test avec Postman

1. Créer une nouvelle requête GET
2. URL: `http://localhost:8081/api/skills/export/csv`
3. Headers:
   - Key: `Authorization`
   - Value: `Bearer YOUR_JWT_TOKEN`
4. Cliquer sur "Send"
5. Cliquer sur "Save Response" > "Save to a file" pour télécharger le CSV

## 6. Analyse du CSV avec Python

```python
import pandas as pd
import matplotlib.pyplot as plt

# Charger le fichier CSV
df = pd.read_csv('mes-competences.csv')

# Afficher les premières lignes
print(df.head())

# Statistiques de base
print("\nStatistiques:")
print(df['temps_total_minutes'].describe())

# Temps total par catégorie
temps_par_categorie = df.groupby('categorie')['temps_total_minutes'].sum()
print("\nTemps total par catégorie:")
print(temps_par_categorie)

# Visualisation
plt.figure(figsize=(10, 6))
temps_par_categorie.plot(kind='bar')
plt.title('Temps total par catégorie de compétence')
plt.xlabel('Catégorie')
plt.ylabel('Temps (minutes)')
plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig('temps_par_categorie.png')
plt.show()

# Distribution des niveaux
niveau_distribution = df['niveau'].value_counts()
print("\nDistribution des niveaux:")
print(niveau_distribution)
```

## 7. Analyse avec R

```r
# Charger le fichier CSV
skills <- read.csv("mes-competences.csv", stringsAsFactors = FALSE)

# Afficher les premières lignes
head(skills)

# Résumé statistique
summary(skills$temps_total_minutes)

# Temps total par catégorie
library(dplyr)
temps_par_categorie <- skills %>%
  group_by(categorie) %>%
  summarise(temps_total = sum(temps_total_minutes)) %>%
  arrange(desc(temps_total))

print(temps_par_categorie)

# Visualisation
library(ggplot2)
ggplot(temps_par_categorie, aes(x = reorder(categorie, temps_total), y = temps_total)) +
  geom_bar(stat = "identity", fill = "steelblue") +
  coord_flip() +
  labs(title = "Temps total par catégorie",
       x = "Catégorie",
       y = "Temps (minutes)") +
  theme_minimal()
```

## Résolution de problèmes

### Erreur 401 Unauthorized
- Vérifiez que votre token JWT est valide
- Vérifiez que le token n'est pas expiré
- Assurez-vous d'inclure "Bearer " avant le token

### Fichier CSV vide
- Vérifiez que vous avez des compétences enregistrées dans votre compte
- Vérifiez que l'userId est correct

### Caractères bizarres dans le CSV
- Assurez-vous d'ouvrir le fichier avec l'encodage UTF-8
- Dans Excel: Data > From Text/CSV > File Origin: UTF-8

### Erreur CORS
- Vérifiez la configuration CORS du backend
- Assurez-vous que l'origine du frontend est autorisée

