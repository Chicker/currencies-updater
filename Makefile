
build:
	sbt clean currencies-updater/stage

build-and-run: build
	docker-compose up --build

run:
	docker-compose up
