FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.7

WORKDIR /

COPY ./ .

RUN ./gradlew --no-daemon dependencies

RUN ./gradlew --no-daemon build

EXPOSE 8080

CMD java -jar build/libs/app-1.0-SNAPSHOT.jar
