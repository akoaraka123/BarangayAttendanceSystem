@echo off
echo ========================================
echo Building FAT JAR (All-in-One)
echo ========================================
echo.
echo This will create a single JAR file with all dependencies included.
echo You can copy this JAR anywhere and it will work!
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
echo 🧹 Cleaning previous builds...
if exist "BarangayAttendanceSystem-FAT.jar" del "BarangayAttendanceSystem-FAT.jar"
if exist "build\temp_jar" rmdir /s /q "build\temp_jar"
if exist "build\classes" rmdir /s /q "build\classes"

REM Create directories
mkdir build\classes
mkdir build\temp_jar

REM Compile the Java files
echo 📦 Compiling Java files...
javac -d "build\classes" -cp "dist\lib\mysql-connector-j-9.5.0.jar" src\attendance\*.java

if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

REM Copy compiled classes
echo 📦 Copying compiled classes...
xcopy /E /I /Y "build\classes\attendance" "build\temp_jar\attendance"

REM Extract MySQL connector JAR into temp directory
echo 📦 Extracting MySQL Connector JAR...
cd build\temp_jar
jar -xf ..\..\dist\lib\mysql-connector-j-9.5.0.jar
cd ..\..

REM Remove duplicate META-INF files (keep only one MANIFEST.MF)
if exist "build\temp_jar\META-INF\MANIFEST.MF" del "build\temp_jar\META-INF\MANIFEST.MF"
if exist "build\temp_jar\META-INF\services\java.sql.Driver" (
    echo ✅ Found MySQL Driver service file
)

REM Create new manifest file
echo 📝 Creating manifest file...
if not exist "build\temp_jar\META-INF" mkdir "build\temp_jar\META-INF"
echo Manifest-Version: 1.0 > "build\temp_jar\META-INF\MANIFEST.MF"
echo Main-Class: attendance.Main >> "build\temp_jar\META-INF\MANIFEST.MF"
echo. >> "build\temp_jar\META-INF\MANIFEST.MF"

REM Create FAT JAR
echo 📦 Creating FAT JAR file...
cd build\temp_jar
jar -cvfm ..\..\BarangayAttendanceSystem-FAT.jar META-INF\MANIFEST.MF * >nul 2>&1
cd ..\..

REM Cleanup
echo 🧹 Cleaning up temporary files...
rmdir /s /q "build\temp_jar"

if not exist "BarangayAttendanceSystem-FAT.jar" (
    echo ❌ FAT JAR creation failed!
    pause
    exit /b 1
)

REM Get file size
for %%A in ("BarangayAttendanceSystem-FAT.jar") do set SIZE=%%~zA
set /a SIZE_MB=%SIZE% / 1048576

echo.
echo ========================================
echo ✅ FAT JAR created successfully!
echo ========================================
echo.
echo 📦 File: BarangayAttendanceSystem-FAT.jar (~%SIZE_MB% MB)
echo.
echo 📋 How to use:
echo    1. Copy BarangayAttendanceSystem-FAT.jar to any location
echo    2. Double-click it or run: java -jar BarangayAttendanceSystem-FAT.jar
echo.
echo ⚠️  Requirements:
echo    - Java 8 or higher installed
echo    - MySQL Server running on localhost:3306
echo    - Database 'attendance_db' created
echo.
echo 💡 This JAR includes all dependencies - no need for lib folder!
echo.
pause
