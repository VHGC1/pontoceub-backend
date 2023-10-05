FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE timesheet-0.0.1-SNAPSHOT
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]