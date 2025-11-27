package attendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("🧪 Testing Database Connection...");
        
        // Test connection
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("✅ Database connection successful!");
            
            // Test if users table exists and has data
            String sql = "SELECT email, password, role FROM users";
            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                
                System.out.println("📋 Users in database:");
                while (rs.next()) {
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String role = rs.getString("role");
                    System.out.println("  - Email: " + email + ", Password: " + password + ", Role: " + role);
                }
                
                if (!rs.next()) {
                    System.out.println("⚠️ No users found in database!");
                }
            }
            
            // Test authentication
            System.out.println("\n🔐 Testing authentication:");
            boolean adminLogin = DatabaseOperations.authenticateUser("admin@barangay.com", "admin123");
            System.out.println("Admin login result: " + adminLogin);
            
            boolean employeeLogin = DatabaseOperations.authenticateUser("employee@barangay.com", "employee123");
            System.out.println("Employee login result: " + employeeLogin);
            
        } catch (Exception e) {
            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
