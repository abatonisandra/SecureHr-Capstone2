@echo off
echo Fixing PostgreSQL setup for SecureHR...

echo.
echo 1. Creating database and user...
psql -U postgres -c "CREATE DATABASE IF NOT EXISTS employeedb;"
psql -U postgres -c "CREATE USER IF NOT EXISTS postgres WITH PASSWORD 'sandra';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE employeedb TO postgres;"

echo.
echo 2. Testing connection...
psql -h localhost -p 5432 -U postgres -d employeedb -c "SELECT version();"

echo.
echo PostgreSQL setup complete!
pause