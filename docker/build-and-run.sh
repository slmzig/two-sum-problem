#!/bin/bash

sbt clean

sbt docker:publishLocal

sh ./docker-down.sh
sh ./docker-up.sh