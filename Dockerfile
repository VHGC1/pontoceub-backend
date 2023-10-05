FROM maven:3.8-openjdk-17 as build

# Copy folder in docker
WORKDIR /opt/app
COPY ./ /opt/app
RUN mvn clean install -DskipTests
# Run spring boot in Docker
FROM openjdk:17-oracle
COPY --from=build /opt/app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","timesheet-0.0.1-SNAPSHOT.jar"]