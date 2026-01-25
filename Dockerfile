FROM eclipse-temurin:21-jre-alpine

# Métadonnées
LABEL maintainer="Skills Tracker Team <support@skillstracker.com>"
LABEL description="Skills Tracker Backend - API REST"
LABEL version="0.0.1-SNAPSHOT"

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080 \
    JAVA_OPTS="-Xmx512m -Xms256m"

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring

# Répertoire de travail
WORKDIR /app

# Copier le JAR compilé
COPY target/skills-tracker-backend-0.0.1-SNAPSHOT.jar app.jar

# Changer le propriétaire
RUN chown spring:spring app.jar

# Utiliser l'utilisateur non-root
USER spring:spring

# Port exposé
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Commande de démarrage
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]

