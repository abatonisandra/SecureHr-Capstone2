# SecureHR – Employee Management API

SecureHR is a Spring Boot based backend service that showcases secure employee management with JWT authentication, role-based authorization, auditing, exception handling, and production-ready tooling for deployment to Render.

## Tech Stack
- Spring Boot 3 (Web, Security, Data JPA, Validation, Actuator)
- PostgreSQL (primary database) & H2 (tests)
- JWT (jjwt) for stateless auth
- Springdoc OpenAPI for live documentation

## Getting Started
1. **Prerequisites**
   - JDK 17+
   - Maven 3.9+
   - PostgreSQL database

2. **Configuration**
   - Copy `application.yml` values to environment variables as needed:
     - `SPRING_DATASOURCE_URL`, `DB_USERNAME`, `DB_PASSWORD`
     - `JWT_SECRET`, `JWT_EXPIRATION`
   - Defaults run locally against `jdbc:postgresql://localhost:5432/securehr`.

3. **Run locally**
   ```bash
   mvn clean spring-boot:run
   ```

4. **API docs**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - OpenAPI spec: `http://localhost:8080/api-docs`

5. **Seeded credentials**
   - Admin user: `admin@securehr.com` / `Admin@123`
   - Update password immediately in production.

## Deployment to Render
1. Provision a PostgreSQL instance (Render or external).
2. Create a Render Web Service from your Git repository.
3. Set environment variables:
   - `SPRING_DATASOURCE_URL`, `DB_USERNAME`, `DB_PASSWORD`
   - `JWT_SECRET` (use a strong random string)
4. Build command: `./mvnw clean package`
5. Start command: `java -jar target/securehr-0.0.1-SNAPSHOT.jar`

## Key Endpoints
| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| POST | `/api/v1/auth/register` | Register a new user | Public |
| POST | `/api/v1/auth/login` | Authenticate and receive JWT | Public |
| GET | `/api/v1/employees` | List employees | ADMIN, HR, MANAGER |
| POST | `/api/v1/employees` | Create employee | ADMIN, HR |
| PUT | `/api/v1/employees/{id}` | Update employee | ADMIN, HR |
| DELETE | `/api/v1/employees/{id}` | Delete employee | ADMIN, HR |

## Testing
```bash
mvn test
```

## Future Enhancements
- Employee self-service portal (view own record only)
- Integration with HRIS/Payroll providers
- Audit trail of updates & soft deletes

