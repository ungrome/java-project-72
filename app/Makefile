
.DEFAULT_GOAL := build-run

install:
	./gradlew install

run-dist:
	./gradlew clean

build:
	./gradlew build

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

build-run: build run

.PHONY: build