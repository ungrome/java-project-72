FROM eclipse-temurin:20-jdk
ARG GRADLE_VERSION=8.7

WORKDIR /app

COPY /app .

RUN gradle shadowJar

CMD ["java", "-jar", "build/libs/app-1.0-SNAPSHOT.jar"]
