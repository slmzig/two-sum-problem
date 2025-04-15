#!/bin/bash

cd "$(dirname "$0")/.."

sbt clean

sbt docker:publishLocal

docker compose -f docker/docker-compose.yaml down

# run as a daemon
docker compose -f docker/docker-compose.yaml up --build -d

# run with log attached to the console
#docker compose -f docker/docker-compose.yaml up --build
