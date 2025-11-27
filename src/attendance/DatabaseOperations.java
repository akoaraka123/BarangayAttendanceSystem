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
        System.out.println("🔍 DEBUG: Attempting login for email: " + email);
        System.out.println("🔍 DEBUG: Password length: " + password.length());
        
        String sql = "SELECT password, role FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");
                System.out.println("🔍 DEBUG: User found in database");
                System.out.println("🔍 DEBUG: Stored password: " + storedPassword);
                System.out.println("🔍 DEBUG: User role: " + role);
                
                // Temporary: Direct comparison for plain text
                boolean passwordMatch = password.equals(storedPassword);
                System.out.println("🔍 DEBUG: Password match: " + passwordMatch);
                
                return passwordMatch;
            } else {
                System.out.println("🔍 DEBUG: No user found with email: " + email);
            }
        } catch (SQLException e) {
            System.err.println("❌ Authentication error: " + e.getMessage());
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
        String sql = "SELECT role FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, email);
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
        String sql = "INSERT INTO employees (employee_id, full_name, contact_number, address, position) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            pst.setString(2, fullName);
            pst.setString(3, contact);
            pst.setString(4, address);
            pst.setString(5, position);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error registering employee: " + e.getMessage());
            return false;
        }
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
    
    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY full_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getString("employee_id"),
                    rs.getString("full_name"),
                    rs.getString("contact_number"),
                    rs.getString("address"),
                    rs.getString("position")
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error getting employees: " + e.getMessage());
        }
        return employees;
    }
    
    public static boolean updateEmployee(String empId, String fullName, String contact, String address, String position) {
        String sql = "UPDATE employees SET full_name = ?, contact_number = ?, address = ?, position = ? WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, fullName);
            pst.setString(2, contact);
            pst.setString(3, address);
            pst.setString(4, position);
            pst.setString(5, empId);
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean removeEmployee(String empId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error removing employee: " + e.getMessage());
            return false;
        }
    }
    
    // ===== ATTENDANCE OPERATIONS =====
    public static String getTodayAction(String empId, String name) {
        String today = LocalDate.now().toString();
        String sql = "SELECT clock_in, clock_out FROM attendance_logs WHERE employee_id = ? AND log_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, empId);
            pst.setString(2, today);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Time clockIn = rs.getTime("clock_in");
                Time clockOut = rs.getTime("clock_out");
                
                if (clockIn == null) return "NO_CLOCKIN";
                if (clockIn != null && clockOut == null) return "CAN_CLOCKOUT";
                if (clockIn != null && clockOut != null) return "BOTH_DONE";
            }
        } catch (SQLException e) {
            System.err.println("Error checking today's action: " + e.getMessage());
        }
        return "NO_CLOCKIN";
    }
    
    public static boolean saveAttendanceLog(String empId, String name, String action) {
        String today = LocalDate.now().toString();
        LocalTime now = LocalTime.now();
        
        try (Connection conn = DBConnection.getConnection()) {
            // Check if log exists for today
            String checkSql = "SELECT id FROM attendance_logs WHERE employee_id = ? AND log_date = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, empId);
            checkPst.setString(2, today);
            ResultSet rs = checkPst.executeQuery();
            
            if (rs.next()) {
                // Update existing log
                String updateSql;
                if (action.equals("Clock IN")) {
                    updateSql = "UPDATE attendance_logs SET clock_in = ? WHERE employee_id = ? AND log_date = ?";
                } else {
                    updateSql = "UPDATE attendance_logs SET clock_out = ? WHERE employee_id = ? AND log_date = ?";
                }
                
                PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setTime(1, Time.valueOf(now));
                updatePst.setString(2, empId);
                updatePst.setString(3, today);
                
                int result = updatePst.executeUpdate();
                return result > 0;
            } else {
                // Insert new log
                String insertSql = "INSERT INTO attendance_logs (employee_id, employee_name, clock_in, clock_out, log_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertPst = conn.prepareStatement(insertSql);
                
                insertPst.setString(1, empId);
                insertPst.setString(2, name);
                insertPst.setTime(3, action.equals("Clock IN") ? Time.valueOf(now) : null);
                insertPst.setTime(4, action.equals("Clock OUT") ? Time.valueOf(now) : null);
                insertPst.setString(5, today);
                
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
        String sql = "SELECT * FROM attendance_logs ORDER BY log_date DESC, employee_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                AttendanceLog log = new AttendanceLog(
                    rs.getInt("id"),
                    rs.getString("employee_id"),
                    rs.getString("employee_name"),
                    rs.getTime("clock_in"),
                    rs.getTime("clock_out"),
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
        String sql = "SELECT * FROM attendance_logs WHERE log_date BETWEEN ? AND ? ORDER BY log_date DESC, employee_name";
        
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
                        rs.getTime("clock_in"),
                        rs.getTime("clock_out"),
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
