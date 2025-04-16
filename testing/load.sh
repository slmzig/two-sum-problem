#!/bin/bash

URL="http://localhost:3000/find"
HEADERS="-H Content-Type:application/json"

TARGETS=(19990)

TARGET=${TARGETS[$RANDOM % ${#TARGETS[@]}]}

BODY=$(jq -n --argjson arr "$(seq 1 10000 | jq -R . | jq -s .)" --argjson target "$TARGET" '{data: $arr, target: $target}')

hey -n 1000 -c 100 \
  -m POST \
  $HEADERS \
  -d "$BODY" \
  $URL
