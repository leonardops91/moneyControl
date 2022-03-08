FROM adoptopenjdk/openjdk8

ARG JAR_FILE=target/moneycontrol-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080:8080

ENTRYPOINT ["java", "-jar", "/app.jar"]