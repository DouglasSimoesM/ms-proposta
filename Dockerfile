FROM openjdk:21-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw -B clean dependency:resolve
RUN ./mvnw -B clean install

ENV APP_PROFILE=prod

EXPOSE 8080
EXPOSE 5432
EXPOSE 5672
EXPOSE 15672

ENTRYPOINT ["java", "-Dspring.profiles.active=${APP_PROFILE}", "-jar", "target/proposta.app-0.0.1-SNAPSHOT.jar"]
