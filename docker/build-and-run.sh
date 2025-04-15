#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

sbt clean
sbt docker:publishLocal

sh "$SCRIPT_DIR/docker-down.sh"
sh "$SCRIPT_DIR/docker-up.sh"
