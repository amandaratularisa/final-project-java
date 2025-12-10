@echo off
echo ===================================
echo Running Pixel Heroes Game
echo ===================================

set JAVAFX_PATH=C:\javafx-sdk-21.0.9\lib
set MYSQL_JAR=lib\mysql-connector-j-9.5.0.jar

if not exist "%JAVAFX_PATH%" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    echo Please check the path
    pause
    exit /b 1
)

if not exist "%MYSQL_JAR%" (
    echo WARNING: MySQL Connector not found at %MYSQL_JAR%
    echo Database features may not work!
    pause
)

if not exist "out\com\game\mario\Main.class" (
    echo ERROR: Game not compiled yet!
    echo Please run build.bat first
    pause
    exit /b 1
)

echo.
echo Starting game...
echo Working Directory: %CD%
echo.

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.graphics,javafx.media -cp "out;%MYSQL_JAR%" com.game.mario.Main

echo.
echo Game closed.
pause
