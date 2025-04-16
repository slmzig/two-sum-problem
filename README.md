# Two Sum Problem Service

A minimal Scala backend that exposes an endpoint to solve the Two Sum Problem with rate limiting, environment-based configuration, and Docker deployment support.

---

## Run the Service via Docker

### Build and run app in docker
```bash
./docker/build-and-run.sh
```
### Start the service manualy:
```bash
./docker/docker-up.sh
```

### Stop the service manualy:
```bash
./docker/docker-down.sh
```

---

## Configuration via Environment Variables

Configuration can be passed directly in `docker-compose.yml`:

| Variable            | Description                              | Example     |
|---------------------|------------------------------------------|-------------|
| `HTTP_HOST`         | Host IP to bind the service              | `0.0.0.0`   |
| `HTTP_PORT`         | Port to expose the service               | `3000`      |
| `DEFAULT_TARGET`    | Fallback target value                    | `10`        |
| `RATE_LIMIT_AMOUNT` | Max number of requests per interval      | `100`       |
| `RATE_LIMIT_PER`    | Interval for rate limit (e.g., `30s`)    | `30s`       |

---

## API Endpoints

### `POST /find`

Finds all unique pairs of numbers in the array that sum to the given `target`.

#### Request
```json
{
  "data": [3, 8, 10, 14],
  "target": 18
}
```
*or (target will be resolved):*
```json
{
  "data": [3, 8, 10, 14]
}
```

#### Response
```json
{
  "target": 18,
  "pairs": [
    {
      "indices": [1, 2],
      "numbers": [8, 10]
    }
  ]
}
```

#### 🔸 Status Codes
| Code | Description                        |
|------|------------------------------------|
| 200  | OK                                 |
| 400  | Validation error                   |
| 429  | Rate limit exceeded                |
| 500  | Internal server error              |

---

## Example Test

```bash
curl -X POST http://localhost:3000/find \
  -H "Content-Type: application/json" \
  -d '{"data": [1,2,3], "target": 5}'
```

**Test rate-limiting:**
```bash
hey -n 20 -c 10 -m POST -H "Content-Type: application/json" \
  -d '{"data": [1,2,3], "target": 5}' http://localhost:3000/find
```

**Load testing:**
```bash
URL="http://localhost:3000/find"
HEADERS="-H Content-Type:application/json"

TARGETS=(19990)

TARGET=${TARGETS[$RANDOM % ${#TARGETS[@]}]}

BODY=$(jq -n --argjson arr "$(seq 1 10000 | jq -R . | jq -s .)" --argjson target "$TARGET" '{data: $arr, target: $target}')

hey -n 100 -c 100 \
  -m POST \
  $HEADERS \
  -d "$BODY" \
  $URL
```
---
## Swagger docs

You can view the OpenAPI 3.0 specification file:

**[openapi.yaml](swagger/openapi.yaml)**

You can use this in tools like:
- [Swagger Editor](https://editor.swagger.io/)
- Postman (import as OpenAPI)