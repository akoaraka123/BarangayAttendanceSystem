# Barangay Attendance System

A comprehensive attendance tracking system developed for barangay (local community) management with modern Java Swing interface and MySQL database backend.

## Features

- **Modern UI Design** with large input fields for easy data entry
- **User Authentication** with Admin and Employee roles
- **Employee Management** - Register, view, edit, and remove employees
- **Attendance Tracking** - Clock IN/OUT functionality with timestamp
- **Export Features** - Export attendance and employee data to CSV
- **Database Management** - MySQL backend with secure connection
- **Logout Functionality** - Secure session management
- **Professional Theme** - Modern, clean interface design

## Quick Start

### Prerequisites
- Java JDK 8 or higher (JDK required, not just JRE)
- MySQL Server 5.7+ or MariaDB 10.2+
- MySQL Connector/J driver

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/BarangayAttendanceSystem.git
   cd BarangayAttendanceSystem
   ```

2. **Download MySQL Connector/J**
   ```bash
   # Create the lib folder
   mkdir dist\lib
   
   # Download from Maven (or manually from mysql.com)
   # Place mysql-connector-j-x.x.x.jar in dist/lib/ folder
   ```
   Download link: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar

3. **Database Setup**
   ```sql
   CREATE DATABASE attendance_db;
   mysql -u root attendance_db < database_setup.sql
   ```

4. **Build the Application**
   ```bash
   # Windows - Creates self-contained JAR with MySQL driver included
   build_fat_jar.bat
   ```

5. **Run the Application**
   ```bash
   java -jar BarangayAttendanceSystem.jar
   ```
   Or just double-click the JAR file!

## Default Login Credentials

- **Admin**: `admin` / `admin123`
- **Employee**: `employee` / `employee123`

## Project Structure

```
BarangayAttendanceSystem/
├── src/attendance/          # Source code
│   ├── Main.java           # Application entry point
│   ├── LoginFrame.java     # Login interface
│   ├── AdminDashboardFrame.java  # Admin dashboard
│   ├── DashboardFrame.java # Employee dashboard
│   ├── DatabaseOperations.java  # Database operations
│   ├── DBConnection.java  # Database connection
│   └── ...                # Other utility classes
├── database_setup.sql      # Database schema and data
├── build_jar.bat          # Windows build script
├── build_jar.sh           # Linux/Mac build script
└── README.md              # This file
```

## Usage

### Admin Features
- Register new employees
- View and edit employee information
- Remove employees
- View attendance logs
- Export data to CSV
- Generate reports
- View statistics

### Employee Features
- Clock IN/OUT functionality
- View attendance history
- Update profile information

## Configuration

### Database Connection
Edit `DBConnection.java` to modify database settings:
```java
private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

## 📦 Build Instructions

### Important Note
**JAR files are excluded from Git** to keep the repository clean. You need to build the JAR file locally after cloning.

### Step 1: Download MySQL Connector
Download the MySQL Connector/J and place it in `dist/lib/` folder:
- Direct link: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
- Or from: https://dev.mysql.com/downloads/connector/j/

### Step 2: Build (Windows)
```bash
# Run the build script - it will auto-detect your Java installation
build_fat_jar.bat
```

This creates a **self-contained JAR** that includes the MySQL driver inside, so you can copy the single JAR file anywhere.

### Step 3: Run the Application
```bash
java -jar BarangayAttendanceSystem.jar
```
Or simply double-click the JAR file!

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure MySQL server is running
   - Check database credentials
   - Verify database exists

2. **Class Not Found Error**
   - Ensure MySQL Connector/J is in classpath
   - Check JAR manifest file

3. **UI Issues**
   - Ensure Java 8+ is installed
   - Check system display settings

## UI Features

- **Large Input Fields** for easy data entry
- **Modern Theme** with professional color scheme
- **Responsive Design** adapts to screen size
- **Intuitive Navigation** with clear buttons
- **Error Handling** with user-friendly messages

## Reports

The system supports exporting:
- Attendance logs with timestamps
- Employee database
- Summary reports with statistics

## Security

- Password-based authentication
- Session management with logout
- Input validation and sanitization
- Secure database connection

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is for educational purposes. Feel free to use and modify for your barangay or community needs.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review the database setup
3. Verify Java and MySQL installation

---

**Developed for barangay management and community service** 🏢s
