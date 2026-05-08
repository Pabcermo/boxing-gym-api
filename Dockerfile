# ---- ETAPA 1: Compilar ----
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiamos el pom.xml
COPY pom.xml .

# Descargamos dependencias con reintento y sin fallar en plugins opcionales
RUN mvn dependency:resolve -B --fail-never

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests -B \
    -Dmaven.wagon.http.retryHandler.count=3 \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=25

# ---- ETAPA 2: Ejecutar ----
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]