FROM openjdk:17-jdk-slim-buster
VOLUME /tmp
ARG JAR_FILE timesheet-0.0.1-SNAPSHOT
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]