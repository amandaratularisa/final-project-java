@echo off
echo ========================================
echo Pixel Heroes - Quick Start
echo ========================================
echo.

set JAVAFX_PATH=C:\javafx-sdk-21.0.9\lib

if not exist "%JAVAFX_PATH%" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    echo Please check the path and try again
    pause
    exit /b 1
)

echo Step 1: Creating output directory...
if not exist "out" mkdir out

echo Step 2: Compiling Java files...
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.graphics,javafx.media -d out src\main\java\com\game\mario\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 3: Starting game...
echo ========================================
echo.

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.graphics,javafx.media -cp out com.game.mario.Main

echo.
echo ========================================
echo Game closed. Thanks for playing!
echo ========================================
pause
