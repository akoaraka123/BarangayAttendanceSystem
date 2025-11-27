package attendance;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Utility class para mag-generate ng unique RFID Card ID / Barcode
 */
public class RFIDCardGenerator {
    
    private static final String PREFIX = "RFID";
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Generate unique RFID Card ID
     * Format: RFID + timestamp + random number
     * Example: RFID20241225123456789
     */
    public static String generateRFIDCardId() {
        // Method 1: Using timestamp + random
        long timestamp = System.currentTimeMillis();
        int randomNum = random.nextInt(9999);
        return PREFIX + timestamp + String.format("%04d", randomNum);
    }
    
    /**
     * Generate RFID Card ID with custom format
     * Format: RFID + Employee ID + random suffix
     * Example: RFIDEMP001A1B2
     */
    public static String generateRFIDCardId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            return generateRFIDCardId();
        }
        
        // Clean employee ID (remove spaces, uppercase)
        String cleanEmpId = employeeId.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
        
        // Generate random suffix (4 alphanumeric characters)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder suffix = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            suffix.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return PREFIX + cleanEmpId + suffix.toString();
    }
    
    /**
     * Generate simple numeric RFID Card ID
     * Format: 10-digit number
     * Example: 1234567890
     */
    public static String generateNumericRFIDCardId() {
        // Generate 10-digit number
        long number = 1000000000L + (random.nextLong() % 9000000000L);
        return String.valueOf(Math.abs(number));
    }
    
    /**
     * Generate UUID-based RFID Card ID
     * Format: UUID without dashes (uppercase)
     * Example: A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6
     */
    public static String generateUUIDRFIDCardId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
    
    /**
     * Generate short RFID Card ID (recommended for barcode)
     * Format: 8-12 alphanumeric characters
     * Example: RFID001A2B
     */
    public static String generateShortRFIDCardId() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder cardId = new StringBuilder(PREFIX);
        
        // Add 8 random alphanumeric characters
        for (int i = 0; i < 8; i++) {
            cardId.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return cardId.toString();
    }
    
    /**
     * Validate RFID Card ID format
     */
    public static boolean isValidRFIDCardId(String cardId) {
        if (cardId == null || cardId.trim().isEmpty()) {
            return false;
        }
        
        // Check length (minimum 8, maximum 50 characters)
        String trimmed = cardId.trim();
        if (trimmed.length() < 8 || trimmed.length() > 50) {
            return false;
        }
        
        // Check if contains only alphanumeric characters
        return trimmed.matches("^[A-Z0-9]+$");
    }
    
    /**
     * Format RFID Card ID for display (add dashes for readability)
     * Example: RFID001A2B -> RFID-001A-2B
     */
    public static String formatRFIDCardId(String cardId) {
        if (cardId == null || cardId.length() < 8) {
            return cardId;
        }
        
        // Add dashes every 4 characters
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < cardId.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append("-");
            }
            formatted.append(cardId.charAt(i));
        }
        
        return formatted.toString();
    }
}

