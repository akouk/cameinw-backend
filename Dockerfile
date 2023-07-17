FROM openjdk:17.0.1-jdk-slim
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring
WORKDIR /app
COPY target/*.jar cameinw-app.jar
ENTRYPOINT ["java", "-jar", "/app/cameinw-app.jar"]
