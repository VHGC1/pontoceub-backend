FROM arm32v7/eclipse-temurin
VOLUME /tmp
ARG JAR_FILE timesheet-0.0.1-SNAPSHOT
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=stg","-jar","/app.jar"]