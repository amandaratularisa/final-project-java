@echo off
echo ===================================
echo CLEANUP - DELETE UNUSED FILES
echo ===================================
echo.

echo Deleting files...

del /f /q CharacterShop_JavaFX.txt 2>nul
del /f /q LoginScreen_JavaFX.txt 2>nul
del /f /q MainMenuScreen_JavaFX.txt 2>nul
del /f /q DatabaseManager_FIXED.txt 2>nul
del /f /q GameLevel_FIXED.txt 2>nul
del /f /q Platform_FIXED.txt 2>nul
del /f /q PlayerData_FIXED.txt 2>nul
del /f /q StoryManager_FIXED.txt 2>nul
del /f /q dbmanager_template.txt 2>nul
del /f /q CARA_TAMBAH_GAMBAR_OPENING.md 2>nul
del /f /q DEBUG_IMAGE_LOADING.md 2>nul
del /f /q SISTEM_AUTO_IMAGE.md 2>nul
del /f /q SOLUSI_FINAL.md 2>nul
del /f /q SOLUSI_GAMBAR_TIDAK_MUNCUL.md 2>nul
del /f /q TEST_GAMBAR_SEKARANG.txt 2>nul
del /f /q TEST_SEKARANG.txt 2>nul
del /f /q UPDATE_BACKGROUND_INSTRUCTIONS.md 2>nul
del /f /q UPLOAD_GAMBAR.txt 2>nul
del /f /q LANGKAH_MANUAL.txt 2>nul
del /f /q convert_to_javafx_layouts.ps1 2>nul
del /f /q fix_image_render.ps1 2>nul
del /f /q update_remaining_screens.ps1 2>nul
del /f /q update_screens.ps1 2>nul
del /f /q compress_image.bat 2>nul
del /f /q quick_test.bat 2>nul
del /f /q run_with_log.bat 2>nul
del /f /q test_image_load.bat 2>nul
del /f /q MANUAL_DELETE.bat 2>nul
del /f /q ImageCompressor.java 2>nul
del /f /q out\ImageCompressor.class 2>nul
del /f /q assets\ui\opening_background_BACKUP.png 2>nul

if exist target rmdir /s /q target 2>nul

echo.
echo [OK] Cleanup completed!
echo.
echo Remaining files: build.bat, run.bat, setup_database.bat, README.md, etc.
echo.
pause
