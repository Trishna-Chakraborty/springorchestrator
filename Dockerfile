FROM openjdk:8-jdk-alpine
EXPOSE 8080
WORKDIR /app
COPY target/springorchestrator-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "springorchestrator-0.0.1-SNAPSHOT.jar" ]