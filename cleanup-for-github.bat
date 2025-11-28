@echo off
echo ========================================
echo Cleaning Project for GitHub Push
echo ========================================
echo.
echo This script will delete:
echo   - Build artifacts (build/ folder)
echo   - Compiled JAR files
echo   - IDE private files
echo   - Temporary files
echo.
pause

echo.
echo 🧹 Starting cleanup...
echo.

REM Delete build folder
echo [1/6] Deleting build/ folder...
if exist "build" (
    rd /s /q "build" 2>nul
    if exist "build" (
        echo    ⚠️  Could not delete build/ - may be in use
    ) else (
        echo    ✅ build/ deleted
    )
) else (
    echo    ℹ️  build/ does not exist
)

REM Delete JAR files
echo [2/6] Deleting JAR files...
if exist "dist\BarangayAttendanceSystem.jar" (
    del /f /q "dist\BarangayAttendanceSystem.jar" 2>nul
    echo    ✅ dist\BarangayAttendanceSystem.jar deleted
)
if exist "BarangayAttendanceSystem-FAT.jar" (
    del /f /q "BarangayAttendanceSystem-FAT.jar" 2>nul
    echo    ✅ BarangayAttendanceSystem-FAT.jar deleted
)

REM Delete IDE private files
echo [3/6] Deleting IDE private files...
if exist "nbproject\private" (
    rd /s /q "nbproject\private" 2>nul
    if exist "nbproject\private" (
        echo    ⚠️  Could not delete nbproject\private/ - may be in use
    ) else (
        echo    ✅ nbproject\private/ deleted
    )
) else (
    echo    ℹ️  nbproject\private/ does not exist
)

REM Delete deploy folder
echo [4/6] Deleting deploy/ folder...
if exist "deploy" (
    rd /s /q "deploy" 2>nul
    echo    ✅ deploy/ deleted
) else (
    echo    ℹ️  deploy/ does not exist
)

REM Delete temporary files
echo [5/6] Deleting temporary files...
if exist "temp_build" rd /s /q "temp_build" 2>nul
if exist "temp_manifest.mf" del /f /q "temp_manifest.mf" 2>nul
for %%f in (*.log) do del /f /q "%%f" 2>nul
echo    ✅ Temporary files deleted

REM Delete dist README (build artifact)
echo [6/6] Cleaning dist/ folder...
if exist "dist\README.TXT" (
    del /f /q "dist\README.TXT" 2>nul
    echo    ✅ dist\README.TXT deleted
)

echo.
echo ========================================
echo ✅ Cleanup Complete!
echo ========================================
echo.
echo 📋 Summary:
echo    ✅ Build artifacts removed
echo    ✅ JAR files removed
echo    ✅ IDE private files removed
echo.
echo 📋 Files kept (important):
echo    ✅ Source code (src/)
echo    ✅ SQL files (database_setup.sql)
echo    ✅ Build scripts (.bat files)
echo    ✅ README files
echo    ✅ manifest.mf
echo    ✅ .gitignore (updated)
echo.
echo 💡 Note: MySQL connector in dist/lib/ is kept
echo    (users need to download it for building)
echo.
echo 🚀 Ready to push to GitHub!
echo.
pause

