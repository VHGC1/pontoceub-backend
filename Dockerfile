FROM maven:3.8-openjdk-17 as maven

WORKDIR /app

COPY ./target/timesheet-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java","-jar","timesheet-0.0.1-SNAPSHOT.jar"]
