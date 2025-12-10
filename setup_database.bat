@echo off
echo ===================================
echo Database Auto-Setup
echo ===================================
echo.
echo Creating database platformer_game...
echo.

mysql -u root -e "CREATE DATABASE IF NOT EXISTS platformer_game;"
mysql -u root platformer_game < database_setup_fixed.sql

echo.
echo Database setup complete!
echo.
pause
