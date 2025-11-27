package attendance;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public class PasswordUtils {
    
    // Simple but effective password hashing (for demo purposes)
    // In production, use BCrypt or Argon2
    private static final String SALT = "BarangayAttendance2025@#$";
    private static final int HASH_ITERATIONS = 1000;
    
    // Password validation regex
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    /**
     * Hash password with salt
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        String saltedPassword = SALT + password;
        String hashed = saltedPassword;
        
        // Multiple iterations for stronger hashing
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            hashed = hashString(hashed);
        }
        
        return hashed;
    }
    
    /**
     * Verify password against hash
     */
    public static boolean verifyPassword(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        
        String computedHash = hashPassword(password);
        return hash.equals(computedHash);
    }
    
    /**
     * Simple string hashing (SHA-256 alternative using built-in methods)
     */
    private static String hashString(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing error", e);
        }
    }
    
    /**
     * Validate password strength
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Get password strength message
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        if (!isPasswordStrong(password)) {
            return "Password must contain:\n" +
                   "• At least 1 digit (0-9)\n" +
                   "• At least 1 lowercase letter\n" +
                   "• At least 1 uppercase letter\n" +
                   "• At least 1 special character (@#$%^&+=)";
        }
        
        return "Password is strong ✓";
    }
    
    /**
     * Generate random password
     */
    public static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one of each required character type
        password.append(chars.charAt(random.nextInt(26))); // Uppercase
        password.append(chars.charAt(random.nextInt(26) + 26)); // Lowercase
        password.append(chars.charAt(random.nextInt(10) + 52)); // Digit
        password.append("@#$%^&+=".charAt(random.nextInt(8))); // Special
        
        // Add remaining characters
        for (int i = 4; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
}
