FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=/build/libs/market-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} market.jar
ENTRYPOINT ["java", "-jar", "market.jar"]