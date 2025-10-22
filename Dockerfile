#FROM maven:3.9.6-eclipse-temurin-21 AS builder
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests



FROM openjdk:21-jdk-slim

WORKDIR /app

#COPY --from=builder /app/target/*.jar app.jar
COPY target/*.jar app.jar

EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "app.jar"]
# Run in debug mode (suspend=n means app starts immediately)
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

