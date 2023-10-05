FROM maven:3.6.3 AS maven
COPY . ./
RUN mvn clean
RUN mvn package
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
