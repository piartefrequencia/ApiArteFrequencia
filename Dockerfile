
# ESTAGIO DE BUILD DA IMAGEM DOCKER

FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ESTAGIO DE EXECUÇÃO DA IMAGEM DOCKER

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ApiArteFrequencia-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

