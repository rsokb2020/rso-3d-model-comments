# RSO: Image metadata microservice

## Prerequisites

```bash
docker run -d --name pg-model3d-metadata -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=model3d-metadata -p 5432:5432 postgres:13
```