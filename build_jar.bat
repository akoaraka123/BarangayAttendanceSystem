@echo off
echo Building Barangay Attendance System JAR with dependencies...
echo.

REM Check if MySQL connector exists
if not exist "dist\lib\mysql-connector-j-9.5.0.jar" (
    echo ❌ MySQL Connector not found!
    echo 🔧 Please download mysql-connector-j-9.5.0.jar and place it in dist\lib folder
    echo 🔧 Download from: https://dev.mysql.com/downloads/connector/j/
    echo.
    pause
    exit /b 1
)

REM Clean previous builds
if exist "BarangayAttendanceSystem.jar" del "BarangayAttendanceSystem.jar"

REM Compile the Java files
echo 📦 Compiling Java files...
javac -d "dist" -cp "dist\lib\mysql-connector-j-9.5.0.jar" src\attendance\*.java

if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

REM Copy resources if any
if exist "src\resources" xcopy "src\resources" "dist\resources" /E /I /Y

REM Create JAR file with manifest
echo 📦 Creating JAR file...
echo Manifest-Version: 1.0 > manifest.mf
echo Main-Class: attendance.Main >> manifest.mf
echo Class-Path: lib/mysql-connector-j-9.5.0.jar >> manifest.mf

cd dist
jar -cvfm ..\BarangayAttendanceSystem.jar ..\manifest.mf attendance\*.class
cd ..

if %ERRORLEVEL% neq 0 (
    echo ❌ JAR creation failed!
    pause
    exit /b 1
)

REM Create deployment package
echo 📦 Creating deployment package...
if not exist "deploy" mkdir deploy
copy "BarangayAttendanceSystem.jar" "deploy\"
xcopy "dist\lib" "deploy\lib" /E /I /Y

REM Create startup script
echo @echo off > deploy\run.bat
echo echo Starting Barangay Attendance System... >> deploy\run.bat
echo java -jar BarangayAttendanceSystem.jar >> deploy\run.bat
echo if %%ERRORLEVEL%% neq 0 ( >> deploy\run.bat
echo     echo. >> deploy\run.bat
echo     echo ❌ Application failed to start! >> deploy\run.bat
echo     echo 🔧 Make sure MySQL is running >> deploy\run.bat
echo     echo 🔧 Make sure database 'attendance_db' exists >> deploy\run.bat
echo     echo 🔧 Make sure mysql-connector-j-9.5.0.jar is in lib folder >> deploy\run.bat
echo     pause >> deploy\run.bat
echo ) >> deploy\run.bat

REM Create README
echo Barangay Attendance System - Deployment Package > deploy\README.txt
echo. >> deploy\README.txt
echo REQUIREMENTS: >> deploy\README.txt
echo - Java 8 or higher installed >> deploy\README.txt
echo - MySQL Server running on localhost:3306 >> deploy\README.txt
echo - Database 'attendance_db' created >> deploy\README.txt
echo. >> deploy\README.txt
echo SETUP INSTRUCTIONS: >> deploy\README.txt
echo 1. Make sure MySQL is installed and running >> deploy\README.txt
echo 2. Create database 'attendance_db' using database_setup.sql >> deploy\README.txt
echo 3. Double-click run.bat to start the application >> deploy\README.txt
echo. >> deploy\README.txt
echo TROUBLESHOOTING: >> deploy\README.txt
echo - If you get 'MySQL Driver not found' error: >> deploy\README.txt
echo   * Make sure mysql-connector-j-9.5.0.jar is in lib folder >> deploy\README.txt
echo - If you get 'Connection failed' error: >> deploy\README.txt
echo   * Make sure MySQL server is running >> deploy\README.txt
echo   * Make sure database 'attendance_db' exists >> deploy\README.txt
echo   * Check MySQL user permissions >> deploy\README.txt
echo. >> deploy\README.txt
echo PACKAGE CONTENTS: >> deploy\README.txt
echo - BarangayAttendanceSystem.jar (Main application) >> deploy\README.txt
echo - lib/mysql-connector-j-9.5.0.jar (MySQL driver) >> deploy\README.txt
echo - run.bat (Startup script) >> deploy\README.txt

echo.
echo ✅ Build completed successfully!
echo 📦 Deployment package created in 'deploy' folder
echo 📋 Contents:
echo   - BarangayAttendanceSystem.jar
echo   - lib/mysql-connector-j-9.5.0.jar
echo   - run.bat (startup script)
echo   - README.txt (instructions)
echo.
echo 🚀 To run: Double-click deploy\run.bat
echo 📂 Or copy entire 'deploy' folder to any computer
echo.
pause
