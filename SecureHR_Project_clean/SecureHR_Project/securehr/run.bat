@echo off
set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/postgres
set DB_USERNAME=postgres
set DB_PASSWORD=sandra
set JWT_SECRET=0123456789ABCDEF0123456789ABCDEF
set JWT_EXPIRATION=3600000

echo Starting SecureHR application with PostgreSQL...
.\mvnw.cmd spring-boot:run