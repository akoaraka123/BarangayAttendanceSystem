@echo off
echo Building Complete Barangay Attendance System JAR...
echo.

REM Check if MySQL connector exists
if not exist "dist\lib\mysql-connector-j-9.5.0.jar" (
    echo ERROR: MySQL Connector not found!
    echo Please download mysql-connector-j-9.5.0.jar and place it in dist\lib folder
    echo Download from: https://dev.mysql.com/downloads/connector/j/
    echo.
    pause
    exit /b 1
)

REM Clean previous builds
if exist "BarangayAttendanceSystem.jar" del "BarangayAttendanceSystem.jar"
if exist "temp" rmdir /s /q "temp"

REM Create temporary directory
mkdir temp
mkdir temp\lib

REM Compile the Java files with UTF-8 encoding
echo Compiling Java files...
javac -encoding UTF-8 -d "temp" -cp "dist\lib\mysql-connector-j-9.5.0.jar" src\attendance\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    rmdir /s /q temp
    pause
    exit /b 1
)

REM Copy MySQL connector to temp
echo Copying dependencies...
copy "dist\lib\mysql-connector-j-9.5.0.jar" "temp\lib\"

REM Extract MySQL connector
echo Extracting MySQL connector...
cd temp\lib
jar -xf mysql-connector-j-9.5.0.jar
cd ..\..

REM Create manifest file
echo Creating manifest...
echo Manifest-Version: 1.0 > manifest.mf
echo Main-Class: attendance.Main >> manifest.mf

REM Create complete JAR
echo Creating complete JAR...
cd temp
jar -cvfm ..\BarangayAttendanceSystem.jar ..\manifest.mf attendance\*.class lib\*
cd ..

REM Clean up
rmdir /s /q temp

if %ERRORLEVEL% neq 0 (
    echo JAR creation failed!
    pause
    exit /b 1
)

REM Create deployment package
echo Creating deployment package...
if not exist "deploy" mkdir deploy
copy "BarangayAttendanceSystem.jar" "deploy\"

REM Create simple run script
echo @echo off > deploy\run_complete.bat
echo echo Starting Barangay Attendance System... >> deploy\run_complete.bat
echo echo (Complete JAR with embedded MySQL driver) >> deploy\run_complete.bat
echo echo. >> deploy\run_complete.bat
echo java -jar BarangayAttendanceSystem.jar >> deploy\run_complete.bat
echo if %%ERRORLEVEL%% neq 0 ( >> deploy\run_complete.bat
echo     echo. >> deploy\run_complete.bat
echo     echo ERROR: Application failed to start! >> deploy\run_complete.bat
echo     echo. >> deploy\run_complete.bat
echo     echo Troubleshooting: >> deploy\run_complete.bat
echo     echo 1. Make sure Java 8 or higher is installed >> deploy\run_complete.bat
echo     echo 2. Make sure MySQL/MariaDB is running on localhost:3306 >> deploy\run_complete.bat
echo     echo 3. Make sure database 'attendance_db' exists >> deploy\run_complete.bat
echo     echo 4. Make sure MySQL user 'root' has proper permissions >> deploy\run_complete.bat
echo     echo. >> deploy\run_complete.bat
echo     echo Press any key to exit... >> deploy\run_complete.bat
echo     pause >> deploy\run_complete.bat
echo     exit /b 1 >> deploy\run_complete.bat
echo ) >> deploy\run_complete.bat

echo.
echo SUCCESS: Complete JAR built!
echo.
echo Created files:
echo - BarangayAttendanceSystem.jar (Complete with embedded MySQL driver)
echo - deploy\run_complete.bat (Simple startup script)
echo.
echo This JAR should work on any computer with Java installed!
echo No external dependencies required.
echo.
pause
