package attendance;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("🧪 Simple Login Test");
        
        // Test admin login
        System.out.println("\n🔐 Testing Admin Login:");
        boolean adminResult = DatabaseOperations.authenticateUser("admin@barangay.com", "admin123");
        System.out.println("Admin login result: " + (adminResult ? "✅ SUCCESS" : "❌ FAILED"));
        
        // Test employee login  
        System.out.println("\n🔐 Testing Employee Login:");
        boolean employeeResult = DatabaseOperations.authenticateUser("employee@barangay.com", "employee123");
        System.out.println("Employee login result: " + (employeeResult ? "✅ SUCCESS" : "❌ FAILED"));
        
        // Test with common mistakes
        System.out.println("\n🔍 Testing common mistakes:");
        
        // Test with spaces
        boolean adminSpaces = DatabaseOperations.authenticateUser(" admin@barangay.com ", "admin123");
        System.out.println("Admin with spaces: " + (adminSpaces ? "✅ SUCCESS" : "❌ FAILED"));
        
        // Test wrong password
        boolean adminWrongPass = DatabaseOperations.authenticateUser("admin@barangay.com", "wrong");
        System.out.println("Admin wrong password: " + (adminWrongPass ? "✅ SUCCESS" : "❌ FAILED"));
        
        // Test wrong email
        boolean adminWrongEmail = DatabaseOperations.authenticateUser("wrong@email.com", "admin123");
        System.out.println("Admin wrong email: " + (adminWrongEmail ? "✅ SUCCESS" : "❌ FAILED"));
    }
}
