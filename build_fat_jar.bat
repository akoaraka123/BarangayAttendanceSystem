@echo off
setlocal EnableDelayedExpansion
echo ============================================
echo Building Self-Contained JAR (Fat JAR)
echo ============================================
echo.

REM ==========================================
REM PORTABLE JAVA DETECTION
REM Works on any computer with Java installed
REM ==========================================

set "JAR_CMD="
set "JAVAC_CMD="

REM Option 1: Check if JAVA_HOME is set
if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\jar.exe" (
        set "JAR_CMD=%JAVA_HOME%\bin\jar.exe"
        set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
        echo Found Java via JAVA_HOME: %JAVA_HOME%
        goto :java_found
    )
)

REM Option 2: Try to find java in PATH and derive JDK location
for /f "tokens=*" %%i in ('where javac 2^>nul') do (
    set "JAVAC_CMD=%%i"
    for %%j in ("%%~dpi..") do set "JAVA_HOME=%%~fj"
    set "JAR_CMD=!JAVA_HOME!\bin\jar.exe"
    echo Found Java via PATH: !JAVA_HOME!
    goto :java_found
)

REM Option 3: Search common installation directories
for %%d in (
    "C:\Program Files\Java\jdk*"
    "C:\Program Files\Eclipse Adoptium\jdk*"
    "C:\Program Files\Microsoft\jdk*"
    "C:\Program Files\Amazon Corretto\jdk*"
    "C:\Program Files\Zulu\zulu*"
    "C:\Program Files\Apache NetBeans\jdk"
) do (
    for /d %%j in (%%d) do (
        if exist "%%j\bin\jar.exe" (
            set "JAVA_HOME=%%j"
            set "JAR_CMD=%%j\bin\jar.exe"
            set "JAVAC_CMD=%%j\bin\javac.exe"
            echo Found Java at: %%j
            goto :java_found
        )
    )
)

REM Java not found
echo.
echo ============================================
echo ERROR: Java JDK not found!
echo ============================================
echo.
echo Please install Java JDK and either:
echo 1. Set JAVA_HOME environment variable
echo 2. Add Java bin folder to PATH
echo.
echo Download JDK from:
echo - https://adoptium.net/
echo - https://www.oracle.com/java/technologies/downloads/
echo.
pause
exit /b 1

:java_found
echo Using: %JAVAC_CMD%
echo.

REM Create directories
if not exist "dist\lib" mkdir dist\lib

REM ==========================================
REM FIND MySQL CONNECTOR
REM Looks in dist\lib folder (relative path)
REM ==========================================

set "MYSQL_JAR="
for %%f in (dist\lib\mysql-connector*.jar) do set "MYSQL_JAR=%%f"

if not defined MYSQL_JAR (
    echo.
    echo ============================================
    echo MySQL Connector NOT FOUND!
    echo ============================================
    echo.
    echo Please download MySQL Connector/J:
    echo.
    echo 1. Go to: https://dev.mysql.com/downloads/connector/j/
    echo 2. Select "Platform Independent"
    echo 3. Download the ZIP file
    echo 4. Extract the .jar file to: dist\lib\
    echo.
    echo Or download directly from Maven:
    echo https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
    echo.
    pause
    exit /b 1
)

echo Found MySQL Connector: !MYSQL_JAR!

REM Clean temp folder
if exist "temp_build" rmdir /s /q temp_build
mkdir temp_build

REM Compile Java files
echo.
echo [1/4] Compiling Java files...
"%JAVAC_CMD%" -d "temp_build" -cp "!MYSQL_JAR!" src\attendance\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!

REM Extract MySQL connector JAR
echo.
echo [2/4] Extracting MySQL connector...
cd temp_build
"%JAR_CMD%" -xf "..\!MYSQL_JAR!"
cd ..
echo Extraction successful!

REM Remove signature files (they cause issues in fat JARs)
echo.
echo [3/4] Cleaning up signatures...
if exist "temp_build\META-INF\*.SF" del "temp_build\META-INF\*.SF" 2>nul
if exist "temp_build\META-INF\*.DSA" del "temp_build\META-INF\*.DSA" 2>nul
if exist "temp_build\META-INF\*.RSA" del "temp_build\META-INF\*.RSA" 2>nul
echo Cleanup successful!

REM Create manifest for fat JAR
echo Manifest-Version: 1.0> temp_manifest.mf
echo Main-Class: attendance.Main>> temp_manifest.mf

REM Create the fat JAR
echo.
echo [4/4] Creating Fat JAR...
cd temp_build
"%JAR_CMD%" -cvfm "..\BarangayAttendanceSystem.jar" "..\temp_manifest.mf" .
cd ..

if %ERRORLEVEL% neq 0 (
    echo JAR creation failed!
    pause
    exit /b 1
)

REM Copy to dist folder as well
copy "BarangayAttendanceSystem.jar" "dist\" /Y

REM Clean up temp files
rmdir /s /q temp_build
del temp_manifest.mf

echo.
echo ============================================
echo BUILD SUCCESSFUL!
echo ============================================
echo.
echo Output: BarangayAttendanceSystem.jar
echo.
echo This JAR is SELF-CONTAINED and includes
echo the MySQL driver inside it.
echo.
echo You can copy this single JAR file anywhere
echo and run it with: java -jar BarangayAttendanceSystem.jar
echo.
echo Or just double-click it if Java is installed!
echo.
pause
