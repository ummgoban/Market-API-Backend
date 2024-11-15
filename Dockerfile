FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=build/libs/market-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} market.jar

ENTRYPOINT ["java", "-jar", "market.jar"]