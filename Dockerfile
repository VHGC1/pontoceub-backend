FROM maven:4.0.0-jdk-17-slim AS build
COPY . ./
RUN mvn clean
RUN mvn package
FROM adoptopenjdk/openjdk17:jdk-17.0.8.1
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
