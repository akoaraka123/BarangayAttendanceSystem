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
     * Capitalize name properly and add spaces if missing
     * Preserves existing spaces and capitalization
     * Example: "marcobatiller" -> "Marco Batiller"
     * Example: "Marco Batiller" -> "Marco Batiller" (preserved)
     */
    public static String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) return "";
        
        String trimmed = name.trim();
        
        // Check if name has no spaces (all lowercase, no spaces)
        // Only add spaces if there are truly no spaces
        if (!trimmed.contains(" ") && trimmed.length() > 0) {
            // Check if it's all lowercase (no capital letters)
            boolean allLowercase = true;
            for (char c : trimmed.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    allLowercase = false;
                    break;
                }
            }
            
            // Only add spaces if it's all lowercase with no spaces
            if (allLowercase) {
                String withSpaces = addSpacesToName(trimmed);
                trimmed = withSpaces;
            }
        }
        
        // Split by spaces and capitalize each word properly
        String[] words = trimmed.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                // Capitalize first letter, lowercase the rest
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
    
    /**
     * Capitalize position properly (does NOT add spaces)
     * Only capitalizes first letter of each word, preserves existing spaces
     * Example: "kagawad" -> "Kagawad"
     * Example: "Kaga Wad" -> "Kaga Wad" (preserved, just capitalizes)
     */
    public static String capitalizePosition(String position) {
        if (position == null || position.trim().isEmpty()) return "";
        
        String trimmed = position.trim();
        
        // Split by spaces and capitalize each word properly (NO space addition)
        String[] words = trimmed.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.length() > 0) {
                // Capitalize first letter, lowercase the rest
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase());
                
                // Add space only if there was a space in original (not the last word)
                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * Add spaces to name if missing (intelligent word detection)
     * Example: "marcobatiller" -> "marco batiller"
     * Detects word boundaries by looking for capital letters or common name patterns
     */
    private static String addSpacesToName(String name) {
        if (name == null || name.isEmpty()) return name;
        
        // If already has spaces, return as is
        if (name.contains(" ")) return name;
        
        StringBuilder result = new StringBuilder();
        char[] chars = name.toCharArray();
        
        // If name has capital letters, add space before each capital (except first)
        boolean hasCapitals = false;
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                hasCapitals = true;
                break;
            }
        }
        
        if (hasCapitals) {
            // Add space before capital letters (except first character)
            for (int i = 0; i < chars.length; i++) {
                if (i > 0 && Character.isUpperCase(chars[i])) {
                    result.append(" ");
                }
                result.append(chars[i]);
            }
            return result.toString();
        }
        
        // If all lowercase with no spaces, try to split intelligently
        // Try common first name lengths (4-7 characters) and ensure second part is at least 3 chars
        if (name.length() >= 7) {
            // Try splitting at positions 4, 5, 6, 7 (common first name lengths)
            for (int splitPoint = 4; splitPoint <= 7 && splitPoint < name.length() - 2; splitPoint++) {
                String firstPart = name.substring(0, splitPoint);
                String secondPart = name.substring(splitPoint);
                
                // If second part is reasonable length (at least 3 chars), use this split
                if (secondPart.length() >= 3) {
                    return firstPart + " " + secondPart;
                }
            }
        }
        
        // For shorter names or if no good split found, try middle split
        if (name.length() > 5) {
            int splitPoint = name.length() / 2;
            // Ensure both parts are at least 3 characters
            if (splitPoint < 3) splitPoint = 3;
            if (splitPoint > name.length() - 3) splitPoint = name.length() - 3;
            
            return name.substring(0, splitPoint) + " " + name.substring(splitPoint);
        }
        
        // If too short to split meaningfully, return as is
        return name;
    }
}
