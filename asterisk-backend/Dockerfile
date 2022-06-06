FROM openjdk:17 as builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM openjdk:17

ARG JAR_FILE=*.jar

COPY target/${JAR_FILE} asterisk-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "asterisk-backend.jar"]