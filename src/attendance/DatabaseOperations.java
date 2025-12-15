/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package attendance;

/**
 *
 * @author Admin
 */
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {
    
    // ===== USER OPERATIONS =====
    public static boolean authenticateUser(String email, String password) {
        System.out.println("DEBUG: Attempting login for username/email: " + email);
        System.out.println("DEBUG: Password length: " + password.length());
        
        // Map usernames to database emails
        String dbEmail = email;
        if (email.equalsIgnoreCase("admin")) {
            dbEmail = "admin@barangay.com";
        } else if (email.equalsIgnoreCase("employee")) {
            dbEmail = "employee@barangay.com";
        }
        
        String sql = "SELECT password, role FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, dbEmail);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");
                System.out.println("DEBUG: User found in database");
                System.out.println("DEBUG: Stored password: " + storedPassword);
                System.out.println("DEBUG: User role: " + role);
                
                // Temporary: Direct comparison for plain text
                boolean passwordMatch = password.equals(storedPassword);
                System.out.println("DEBUG: Password match: " + passwordMatch);
                
                return passwordMatch;
            } else {
                System.out.println("DEBUG: No user found with email: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean createUser(String email, String password, String role) {
        // Check if user already exists
        if (userExists(email)) {
            return false;
        }
        
        // Validate password strength
        if (!PasswordUtils.isPasswordStrong(password)) {
            return false;
        }
        
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, email);
            pst.setString(2, PasswordUtils.hashPassword(password));
            pst.setString(3, role);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean userExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean changePassword(String email, String oldPassword, String newPassword) {
        // Verify old password first
        if (!authenticateUser(email, oldPassword)) {
            return false;
        }
        
        // Validate new password
        if (!PasswordUtils.isPasswordStrong(newPassword)) {
            return false;
        }
        
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, PasswordUtils.hashPassword(newPassword));
            pst.setString(2, email);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
    
    public static String getUserRole(String email) {
        // Map usernames to database emails
        String dbEmail = email;
        if (email.equalsIgnoreCase("admin")) {
            dbEmail = "admin@barangay.com";
        } else if (email.equalsIgnoreCase("employee")) {
            dbEmail = "employee@barangay.com";
        }
        
        String sql = "SELECT role FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, dbEmail);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user role: " + e.getMessage());
        }
        return null;
    }
    
    // ===== EMPLOYEE OPERATIONS =====
    public static boolean registerEmployee(String empId, String fullName, String contact, String address, String position) {
        // Auto-generate RFID Card ID
        String rfidCardId = generateUniqueRFIDCardId();
        return registerEmployee(empId, fullName, contact, address, position, rfidCardId);
    }
    
    public static boolean registerEmployee(String empId, String fullName, String contact, String address, String position, String rfidCardId) {
        String sql = "INSERT INTO employees (employee_id, full_name, contact_number, address, position, rfid_card_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            pst.setString(2, fullName);
            pst.setString(3, contact);
            pst.setString(4, address);
            pst.setString(5, position);
            pst.setString(6, rfidCardId != null && !rfidCardId.trim().isEmpty() ? rfidCardId.trim().toUpperCase() : null);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error registering employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate unique RFID Card ID na hindi pa naka-register
     */
    private static String generateUniqueRFIDCardId() {
        String rfidCardId;
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            // Generate RFID Card ID
            rfidCardId = RFIDCardGenerator.generateShortRFIDCardId();
            attempts++;
            
            // Check if already exists
            if (!isRFIDCardRegistered(rfidCardId)) {
                return rfidCardId;
            }
            
            // If max attempts reached, use timestamp-based ID
            if (attempts >= maxAttempts) {
                rfidCardId = RFIDCardGenerator.generateRFIDCardId();
                break;
            }
        } while (attempts < maxAttempts);
        
        return rfidCardId;
    }
    
    /**
     * Generate unique RFID Card ID for employee (public method)
     */
    public static String generateUniqueRFIDCardIdForEmployee(String empId) {
        String rfidCardId;
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            // Generate RFID Card ID based on employee ID
            rfidCardId = RFIDCardGenerator.generateRFIDCardId(empId);
            attempts++;
            
            // Check if already exists
            if (!isRFIDCardRegistered(rfidCardId)) {
                return rfidCardId.toUpperCase();
            }
            
            // If max attempts reached, use timestamp-based ID
            if (attempts >= maxAttempts) {
                rfidCardId = RFIDCardGenerator.generateRFIDCardId();
                break;
            }
        } while (attempts < maxAttempts);
        
        return rfidCardId.toUpperCase();
    }
    
    public static boolean isEmployeeRegistered(String empId, String fullName) {
        String sql = "SELECT * FROM employees WHERE employee_id = ? AND full_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            pst.setString(2, fullName);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking employee: " + e.getMessage());
            return false;
        }
    }
    
    public static String getEmployeeName(String empId) {
        String sql = "SELECT full_name FROM employees WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee name: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get employee ID by RFID card ID
     */
    public static String getEmployeeIdByRFID(String rfidCardId) {
        String sql = "SELECT employee_id FROM employees WHERE rfid_card_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, rfidCardId.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("employee_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee ID by RFID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get employee name by RFID card ID
     */
    public static String getEmployeeNameByRFID(String rfidCardId) {
        String sql = "SELECT full_name FROM employees WHERE rfid_card_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, rfidCardId.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee name by RFID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Check if RFID card is already registered
     */
    public static boolean isRFIDCardRegistered(String rfidCardId) {
        String sql = "SELECT id FROM employees WHERE rfid_card_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, rfidCardId.trim());
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking RFID card: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update RFID card ID for an employee
     */
    public static boolean updateEmployeeRFID(String empId, String rfidCardId) {
        String sql = "UPDATE employees SET rfid_card_id = ? WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, rfidCardId != null && !rfidCardId.trim().isEmpty() ? rfidCardId.trim() : null);
            pst.setString(2, empId);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee RFID: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        // Use SELECT * and check columns dynamically
        String sql = "SELECT * FROM employees ORDER BY full_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            // Check if rfid_card_id column exists using ResultSetMetaData
            ResultSetMetaData metaData = rs.getMetaData();
            boolean hasRfidColumn = false;
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if ("rfid_card_id".equalsIgnoreCase(metaData.getColumnName(i))) {
                    hasRfidColumn = true;
                    break;
                }
            }
            
            while (rs.next()) {
                String rfidCardId = null;
                if (hasRfidColumn) {
                    rfidCardId = rs.getString("rfid_card_id");
                }
                
                Employee emp = new Employee(
                    rs.getString("employee_id"),
                    rs.getString("full_name"),
                    rs.getString("contact_number"),
                    rs.getString("address"),
                    rs.getString("position"),
                    rfidCardId
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error getting employees: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
        }
        return employees;
    }
    
    public static boolean updateEmployee(String empId, String fullName, String contact, String address, String position) {
        return updateEmployee(empId, fullName, contact, address, position, null);
    }
    
    public static boolean updateEmployee(String empId, String fullName, String contact, String address, String position, String rfidCardId) {
        String sql = "UPDATE employees SET full_name = ?, contact_number = ?, address = ?, position = ?, rfid_card_id = ? WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, fullName);
            pst.setString(2, contact);
            pst.setString(3, address);
            pst.setString(4, position);
            pst.setString(5, rfidCardId != null && !rfidCardId.trim().isEmpty() ? rfidCardId.trim() : null);
            pst.setString(6, empId);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean removeEmployee(String empId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // First, delete all attendance logs for this employee
                String deleteLogsSql = "DELETE FROM attendance_logs WHERE employee_id = ?";
                try (PreparedStatement logsPst = conn.prepareStatement(deleteLogsSql)) {
                    logsPst.setString(1, empId);
                    logsPst.executeUpdate();
                }
                
                // Then, delete the employee
                String deleteEmpSql = "DELETE FROM employees WHERE employee_id = ?";
                try (PreparedStatement empPst = conn.prepareStatement(deleteEmpSql)) {
                    empPst.setString(1, empId);
                    int result = empPst.executeUpdate();
                    
                    // Commit transaction
                    conn.commit();
                    return result > 0;
                }
            } catch (SQLException e) {
                // Rollback on error
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error removing employee: " + e.getMessage());
            return false;
        }
    }
    
    // ===== ATTENDANCE OPERATIONS =====
    public static String getTodayAction(String empId, String name) {
        String today = LocalDate.now().toString();
        LocalTime currentTime = LocalTime.now();
        String sql = "SELECT morning_clock_in, morning_clock_out, afternoon_clock_in, afternoon_clock_out " +
                     "FROM attendance_logs WHERE employee_id = ? AND log_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            pst.setString(2, today);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Time morningIn = rs.getTime("morning_clock_in");
                Time morningOut = rs.getTime("morning_clock_out");
                Time afternoonIn = rs.getTime("afternoon_clock_in");
                Time afternoonOut = rs.getTime("afternoon_clock_out");
                
                // Check morning attendance
                if (morningIn == null) {
                    return "MORNING_IN";  // Need to clock in for morning
                }
                if (morningIn != null && morningOut == null) {
                    return "MORNING_OUT";  // Need to clock out for morning
                }
                
                // Check afternoon attendance (after 12pm)
                if (morningOut != null && afternoonIn == null) {
                    // Check if it's after 12pm (break time is 12pm-1pm)
                    if (currentTime.isAfter(LocalTime.of(12, 0))) {
                        return "AFTERNOON_IN";  // Need to clock in for afternoon
                    } else {
                        return "WAIT_FOR_BREAK";  // Still in morning, wait for break time
                    }
                }
                if (afternoonIn != null && afternoonOut == null) {
                    return "AFTERNOON_OUT";  // Need to clock out for afternoon
                }
                
                // All attendance completed
                if (morningIn != null && morningOut != null && afternoonIn != null && afternoonOut != null) {
                    return "ALL_DONE";
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking today's action: " + e.getMessage());
        }
        return "MORNING_IN";  // No record for today, start with morning IN
    }
    
    public static boolean saveAttendanceLog(String empId, String name, String action) {
        String today = LocalDate.now().toString();
        LocalTime now = LocalTime.now();
        
        // Determine which attendance period based on current action status
        String currentStatus = getTodayAction(empId, name);
        
        try (Connection conn = DBConnection.getConnection()) {
            // Check if log exists for today
            String checkSql = "SELECT id, morning_clock_in, morning_clock_out, afternoon_clock_in, afternoon_clock_out " +
                             "FROM attendance_logs WHERE employee_id = ? AND log_date = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, empId);
            checkPst.setString(2, today);
            ResultSet rs = checkPst.executeQuery();
            
            if (rs.next()) {
                // Update existing log based on current status
                String updateSql = "";
                Time timeValue = Time.valueOf(now);
                
                if (currentStatus.equals("MORNING_IN")) {
                    updateSql = "UPDATE attendance_logs SET morning_clock_in = ? WHERE employee_id = ? AND log_date = ?";
                } else if (currentStatus.equals("MORNING_OUT")) {
                    updateSql = "UPDATE attendance_logs SET morning_clock_out = ? WHERE employee_id = ? AND log_date = ?";
                } else if (currentStatus.equals("AFTERNOON_IN")) {
                    updateSql = "UPDATE attendance_logs SET afternoon_clock_in = ? WHERE employee_id = ? AND log_date = ?";
                } else if (currentStatus.equals("AFTERNOON_OUT")) {
                    updateSql = "UPDATE attendance_logs SET afternoon_clock_out = ? WHERE employee_id = ? AND log_date = ?";
                } else {
                    // Invalid status - shouldn't happen
                    return false;
                }
                
                PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setTime(1, timeValue);
                updatePst.setString(2, empId);
                updatePst.setString(3, today);
                
                int result = updatePst.executeUpdate();
                return result > 0;
            } else {
                // Insert new log - determine which field to set based on action
                String insertSql = "INSERT INTO attendance_logs (employee_id, employee_name, " +
                                  "morning_clock_in, morning_clock_out, afternoon_clock_in, afternoon_clock_out, log_date) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertPst = conn.prepareStatement(insertSql);
                
                insertPst.setString(1, empId);
                insertPst.setString(2, name);
                
                // Set the appropriate field based on current status
                if (currentStatus.equals("MORNING_IN")) {
                    insertPst.setTime(3, Time.valueOf(now));  // morning_clock_in
                    insertPst.setTime(4, null);
                    insertPst.setTime(5, null);
                    insertPst.setTime(6, null);
                } else if (currentStatus.equals("MORNING_OUT")) {
                    insertPst.setTime(3, null);
                    insertPst.setTime(4, Time.valueOf(now));  // morning_clock_out
                    insertPst.setTime(5, null);
                    insertPst.setTime(6, null);
                } else if (currentStatus.equals("AFTERNOON_IN")) {
                    insertPst.setTime(3, null);
                    insertPst.setTime(4, null);
                    insertPst.setTime(5, Time.valueOf(now));  // afternoon_clock_in
                    insertPst.setTime(6, null);
                } else if (currentStatus.equals("AFTERNOON_OUT")) {
                    insertPst.setTime(3, null);
                    insertPst.setTime(4, null);
                    insertPst.setTime(5, null);
                    insertPst.setTime(6, Time.valueOf(now));  // afternoon_clock_out
                } else {
                    // Default to morning IN
                    insertPst.setTime(3, Time.valueOf(now));
                    insertPst.setTime(4, null);
                    insertPst.setTime(5, null);
                    insertPst.setTime(6, null);
                }
                
                insertPst.setString(7, today);
                
                int result = insertPst.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saving attendance log: " + e.getMessage());
            return false;
        }
    }
    
    public static List<AttendanceLog> getAllAttendanceLogs() {
        List<AttendanceLog> logs = new ArrayList<>();
        String sql = "SELECT id, employee_id, employee_name, " +
                     "morning_clock_in, morning_clock_out, " +
                     "afternoon_clock_in, afternoon_clock_out, " +
                     "log_date FROM attendance_logs ORDER BY log_date DESC, employee_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                AttendanceLog log = new AttendanceLog(
                    rs.getInt("id"),
                    rs.getString("employee_id"),
                    rs.getString("employee_name"),
                    rs.getTime("morning_clock_in"),
                    rs.getTime("morning_clock_out"),
                    rs.getTime("afternoon_clock_in"),
                    rs.getTime("afternoon_clock_out"),
                    rs.getDate("log_date")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance logs: " + e.getMessage());
        }
        return logs;
    }
    
    public static boolean removeAttendanceLog(int logId) {
        String sql = "DELETE FROM attendance_logs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, logId);
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error removing attendance log: " + e.getMessage());
            return false;
        }
    }
    
    // ===== EXPORT AND REPORT OPERATIONS =====
    public static List<AttendanceLog> getAttendanceLogsByDateRange(Date startDate, Date endDate) {
        List<AttendanceLog> logs = new ArrayList<>();
        String sql = "SELECT id, employee_id, employee_name, " +
                     "morning_clock_in, morning_clock_out, " +
                     "afternoon_clock_in, afternoon_clock_out, " +
                     "log_date FROM attendance_logs WHERE log_date BETWEEN ? AND ? ORDER BY log_date DESC, employee_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setDate(1, new java.sql.Date(startDate.getTime()));
            pst.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    AttendanceLog log = new AttendanceLog(
                        rs.getInt("id"),
                        rs.getString("employee_id"),
                        rs.getString("employee_name"),
                        rs.getTime("morning_clock_in"),
                        rs.getTime("morning_clock_out"),
                        rs.getTime("afternoon_clock_in"),
                        rs.getTime("afternoon_clock_out"),
                        rs.getDate("log_date")
                    );
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance logs by date range: " + e.getMessage());
        }
        return logs;
    }
    
    /**
     * Get attendance logs for a specific employee and month
     */
    public static List<AttendanceLog> getAttendanceLogsByMonth(String employeeId, int year, int month) {
        List<AttendanceLog> logs = new ArrayList<>();
        // Get first and last day of the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        String sql = "SELECT id, employee_id, employee_name, " +
                     "morning_clock_in, morning_clock_out, " +
                     "afternoon_clock_in, afternoon_clock_out, " +
                     "log_date FROM attendance_logs WHERE employee_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, employeeId);
            pst.setDate(2, java.sql.Date.valueOf(startDate));
            pst.setDate(3, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    AttendanceLog log = new AttendanceLog(
                        rs.getInt("id"),
                        rs.getString("employee_id"),
                        rs.getString("employee_name"),
                        rs.getTime("morning_clock_in"),
                        rs.getTime("morning_clock_out"),
                        rs.getTime("afternoon_clock_in"),
                        rs.getTime("afternoon_clock_out"),
                        rs.getDate("log_date")
                    );
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance logs by month: " + e.getMessage());
        }
        return logs;
    }
    
    public static boolean exportAttendanceToCSV(String filePath) {
        List<AttendanceLog> logs = getAllAttendanceLogs();
        return ExcelExporter.exportAttendanceToCSV(logs, filePath);
    }
    
    public static boolean exportEmployeesToCSV(String filePath) {
        List<Employee> employees = getAllEmployees();
        return ExcelExporter.exportEmployeesToCSV(employees, filePath);
    }
    
    public static boolean generateAttendanceReport(String filePath) {
        List<AttendanceLog> logs = getAllAttendanceLogs();
        return ExcelExporter.generateAttendanceSummary(logs, filePath);
    }
    
    public static String getAttendanceStatistics() {
        List<AttendanceLog> logs = getAllAttendanceLogs();
        int totalLogs = logs.size();
        int todayLogs = 0;
        double totalHours = 0;
        
        String today = java.time.LocalDate.now().toString();
        
        for (AttendanceLog log : logs) {
            if (log.getClockIn() != null) {
                if (log.getLogDate().toString().equals(today)) {
                    todayLogs++;
                }
                
                String hours = log.getHoursWorked();
                if (!hours.isEmpty() && hours.contains("h")) {
                    try {
                        int h = Integer.parseInt(hours.split("h")[0].trim());
                        totalHours += h;
                    } catch (Exception e) {
                        // Skip invalid hours
                    }
                }
            }
        }
        
        return String.format("Total Records: %d\nToday's Attendance: %d\nTotal Hours: %.1f", 
            totalLogs, todayLogs, totalHours);
    }
}
