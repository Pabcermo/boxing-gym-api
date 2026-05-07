# ---- ETAPA 1: Compilar ----
# Usamos una imagen con Maven y Java 21 para construir el JAR
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar el cache de Docker
# Si el pom.xml no cambia, Docker no re-descarga las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# ---- ETAPA 2: Ejecutar ----
# Imagen mucho más liviana, solo para correr el JAR
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos solo el JAR generado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Puerto que expone la app (Cloud Run usa 8080 por defecto)
EXPOSE 8080

# Comando para arrancar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]