# Security app with Spring Security and Auth0
## Description
Spring Boot REST API secured with Auth0 and Spring Security OAuth2 Resource Server. Implements JWT-based authentication and authorization with support for Auth0 permissions and roles.

## Features
- ✅ Auth0 JWT authentication
- ✅ Role-based access control (RBAC)
- ✅ Permission-based authorization
- ✅ Public endpoints (no authentication required)
- ✅ Protected user management endpoints
- ✅ Method-level security with `@PreAuthorize`
- ✅ CORS configuration
- ✅ Security headers

## Auth0 Setup

1. **Create an Auth0 Account** (if you don't have one)
   - Go to [Auth0](https://auth0.com) and sign up

2. **Create an API in Auth0 Dashboard**
   - Navigate to Applications > APIs
   - Click "Create API"
   - Set an identifier (this is your `AUTH0_AUDIENCE`)
   - Note your Auth0 domain (e.g., `your-tenant.auth0.com`)

3. **Configure Roles and Permissions** (Optional but recommended)
   - Go to User Management > Roles
   - Create roles: `ADMIN`, `USER`
   - Go to APIs > Your API > Permissions
   - Create permissions: `read:users`, `write:users`, `delete:users`
   - Assign permissions to roles as needed

4. **Create a Machine to Machine Application** (for API access)
   - Go to Applications > Applications
   - Click "Create Application"
   - Choose "Machine to Machine Applications"
   - Authorize it for your API
   - Grant the necessary permissions

## Environment Configuration

Add the following environment variables to your `.env` file or run configuration:

```bash
# Auth0 Configuration
AUTH0_ISSUER_URI=https://your-tenant.auth0.com/
AUTH0_AUDIENCE=your-api-identifier
AUTH0_ROLES_CLAIM_NAMESPACE=https://your-tenant.auth0.com/roles  # Optional: for custom roles namespace
```

## Run the project
1. Clone it
2. Create `.env` file following `.env.example` and add Auth0 configuration
3. Run `docker compose up -d`
4. Log in to pgadmin and add the server connection:
   - Click in Servers>Register>Server
   - Add a custom name
   - In Connection:
     - Host: the postgres service name in docker compose
     - Port: the postgres service port in docker compose
     - Username: the postgres name in `POSTGRES_USER` in `.env`
     - Password: the postgres password in `POSTGRES_PASSWORD` in `.env`
   - Save
5. Click in Servers>$YOUR_SERVER>DATABASE>security>Schemas>Create>Schema
6. Add `security` schema and click en save
7. In the project, in run options, edit configuration and add:
   - `POSTGRES_URL` with the value defined in `.env`
   - `AUTH0_ISSUER_URI` with your Auth0 issuer URI
   - `AUTH0_AUDIENCE` with your API identifier
   - `AUTH0_ROLES_CLAIM_NAMESPACE` (optional) with your roles namespace
8. Run the project.

## API Endpoints

### Public Endpoints (No Authentication Required)
- `GET /api/public/health` - Health check
- `GET /api/public/info` - API information
- `GET /api/public/welcome` - Welcome message

### Protected Endpoints (Require Auth0 JWT Token)
- `GET /api/users` - Get all users (requires `read:users` permission or `USER`/`ADMIN` role)
- `GET /api/users/{id}` - Get user by ID (requires `read:users` permission or `USER`/`ADMIN` role)
- `POST /api/users` - Create user (requires `write:users` permission or `ADMIN` role)
- `PUT /api/users/{id}` - Update user (requires `write:users` permission or `ADMIN` role)
- `DELETE /api/users/{id}` - Delete user (requires `delete:users` permission or `ADMIN` role)

## Testing with Auth0

To test the API, you'll need to obtain a JWT token from Auth0:

1. Use Auth0's test token endpoint or your M2M application credentials
2. Include the token in the `Authorization` header:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

Example with curl:
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```