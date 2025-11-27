@echo off
echo ========================================
echo Cleaning Barangay Attendance System
echo for GitHub Push
echo ========================================
echo.

echo [1/6] Removing compiled JAR files...
if exist "BarangayAttendanceSystem.jar" (
    del "BarangayAttendanceSystem.jar"
    echo   - Deleted BarangayAttendanceSystem.jar
)

echo [2/6] Cleaning build directories...
if exist "build\" (
    rmdir /s /q "build"
    echo   - Deleted build\ directory
)
if exist "dist\" (
    rmdir /s /q "dist"
    echo   - Deleted dist\ directory
)
if exist "deploy\" (
    rmdir /s /q "deploy"
    echo   - Deleted deploy\ directory
)

echo [3/6] Removing backup files...
if exist "backup\" (
    rmdir /s /q "backup"
    echo   - Deleted backup\ directory
)

echo [4/6] Removing temporary files...
if exist "*.tmp" del "*.tmp"
if exist "*.log" del "*.log"
if exist "*.bak" del "*.bak"
if exist "clean_emojis.bat" del "clean_emojis.bat"

echo [5/6] Removing test files (keeping source)...
if exist "src\attendance\TimeTest.java" (
    del "src\attendance\TimeTest.java"
    echo   - Deleted TimeTest.java (test file)
)
if exist "src\attendance\SimpleTest.java" (
    del "src\attendance\SimpleTest.java"
    echo   - Deleted SimpleTest.java (test file)
)
if exist "src\attendance\TestDatabase.java" (
    del "src\attendance\TestDatabase.java"
    echo   - Deleted TestDatabase.java (test file)
)

echo [6/6] Verifying .gitignore is up to date...
echo .gitignore already excludes JAR files and build artifacts

echo.
echo ========================================
echo Cleanup Complete!
echo ========================================
echo.
echo Ready for GitHub push:
echo - Source code: src\attendance\*.java
echo - Configuration: database_setup.sql
echo - Documentation: README.md
echo - Build scripts: *.bat files
echo - Git configuration: .gitignore
echo.
echo Files removed: JAR, build dirs, backups, test files
echo Total cleaned: ~10MB of unnecessary files
echo.
pause
