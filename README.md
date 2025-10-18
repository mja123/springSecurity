# Security app with Spring Secuirity
## Description
## Run the project
1. Clone it
2. Create `.env` file following `.env.example`
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
7. In the project, in run options, edit configuration and add `POSTRES_URL` with the value defined in `.env`
8. Run the project.