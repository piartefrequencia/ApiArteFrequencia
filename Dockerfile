
# ESTAGIO DE BUILD DA IMAGEM DOCKER

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ESTAGIO DE EXECUÇÃO DA IMAGEM DOCKER

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ApiArteFrequencia-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xmx512M -Xms256M"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]



