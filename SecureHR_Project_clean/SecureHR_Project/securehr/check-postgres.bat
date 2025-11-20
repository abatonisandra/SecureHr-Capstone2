@echo off
echo Checking PostgreSQL status...

echo.
echo 1. Checking if PostgreSQL service is running...
sc query postgresql-x64-15 | find "RUNNING" >nul
if %errorlevel% equ 0 (
    echo PostgreSQL service is RUNNING
) else (
    echo PostgreSQL service is NOT running
    echo Starting PostgreSQL service...
    net start postgresql-x64-15
)

echo.
echo 2. Testing connection with different passwords...

echo Testing password: sandra
set PGPASSWORD=sandra
psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT 1;" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password 'sandra' works!
    echo Creating database...
    psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE IF NOT EXISTS employeedb;" 2>nul
    echo.
    echo Your PostgreSQL is ready! Run your Spring Boot app now.
    pause
    exit /b 0
)

echo Testing password: postgres
set PGPASSWORD=postgres
psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT 1;" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password 'postgres' works!
    echo Updating your .env file...
    echo # Database Configuration > .env.new
    echo SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/employeedb >> .env.new
    echo DB_USERNAME=postgres >> .env.new
    echo DB_PASSWORD=postgres >> .env.new
    echo. >> .env.new
    echo # JWT Configuration >> .env.new
    echo JWT_SECRET=0123456789ABCDEF0123456789ABCDEF >> .env.new
    echo JWT_EXPIRATION=3600000 >> .env.new
    move .env.new .env
    echo Creating database...
    psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE IF NOT EXISTS employeedb;" 2>nul
    echo.
    echo Your .env file updated! Password is now 'postgres'. Run your Spring Boot app now.
    pause
    exit /b 0
)

echo.
echo ERROR: Cannot connect to PostgreSQL with any password
echo Please check your PostgreSQL installation
pause