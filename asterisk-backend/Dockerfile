FROM openjdk:17

ARG JAR_FILE=*.jar

COPY target/${JAR_FILE} asterisk-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "asterisk-backend.jar"]