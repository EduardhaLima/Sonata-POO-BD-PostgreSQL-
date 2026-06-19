# ============================================================================
#  Dockerfile da Sonata API
#  -------------------------------------------------------------------------
#  Build multi-stage: compila o JAR com Maven+JDK17 e roda em imagem JRE leve.
# ============================================================================

# --- ETAPA 1: build do JAR usando Maven + JDK 17 ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# --- ETAPA 2: imagem final, so com o runtime Java ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/sonata-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
