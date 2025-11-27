@echo off
echo Building Barangay Attendance System JAR...
echo.

REM Check if MySQL connector exists
if not exist "dist\lib\mysql-connector-j-9.5.0.jar" (
    echo MySQL Connector not found!
    echo Please download mysql-connector-j-9.5.0.jar and place it in dist\lib folder
    echo Download from: https://dev.mysql.com/downloads/connector/j/
    echo.
    pause
    exit /b 1
)

REM Clean previous builds
if exist "BarangayAttendanceSystem.jar" del "BarangayAttendanceSystem.jar"

REM Compile the Java files with UTF-8 encoding
echo Compiling Java files...
javac -encoding UTF-8 -d "dist" -cp "dist\lib\mysql-connector-j-9.5.0.jar" src\attendance\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Create JAR file with manifest
echo Creating JAR file...
echo Manifest-Version: 1.0 > manifest.mf
echo Main-Class: attendance.Main >> manifest.mf
echo Class-Path: lib/mysql-connector-j-9.5.0.jar >> manifest.mf

cd dist
jar -cvfm ..\BarangayAttendanceSystem.jar ..\manifest.mf attendance\*.class
cd ..

if %ERRORLEVEL% neq 0 (
    echo JAR creation failed!
    pause
    exit /b 1
)

REM Create deployment package
echo Creating deployment package...
if not exist "deploy" mkdir deploy
copy "BarangayAttendanceSystem.jar" "deploy\"
xcopy "dist\lib" "deploy\lib" /E /I /Y

REM Create startup script
echo @echo off > deploy\run.bat
echo echo Starting Barangay Attendance System... >> deploy\run.bat
echo java -jar BarangayAttendanceSystem.jar >> deploy\run.bat
echo if %%ERRORLEVEL%% neq 0 ( >> deploy\run.bat
echo     echo. >> deploy\run.bat
echo     echo Application failed to start! >> deploy\run.bat
echo     echo Make sure MySQL is running >> deploy\run.bat
echo     echo Make sure database 'attendance_db' exists >> deploy\run.bat
echo     echo Make sure mysql-connector-j-9.5.0.jar is in lib folder >> deploy\run.bat
echo     pause >> deploy\run.bat
echo ) >> deploy\run.bat

echo.
echo Build completed successfully!
echo Deployment package created in 'deploy' folder
echo.
echo To run: Double-click deploy\run.bat
echo Or copy entire 'deploy' folder to any computer
echo.
pause
