@echo off
echo ===================================
echo Building Pixel Heroes Game
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

echo.
echo Step 1: Creating output directory...
if not exist "out" mkdir out

echo Step 2: Compiling Java files...
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.graphics,javafx.media -cp "%MYSQL_JAR%" -d out src\main\java\com\game\mario\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 3: Copying resources...
if exist "src\main\resources" (
    xcopy /E /Y /I "src\main\resources\*" "out\" >nul
)

echo.
echo ===================================
echo Build successful!
echo ===================================
echo.
echo To run the game, use: run.bat
echo.
pause
