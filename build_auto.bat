@echo off
echo Barangay Attendance System - Auto Build Script
echo ============================================
echo.

REM Create dist directory
if not exist "dist" mkdir dist
if not exist "dist\lib" mkdir dist\lib

REM Check if MySQL connector exists
if not exist "dist\lib\mysql-connector-j-9.5.0.jar" (
    echo Downloading MySQL Connector/J...
    echo This may take a few minutes...
    
    REM Download MySQL connector
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.5.0/mysql-connector-j-9.5.0.jar' -OutFile 'dist\lib\mysql-connector-j-9.5.0.jar'"
    
    if %ERRORLEVEL% neq 0 (
        echo.
        echo ERROR: Could not download MySQL Connector/J automatically
        echo Please download manually from:
        echo https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.5.0/mysql-connector-j-9.5.0.jar
        echo And save to: dist\lib\mysql-connector-j-9.5.0.jar
        echo.
        pause
        exit /b 1
    )
    
    echo MySQL Connector/J downloaded successfully!
)

REM Compile Java files
echo Compiling Java files...
javac -d "dist" -cp "dist\lib\mysql-connector-j-9.5.0.jar" src\attendance\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Create JAR file
echo Creating JAR file...
echo Manifest-Version: 1.0 > manifest.mf
echo Main-Class: attendance.Main >> manifest.mf
echo Class-Path: lib/mysql-connector-j-9.5.0.jar >> manifest.mf

cd dist
jar -cvfm BarangayAttendanceSystem.jar ..\manifest.mf attendance\*.class
cd ..

if %ERRORLEVEL% neq 0 (
    echo JAR creation failed!
    pause
    exit /b 1
)

echo.
echo ============================================
echo BUILD SUCCESSFUL!
echo ============================================
echo.
echo Your JAR file is ready: dist\BarangayAttendanceSystem.jar
echo.
echo To run the application:
echo   java -jar dist\BarangayAttendanceSystem.jar
echo.
echo Default login: admin@barangay.com / admin123
echo.
pause
