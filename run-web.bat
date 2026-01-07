@echo off
echo ========================================
echo   ABA Banking System - Web Build Script
echo ========================================
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

echo Compiling Java files...
javac -cp "lib/*" -d bin ^
    src/config/*.java ^
    src/model/*.java ^
    src/security/*.java ^
    src/repository/*.java ^
    src/service/*.java ^
    src/web/*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.
echo Starting web server...
echo.

java -cp "bin;lib/*" web.WebServer

pause
