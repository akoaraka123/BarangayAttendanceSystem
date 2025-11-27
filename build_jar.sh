#!/bin/bash

echo "Building Barangay Attendance System JAR with dependencies..."
echo

# Check if MySQL connector exists
if [ ! -f "lib/mysql-connector-j-8.4.0.jar" ]; then
    echo "❌ MySQL Connector not found!"
    echo "🔧 Please download mysql-connector-j-8.4.0.jar and place it in lib folder"
    echo "🔧 Download from: https://dev.mysql.com/downloads/connector/j/"
    echo
    exit 1
fi

# Create lib directory if it doesn't exist
mkdir -p lib

# Clean previous builds
rm -rf dist
rm -f BarangayAttendanceSystem.jar

# Create dist directory
mkdir -p dist

# Compile the Java files
echo "📦 Compiling Java files..."
javac -d "dist" -cp "lib/mysql-connector-j-8.4.0.jar" src/attendance/*.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

# Copy resources if any
if [ -d "src/resources" ]; then
    cp -r "src/resources" "dist/"
fi

# Create JAR file with manifest
echo "📦 Creating JAR file..."
echo "Manifest-Version: 1.0" > manifest.mf
echo "Main-Class: attendance.Main" >> manifest.mf
echo "Class-Path: lib/mysql-connector-j-8.4.0.jar" >> manifest.mf

cd dist
jar -cvfm ../BarangayAttendanceSystem.jar ../manifest.mf attendance/*.class
cd ..

if [ $? -ne 0 ]; then
    echo "❌ JAR creation failed!"
    exit 1
fi

# Create deployment package
echo "📦 Creating deployment package..."
mkdir -p deploy
cp "BarangayAttendanceSystem.jar" "deploy/"
cp -r "lib" "deploy/"

# Create startup script
cat > deploy/run.sh << 'EOF'
#!/bin/bash
echo "Starting Barangay Attendance System..."
java -jar BarangayAttendanceSystem.jar
if [ $? -ne 0 ]; then
    echo
    echo "❌ Application failed to start!"
    echo "🔧 Make sure MySQL is running"
    echo "🔧 Make sure database 'attendance_db' exists"
    read -p "Press Enter to continue..."
fi
EOF

chmod +x deploy/run.sh

# Create README
cat > deploy/README.txt << 'EOF'
Barangay Attendance System - Deployment Package

REQUIREMENTS:
- Java 8 or higher installed
- MySQL Server running on localhost:3306
- Database 'attendance_db' created

SETUP INSTRUCTIONS:
1. Make sure MySQL is installed and running
2. Create database 'attendance_db' using database_setup.sql
3. Run ./run.sh to start the application

TROUBLESHOOTING:
- If you get 'MySQL Driver not found' error:
  * Make sure mysql-connector-j-8.4.0.jar is in lib folder
- If you get 'Connection failed' error:
  * Make sure MySQL server is running
  * Make sure database 'attendance_db' exists
  * Check MySQL user permissions
EOF

echo
echo "✅ Build completed successfully!"
echo "📦 Deployment package created in 'deploy' folder"
echo "📋 Contents:"
echo "  - BarangayAttendanceSystem.jar"
echo "  - lib/mysql-connector-j-8.4.0.jar"
echo "  - run.sh (startup script)"
echo "  - README.txt (instructions)"
echo
echo "🚀 To run: ./deploy/run.sh"
echo "📂 Or copy entire 'deploy' folder to any computer"
echo
