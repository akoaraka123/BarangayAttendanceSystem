@echo off
echo ========================================
echo Cleaning up project files for GitHub
echo ========================================
echo.
echo This will delete:
echo - Build artifacts (build/ folder)
echo - Compiled JAR files (dist/*.jar)
echo - IDE private files (nbproject/private/)
echo - Temporary build files
echo.
set /p confirm="Continue? (Y/N): "
if /i not "%confirm%"=="Y" (
    echo Cleanup cancelled.
    pause
    exit /b 0
)

echo.
echo 🧹 Cleaning up...

REM Delete build folder
if exist "build" (
    echo Deleting build/ folder...
    rmdir /s /q "build"
    echo ✅ build/ deleted
)

REM Delete compiled JAR files (keep lib folder structure)
if exist "dist\BarangayAttendanceSystem.jar" (
    echo Deleting dist\BarangayAttendanceSystem.jar...
    del /q "dist\BarangayAttendanceSystem.jar"
    echo ✅ JAR deleted
)

if exist "BarangayAttendanceSystem-FAT.jar" (
    echo Deleting BarangayAttendanceSystem-FAT.jar...
    del /q "BarangayAttendanceSystem-FAT.jar"
    echo ✅ FAT JAR deleted
)

REM Delete IDE private files
if exist "nbproject\private" (
    echo Deleting nbproject\private/ folder...
    rmdir /s /q "nbproject\private"
    echo ✅ nbproject/private/ deleted
)

REM Delete deploy folder if exists
if exist "deploy" (
    echo Deleting deploy/ folder...
    rmdir /s /q "deploy"
    echo ✅ deploy/ deleted
)

REM Delete temporary build folders
if exist "temp_build" (
    echo Deleting temp_build/ folder...
    rmdir /s /q "temp_build"
    echo ✅ temp_build/ deleted
)

REM Delete log files
for %%f in (*.log) do (
    echo Deleting %%f...
    del /q "%%f"
    echo ✅ %%f deleted
)

REM Delete temporary manifest files
if exist "temp_manifest.mf" (
    echo Deleting temp_manifest.mf...
    del /q "temp_manifest.mf"
    echo ✅ temp_manifest.mf deleted
)

echo.
echo ========================================
echo ✅ Cleanup completed!
echo ========================================
echo.
echo 📋 Files kept (important):
echo    - Source files (src/)
echo    - SQL files (database_setup.sql, etc.)
echo    - Build scripts (.bat files)
echo    - README files
echo    - manifest.mf
echo    - nbproject/ (for NetBeans)
echo.
echo 📋 Files deleted:
echo    - Build artifacts
echo    - Compiled JAR files
echo    - IDE private configs
echo    - Temporary files
echo.
echo 💡 Note: MySQL connector JAR in dist/lib/ is kept
echo    (users need to download it separately for building)
echo.
pause

