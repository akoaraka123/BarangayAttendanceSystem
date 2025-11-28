@echo off
title Barangay Attendance System
echo ========================================
echo Barangay Attendance System
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Java is not installed or not in PATH!
    echo.
    echo Please install Java 8 or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

REM Check if FAT JAR exists
if not exist "BarangayAttendanceSystem-FAT.jar" (
    echo ❌ BarangayAttendanceSystem-FAT.jar not found!
    echo.
    echo Please run build_fat_jar.bat first to create the FAT JAR.
    echo.
    pause
    exit /b 1
)

echo ✅ Starting application...
echo.

REM Run the FAT JAR
java -jar BarangayAttendanceSystem-FAT.jar

REM Check exit code
if %ERRORLEVEL% neq 0 (
    echo.
    echo ❌ Application exited with error code: %ERRORLEVEL%
    echo.
    echo Troubleshooting:
    echo 1. Make sure MySQL Server is running
    echo 2. Make sure database 'attendance_db' exists
    echo 3. Check MySQL connection settings in DBConnection.java
    echo.
    pause
)

