@echo off
echo Resetting PostgreSQL password to 'sandra'...

echo.
echo Step 1: Stopping PostgreSQL service...
net stop postgresql-x64-15 2>nul
net stop postgresql-x64-14 2>nul
net stop postgresql-x64-13 2>nul
net stop postgresql-x64-12 2>nul

echo.
echo Step 2: Starting PostgreSQL service...
net start postgresql-x64-15 2>nul
if %errorlevel% neq 0 (
    net start postgresql-x64-14 2>nul
    if %errorlevel% neq 0 (
        net start postgresql-x64-13 2>nul
        if %errorlevel% neq 0 (
            net start postgresql-x64-12 2>nul
        )
    )
)

echo.
echo Step 3: Resetting password using psql...
psql -U postgres -c "ALTER USER postgres PASSWORD 'sandra';" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password reset to 'sandra'
    goto :create_db
)

echo.
echo Step 4: Trying alternative method...
set PGPASSWORD=postgres
psql -U postgres -c "ALTER USER postgres PASSWORD 'sandra';" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password reset to 'sandra'
    goto :create_db
)

echo.
echo Step 5: Trying with no password...
set PGPASSWORD=
psql -U postgres -c "ALTER USER postgres PASSWORD 'sandra';" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Password reset to 'sandra'
    goto :create_db
)

echo.
echo ERROR: Could not reset password. Please reset manually using pgAdmin
pause
exit /b 1

:create_db
echo.
echo Step 6: Creating database...
set PGPASSWORD=sandra
psql -U postgres -c "CREATE DATABASE employeedb;" 2>nul
echo Database creation attempted

echo.
echo PostgreSQL password reset complete!
echo Password is now: sandra
pause