## About

The architecture of the code:
- Tagless Final approach with using Cats and Tofu

Which libraries are used:

- tofu (Logging, Race)
- cats/cats effect
- circe (JSON encode/decode)
- doobie (accessing to Postgres)
- monix
- pureconfig (loading config. params from application.conf file)
- sttp (http client)
- flyway (database migrations)

## Database

```
docker run \
    --name currencies \
    --memory="256m" \
    -p 5500:5432 \
    -e POSTGRES_PASSWORD=postgres \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_DB=currencies \
    -d \
    postgres:11.10
```