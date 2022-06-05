FROM openjdk:17-oracle

ARG JAR_FILE=*.jar

COPY target/${JAR_FILE} asterisk-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "asterisk-backend.jar"]