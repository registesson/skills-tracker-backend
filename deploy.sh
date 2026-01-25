#!/bin/bash

# Script de déploiement rapide - Skills Tracker Backend
# Usage: ./deploy.sh [dev|staging|prod]

set -e

ENVIRONMENT=${1:-dev}
echo "🚀 Déploiement pour l'environnement: $ENVIRONMENT"

# Vérifier que Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven n'est pas installé"
    exit 1
fi

# Fonction de vérification des variables d'environnement
check_env_vars() {
    echo "🔍 Vérification des variables d'environnement..."

    if [ "$ENVIRONMENT" == "prod" ]; then
        if [ -z "$DB_URL" ] || [ -z "$JWT_SECRET" ]; then
            echo "❌ Variables d'environnement manquantes pour la production"
            echo "   Requises: DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET"
            exit 1
        fi

        if [ "$JWT_SECRET" == "CHANGE_THIS_IN_PRODUCTION" ]; then
            echo "❌ La clé JWT par défaut ne peut pas être utilisée en production"
            echo "   Générer une nouvelle clé avec: openssl rand -base64 64"
            exit 1
        fi
    fi

    echo "✅ Variables d'environnement OK"
}

# Fonction de compilation
build() {
    echo "🔨 Compilation du projet..."
    mvn clean package -DskipTests
    echo "✅ Compilation réussie"
}

# Fonction de test
run_tests() {
    echo "🧪 Exécution des tests..."
    mvn test
    echo "✅ Tests passés avec succès"
}

# Déploiement selon l'environnement
case $ENVIRONMENT in
    dev)
        echo "📦 Déploiement en développement..."
        mvn spring-boot:run
        ;;

    staging)
        echo "📦 Déploiement en staging..."
        check_env_vars
        build
        run_tests

        echo "🐳 Lancement avec Docker..."
        docker-compose up -d

        echo "⏳ Attente du démarrage (30s)..."
        sleep 30

        echo "🔍 Vérification du health check..."
        if curl -f http://localhost:8080/actuator/health; then
            echo "✅ Application démarrée avec succès"
        else
            echo "❌ Erreur lors du démarrage"
            docker-compose logs backend
            exit 1
        fi
        ;;

    prod)
        echo "📦 Déploiement en production..."
        check_env_vars

        echo "⚠️  ATTENTION: Déploiement en PRODUCTION"
        echo "   Êtes-vous sûr de vouloir continuer? (y/N)"
        read -r response
        if [[ ! "$response" =~ ^[Yy]$ ]]; then
            echo "❌ Déploiement annulé"
            exit 1
        fi

        build
        run_tests

        echo "🚀 Déploiement du JAR..."
        java -jar target/skills-tracker-backend-0.0.1-SNAPSHOT.jar \
            --spring.profiles.active=prod
        ;;

    *)
        echo "❌ Environnement invalide: $ENVIRONMENT"
        echo "Usage: ./deploy.sh [dev|staging|prod]"
        exit 1
        ;;
esac

echo "🎉 Déploiement terminé avec succès!"

