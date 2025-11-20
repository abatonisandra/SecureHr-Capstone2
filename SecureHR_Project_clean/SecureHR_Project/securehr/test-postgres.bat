@echo off
echo Testing PostgreSQL connection...

echo.
echo Testing with password: sandra
psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT version();" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password 'sandra' works!
    goto :create_db
) else (
    echo FAILED: Password 'sandra' doesn't work
)

echo.
echo Testing with password: postgres
set PGPASSWORD=postgres
psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT version();" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password 'postgres' works!
    set DB_PASSWORD=postgres
    goto :create_db
) else (
    echo FAILED: Password 'postgres' doesn't work
)

echo.
echo Testing with no password
set PGPASSWORD=
psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT version();" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: No password works!
    set DB_PASSWORD=
    goto :create_db
) else (
    echo FAILED: No password doesn't work
)

echo.
echo ERROR: Cannot connect to PostgreSQL with any common passwords
echo Please check your PostgreSQL installation and password
pause
exit /b 1

:create_db
echo.
echo Creating database if it doesn't exist...
psql -h localhost -p 5432 -U postgres -d postgres -c "CREATE DATABASE employeedb;" 2>nul
echo Database creation attempted (may already exist)

echo.
echo PostgreSQL connection test complete!
echo Use password: %DB_PASSWORD%
pause