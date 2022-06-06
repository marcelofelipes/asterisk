# Copy project and install dependencies
FROM openjdk:17 as builder

COPY ./ ./

RUN ./mvnw install -DskipTests

# Copy the built .jar file
FROM openjdk:17

COPY --from=builder target/asterisk-backend.jar /asterisk-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/asterisk-backend.jar"]