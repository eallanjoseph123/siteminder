FROM openjdk:8-jre-alpine

WORKDIR /app

COPY target/siteminder-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "siteminder-0.0.1-SNAPSHOT.jar"]