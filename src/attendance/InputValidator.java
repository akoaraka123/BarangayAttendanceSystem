package attendance;

import java.util.regex.Pattern;

public class InputValidator {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Phone number validation (Philippines format)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(09|\\+639)\\d{9}$"
    );
    
    // Employee ID validation
    private static final Pattern EMP_ID_PATTERN = Pattern.compile(
        "^[A-Z]{3,4}\\d{3,4}$"
    );
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number (Philippines format)
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.trim().replaceAll("[\\s-]", ""); // Remove spaces and dashes
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validate employee ID format (e.g., EMP001, BRGY2023)
     */
    public static boolean isValidEmployeeId(String empId) {
        if (empId == null || empId.trim().isEmpty()) {
            return false;
        }
        return EMP_ID_PATTERN.matcher(empId.trim()).matches();
    }
    
    /**
     * Validate name (letters, spaces, hyphens, apostrophes only)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().matches("^[A-Za-z\\s\\-']{2,50}$");
    }
    
    /**
     * Validate address (basic validation)
     */
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.trim().length() >= 5 && address.trim().length() <= 200;
    }
    
    /**
     * Validate position (basic validation)
     */
    public static boolean isValidPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            return false;
        }
        return position.trim().matches("^[A-Za-z\\s]{2,30}$");
    }
    
    /**
     * Get validation error message for email
     */
    public static String getEmailErrorMessage(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format. Example: user@domain.com";
        }
        return "";
    }
    
    /**
     * Get validation error message for phone number
     */
    public static String getPhoneErrorMessage(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "Phone number is required";
        }
        if (!isValidPhoneNumber(phone)) {
            return "Invalid phone format. Use 09XXXXXXXX or +639XXXXXXXX";
        }
        return "";
    }
    
    /**
     * Get validation error message for employee ID
     */
    public static String getEmpIdErrorMessage(String empId) {
        if (empId == null || empId.trim().isEmpty()) {
            return "Employee ID is required";
        }
        if (!isValidEmployeeId(empId)) {
            return "Invalid Employee ID format. Use EMP001 or BRGY2023";
        }
        return "";
    }
    
    /**
     * Get validation error message for name
     */
    public static String getNameErrorMessage(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name is required";
        }
        if (!isValidName(name)) {
            return "Name should only contain letters, spaces, hyphens, and apostrophes";
        }
        return "";
    }
    
    /**
     * Get validation error message for address
     */
    public static String getAddressErrorMessage(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "Address is required";
        }
        if (!isValidAddress(address)) {
            return "Address must be 5-200 characters long";
        }
        return "";
    }
    
    /**
     * Get validation error message for position
     */
    public static String getPositionErrorMessage(String position) {
        if (position == null || position.trim().isEmpty()) {
            return "Position is required";
        }
        if (!isValidPosition(position)) {
            return "Position should only contain letters and spaces";
        }
        return "";
    }
    
    /**
     * Format phone number to standard format
     */
    public static String formatPhoneNumber(String phone) {
        if (phone == null) return "";
        String clean = phone.trim().replaceAll("[\\s-]", "");
        if (clean.startsWith("+639")) {
            return clean;
        } else if (clean.startsWith("09")) {
            return "+63" + clean.substring(1);
        }
        return clean;
    }
    
    /**
     * Capitalize name properly
     */
    public static String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) return "";
        
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}
