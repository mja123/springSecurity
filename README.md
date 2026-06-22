# springSecurity — Spring Boot REST API with Auth0 & PostgreSQL

Spring Boot 3.5.6 REST API secured with Auth0 and Spring Security OAuth2 Resource Server. Implements JWT-based authentication and authorization with role-based access control (RBAC) and permission-based authorization backed by a PostgreSQL database.

## Tech stack

| Layer | Technology |
| --- | --- |
| Runtime | Java 21 (Eclipse Temurin) |
| Framework | Spring Boot 3.5.6 |
| Security | Spring Security + OAuth2 Resource Server |
| Identity | Auth0 (JWT) |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA + Hibernate |
| Docs | SpringDoc OpenAPI (Swagger UI at `/swagger-ui.html`) |
| Build | Gradle (Wrapper included) |
| Container | Docker multi-stage build |

## Features

- Auth0 JWT authentication (RS256)
- Role-based access control (`ADMIN`, `USER`)
- Permission-based authorization (`read:books`, `write:books`, `delete:books`)
- Public endpoints (no auth required)
- Protected CRUD endpoints with `@PreAuthorize`
- CORS configuration
- Health probe at `/api/public/health`

## API Endpoints

### Public (no token required)

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/public/health` | Health check (k8s probe target) |
| GET | `/api/public/info` | API information |
| GET | `/api/public/welcome` | Welcome message |

### Protected (Bearer JWT required)

| Method | Path | Required permission / role |
| --- | --- | --- |
| GET | `/api/users` | `read:books` or `USER`/`ADMIN` |
| GET | `/api/users/{id}` | `read:books` or `USER`/`ADMIN` |
| POST | `/api/users` | `write:books` or `ADMIN` |
| PUT | `/api/users/{id}` | `write:books` or `ADMIN` |
| DELETE | `/api/users/{id}` | `delete:books` or `ADMIN` |

## Auth0 setup

1. **Create an Auth0 account** at [auth0.com](https://auth0.com).
2. **Create an API** in *Applications → APIs*:
   - Identifier (audience): `https://spring-api`
   - Add permissions: `read:books`, `write:books`, `delete:books`
3. **Create roles** in *User Management → Roles*: `ADMIN`, `USER`. Assign permissions to them.
4. **Create a Machine-to-Machine application** (for Auth0 management API calls):
   - Go to *Applications → Applications → Create Application → Machine to Machine*
   - Authorize it for your API and grant the needed permissions.
5. Note your **Domain** (`dev-xxxx.us.auth0.com`), **Client ID**, and **Client Secret**.

## Environment variables

| Variable | Description |
| --- | --- |
| `POSTGRES_URL` | JDBC URL — `jdbc:postgresql://postgres:5432/security` |
| `AUTH0_ISSUER_URI` | Auth0 tenant URL with trailing slash, e.g. `https://dev-xxxx.us.auth0.com/` |
| `AUTH0_AUDIENCE` | API identifier, e.g. `https://spring-api` |
| `AUTH0_ROLES_CLAIM_NAMESPACE` | Custom claim namespace for roles, e.g. `https://spring-api/roles` |
| `AUTH0_CLIENT_ID` | Machine-to-Machine app client ID |
| `AUTH0_CLIENT_SECRET` | Machine-to-Machine app client secret |

Copy `.env.example` to `.env` and fill in the values before running locally.

## Local development (Docker Compose)

```bash
cp .env.example .env          # fill in AUTH0_* and POSTGRES_* values
docker compose up -d          # starts postgres on host port 5433
# then run the Spring Boot app from your IDE with the .env vars in run config
```

The compose file starts only PostgreSQL. Run the Spring Boot app from your IDE, passing the environment variables from `.env` in the run configuration.

## Kubernetes deployment

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/)
- [minikube](https://minikube.sigs.k8s.io/docs/start/) (or any k8s cluster)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

### Architecture

```
namespace: spring-security
┌─────────────────────────────────────────────────────────┐
│                                                         │
│  Deployment: spring-security-api (2 replicas)           │
│    image: springsecurity-api:0.0.1                      │
│    → Service: spring-security-api (ClusterIP port 80)  │
│                                                         │
│  StatefulSet: postgres (1 replica, 2 Gi PVC)            │
│    image: postgres:16-alpine                            │
│    → Service: postgres (headless, port 5432)            │
│                                                         │
│  Secrets:                                               │
│    spring-security-api-secret  (POSTGRES_URL,           │
│                                 AUTH0_CLIENT_ID,         │
│                                 AUTH0_CLIENT_SECRET)    │
│    postgres-secret             (POSTGRES_USER,          │
│                                 POSTGRES_PASSWORD)      │
└─────────────────────────────────────────────────────────┘
```

### Step 1 — Build and load the Docker image

```bash
cd springSecurity

# Build the image
docker build -t springsecurity-api:0.0.1 .

# Load it into minikube's local registry
minikube image load springsecurity-api:0.0.1
```

### Step 2 — Create Kubernetes Secrets

Secrets are **not** stored in the manifests. Create them manually:

```bash
# Create the namespace first so the secrets land in the right place
kubectl apply -f k8s/namespace.yaml

# PostgreSQL credentials
kubectl create secret generic postgres-secret \
  --namespace spring-security \
  --from-literal=POSTGRES_USER='security' \
  --from-literal=POSTGRES_PASSWORD='<your-postgres-password>'

# Spring Boot app secrets
kubectl create secret generic spring-security-api-secret \
  --namespace spring-security \
  --from-literal=POSTGRES_URL='jdbc:postgresql://postgres:5432/security' \
  --from-literal=AUTH0_CLIENT_ID='<your-m2m-client-id>' \
  --from-literal=AUTH0_CLIENT_SECRET='<your-m2m-client-secret>'
```

> The `POSTGRES_USER` and `POSTGRES_PASSWORD` values must match those in `POSTGRES_URL`.

### Step 3 — Apply the manifests

```bash
# Deploy PostgreSQL (StatefulSet + headless Service)
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/postgres-statefulset.yaml

# Deploy the API (Deployment + ClusterIP Service)
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/deployment.yaml
```

Or apply everything at once (namespace must already exist):

```bash
kubectl apply -f k8s/
```

### Step 4 — Verify pods are running

```bash
kubectl get pods -n spring-security
# NAME                                    READY   STATUS    RESTARTS
# spring-security-api-<hash>-<hash>       1/1     Running   0
# spring-security-api-<hash>-<hash>       1/1     Running   0
# postgres-0                              1/1     Running   0
```

The API pods use a readiness probe on `/api/public/health` with a 20 s initial delay — allow ~30–60 s for the JVM to start.

### Step 5 — Access the API

The service is `ClusterIP` (internal only). Port-forward to reach it locally:

```bash
kubectl port-forward service/spring-security-api 8080:80 -n spring-security
```

```bash
# Public endpoint — no token needed
curl http://localhost:8080/api/public/health

# Protected endpoint — attach a valid Auth0 JWT
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer <your-jwt-token>"
```

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Obtaining a JWT for testing (M2M / client-credentials)

```bash
curl -s --request POST \
  --url "https://<AUTH0_DOMAIN>/oauth/token" \
  --header "content-type: application/json" \
  --data '{
    "client_id":     "<M2M_CLIENT_ID>",
    "client_secret": "<M2M_CLIENT_SECRET>",
    "audience":      "https://spring-api",
    "grant_type":    "client_credentials"
  }' | jq -r '.access_token'
```

### Teardown

```bash
kubectl delete namespace spring-security
```

This removes all resources in the namespace, including PostgreSQL's PersistentVolumeClaim.
