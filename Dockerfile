FROM openjdk:21-jdk
WORKDIR /app
COPY . .
CMD ["java", "-jar", "proposta-app.jar"]
