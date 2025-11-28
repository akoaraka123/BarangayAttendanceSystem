# Barangay Attendance System - Deployment Guide

## Problem: "No suitable driver found" Error

If you're getting the error **"No suitable driver found for jdbc:mysql"**, it means the MySQL connector is not included in your JAR file.

## Solution: Use FAT JAR (All-in-One)

### Option 1: Build FAT JAR (Recommended)

1. Run `build_fat_jar.bat` in the project folder
2. This creates `BarangayAttendanceSystem-FAT.jar` with all dependencies included
3. Copy this single JAR file anywhere - it will work!

### Option 2: Use Deployment Package

1. Run `build_jar.bat` in the project folder
2. Copy the entire `deploy` folder (not just the JAR)
3. The `deploy` folder contains:
   - `BarangayAttendanceSystem.jar`
   - `lib/mysql-connector-j-9.5.0.jar` (required!)
   - `run.bat` (startup script)

## Quick Start

### Using FAT JAR:
```bash
java -jar BarangayAttendanceSystem-FAT.jar
```

### Using Deployment Package:
Double-click `run.bat` in the `deploy` folder

## Requirements

- **Java 8 or higher** installed
- **MySQL Server** running on `localhost:3306`
- **Database `attendance_db`** created (use `database_setup.sql`)

## Troubleshooting

### Error: "No suitable driver found"
- **Solution**: Use the FAT JAR (`build_fat_jar.bat`) instead of regular JAR

### Error: "Connection failed"
- Make sure MySQL Server is running
- Make sure database `attendance_db` exists
- Check MySQL user permissions (default: root with no password)

### JAR file won't open
- Make sure Java is installed: `java -version`
- Try running from command line: `java -jar BarangayAttendanceSystem-FAT.jar`

## File Sizes

- **Regular JAR**: ~50 KB (requires lib folder)
- **FAT JAR**: ~2-3 MB (includes all dependencies, standalone)

## Recommendation

**Always use the FAT JAR** (`BarangayAttendanceSystem-FAT.jar`) for deployment - it's easier and more reliable!

