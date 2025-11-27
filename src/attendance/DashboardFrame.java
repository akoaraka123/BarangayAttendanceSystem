package attendance;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    JButton btnManual, btnLogout;
    private RFIDReader rfidReader;
    private JLabel rfidStatusLabel;
    
    // Cooldown mechanism para maiwasan ang duplicate readings
    private String lastScannedCardId = null;
    private long lastScanTime = 0;
    private static final long COOLDOWN_PERIOD = 3000; // 3 seconds cooldown (para hindi mag-double scan, pero pwede mag-reread after)

    public DashboardFrame() {
        setTitle("Barangay Attendance - Employee Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ====== Header ======
        JPanel headerPanel = ThemeManager.createHeaderPanel();
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("🏢 Employee Dashboard", SwingConstants.CENTER);
        ThemeManager.styleHeaderLabel(headerLabel);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        // RFID Status Label
        rfidStatusLabel = new JLabel("🔴 RFID: OFF", SwingConstants.CENTER);
        rfidStatusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        rfidStatusLabel.setForeground(Color.RED);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        statusPanel.add(rfidStatusLabel);
        headerPanel.add(statusPanel, BorderLayout.WEST);
        
        // Logout button on the right
        btnLogout = new JButton("🚪 Logout");
        ThemeManager.styleDangerButton(btnLogout);
        btnLogout.setPreferredSize(new Dimension(120, 40));
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(btnLogout);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        // ====== Button Panel ======
        JPanel panel = ThemeManager.createModernPanel();
        panel.setLayout(new GridLayout(2, 1, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 350, 100, 350));

        btnManual = new JButton("✍️ Manual Entry");
        JButton btnRFIDManual = new JButton("🔖 Enter RFID Card ID");

        Dimension buttonSize = new Dimension(450, 100);
        
        // Style buttons
        ThemeManager.styleButton(btnManual);
        ThemeManager.styleSuccessButton(btnRFIDManual);
        
        btnManual.setPreferredSize(buttonSize);
        btnRFIDManual.setPreferredSize(buttonSize);

        panel.add(btnManual);
        panel.add(btnRFIDManual);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnManual.addActionListener(e -> manualEntry());
        btnRFIDManual.addActionListener(e -> enterRFIDCardID());
        btnLogout.addActionListener(e -> logout());
        
        // Initialize RFID Reader
        // Check if may saved configuration, otherwise use simulation mode
        initializeRFIDReader();
        
        // Auto-start RFID reading kapag bukas ang dashboard
        autoStartRFIDReading();
    }
    
    /**
     * Initialize RFID Reader - automatic simulation mode (no dialog)
     */
    private void initializeRFIDReader() {
        // Try to load configuration from properties
        String savedPort = System.getProperty("rfid.port");
        String savedBaudRate = System.getProperty("rfid.baudrate");
        
        if (savedPort != null && savedBaudRate != null) {
            try {
                int baudRate = Integer.parseInt(savedBaudRate);
                rfidReader = new RFIDReader(savedPort, baudRate);
                System.out.println("RFID Reader configured: " + savedPort + " @ " + baudRate);
            } catch (NumberFormatException e) {
                rfidReader = new RFIDReader(); // Use simulation mode
            }
        } else {
            // Automatic simulation mode - walang dialog
            rfidReader = new RFIDReader(); // Use simulation mode
            System.out.println("RFID Reader: Simulation mode active (manual entry).");
        }
        
        rfidReader.setOnCardDetected(this::handleRFIDCardDetected);
    }
    
    /**
     * Auto-start RFID reading kapag bukas ang dashboard
     */
    private void autoStartRFIDReading() {
        // Auto-enable RFID mode
        if (rfidReader.startReading()) {
            // Check if simulation mode or actual hardware
            String savedPort = System.getProperty("rfid.port");
            if (savedPort != null && !rfidReader.isSimulationMode()) {
                rfidStatusLabel.setText("🟢 RFID: ON - Hardware Mode (" + savedPort + ")");
            } else {
                rfidStatusLabel.setText("🟢 RFID: ON - Simulation Mode (Use Manual Entry)");
            }
            rfidStatusLabel.setForeground(new Color(0, 150, 0));
        } else {
            rfidStatusLabel.setText("🔴 RFID: Failed to Start");
            rfidStatusLabel.setForeground(Color.RED);
        }
    }
    
    
    /**
     * Manual entry ng RFID Card ID (para sa testing gamit ang printed barcode)
     * Mas seamless na - walang cooldown, pwede agad mag-reread
     */
    private void enterRFIDCardID() {
        // Show helpful message
        String message = "Enter RFID Card ID:\n\n" +
                        "📋 I-type ang RFID Card ID na nasa printed barcode\n" +
                        "🔖 O i-type ang ID number ng card\n\n" +
                        "Example: 1404270496";
        
        String rfidCardId = ThemeManager.showLargeInputDialog(this, 
            message, 
            "Enter RFID Card ID");
        
        if (rfidCardId == null || rfidCardId.trim().isEmpty()) {
            return;
        }
        
        // Reset cooldown para sa manual entry - pwede agad mag-reread
        lastScannedCardId = null;
        lastScanTime = 0;
        
        // Process RFID Card ID immediately
        handleRFIDCardDetected(rfidCardId.trim());
        
        // Show success message
        System.out.println("✅ Manual RFID Entry: " + rfidCardId.trim());
    }
    
    /**
     * Handle RFID card detection - Automatic Clock IN/OUT
     */
    private void handleRFIDCardDetected(String rfidCardId) {
        System.out.println("RFID Card Detected: " + rfidCardId);
        
        // Clean RFID card ID (uppercase, trim)
        rfidCardId = rfidCardId.trim().toUpperCase();
        
        // Check cooldown period - maiwasan ang duplicate readings within 3 seconds
        // Pero pwede mag-reread after 3 seconds
        long currentTime = System.currentTimeMillis();
        if (lastScannedCardId != null && lastScannedCardId.equals(rfidCardId)) {
            long timeSinceLastScan = currentTime - lastScanTime;
            if (timeSinceLastScan < COOLDOWN_PERIOD) {
                // Same card scanned within cooldown period - ignore para maiwasan ang double scan
                long remainingSeconds = (COOLDOWN_PERIOD - timeSinceLastScan) / 1000;
                System.out.println("RFID Card ignored (cooldown): " + rfidCardId + " - " + remainingSeconds + " seconds remaining");
                // Show message pero hindi blocking
                return;
            }
        }
        
        // Different card or same card after cooldown - process it
        
        // Update last scanned card and time
        lastScannedCardId = rfidCardId;
        lastScanTime = currentTime;
        
        // Try to get employee by RFID card ID
        String empId = DatabaseOperations.getEmployeeIdByRFID(rfidCardId);
        
        if (empId == null) {
            // RFID card not registered
            JOptionPane.showMessageDialog(this, 
                "❌ RFID Card not registered!\n\n" +
                "Card ID: " + rfidCardId + "\n\n" +
                "Please register this RFID card to an employee first.\n" +
                "Contact admin para mag-register ng RFID card.",
                "RFID Card Not Found", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get employee name
        String name = DatabaseOperations.getEmployeeName(empId);
        if (name == null) {
            JOptionPane.showMessageDialog(this, 
                "Employee not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Automatic process attendance (no confirmation needed)
        autoProcessAttendance(empId, name);
    }
    
    /**
     * Process attendance for employee (with confirmation)
     */
    private void processAttendance(String empId, String name) {
        String action = DatabaseOperations.getTodayAction(empId, name);
        String message;
        String buttonText;

        if (action.equals("NO_CLOCKIN")) {
            message = "Good morning, " + name + "!\nReady to Clock IN?";
            buttonText = "🕐 Clock IN";
        } else if (action.equals("CAN_CLOCKOUT")) {
            message = "Good day, " + name + "!\nReady to Clock OUT?";
            buttonText = "🕕 Clock OUT";
        } else {
            JOptionPane.showMessageDialog(this, 
                "You have already completed both Clock IN and OUT today!", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, message, "Confirm Action", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (DatabaseOperations.saveAttendanceLog(empId, name, buttonText.contains("IN") ? "Clock IN" : "Clock OUT")) {
                JOptionPane.showMessageDialog(this, 
                    buttonText + " recorded successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error recording attendance!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Automatic process attendance kapag na-scan ang RFID card (no confirmation)
     */
    private void autoProcessAttendance(String empId, String name) {
        String action = DatabaseOperations.getTodayAction(empId, name);
        String actionType;
        String successMessage;

        if (action.equals("NO_CLOCKIN")) {
            actionType = "Clock IN";
            successMessage = "✅ Clock IN recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🕐 Time: " + java.time.LocalTime.now().toString().substring(0, 5);
        } else if (action.equals("CAN_CLOCKOUT")) {
            actionType = "Clock OUT";
            successMessage = "✅ Clock OUT recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🕕 Time: " + java.time.LocalTime.now().toString().substring(0, 5);
        } else {
            JOptionPane.showMessageDialog(this, 
                "⚠️ " + name + "\n\n" +
                "You have already completed both Clock IN and OUT today!",
                "Attendance Already Recorded", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Save attendance automatically
        if (DatabaseOperations.saveAttendanceLog(empId, name, actionType)) {
            // Show success message
            JOptionPane.showMessageDialog(this, 
                successMessage,
                "Attendance Recorded", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "❌ Error recording attendance!\n\n" +
                "Please try again or contact admin.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manualEntry() {
        String empId, name;
        
        // Employee ID validation
        while (true) {
            empId = ThemeManager.showLargeInputDialog(this, "Enter Employee ID:", "Employee ID");
            if (empId == null) return;
            
            String error = InputValidator.getEmpIdErrorMessage(empId);
            if (error.isEmpty()) {
                empId = empId.trim().toUpperCase();
                break;
            }
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        // Name validation
        while (true) {
            name = ThemeManager.showLargeInputDialog(this, "Enter Employee Name:", "Employee Name");
            if (name == null) return;
            
            String error = InputValidator.getNameErrorMessage(name);
            if (error.isEmpty()) {
                name = InputValidator.capitalizeName(name);
                break;
            }
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!DatabaseOperations.isEmployeeRegistered(empId, name)) {
            JOptionPane.showMessageDialog(this,
                    "❌ Employee not registered!\nPlease register first before logging attendance.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String todayAction = DatabaseOperations.getTodayAction(empId, name);
        String[] options = null;

        switch (todayAction) {
            case "NO_CLOCKIN", "CAN_CLOCKIN" -> options = new String[]{"Clock IN"};
            case "CAN_CLOCKOUT" -> options = new String[]{"Clock OUT"};
            case "BOTH_DONE" -> {
                JOptionPane.showMessageDialog(this, "❌ Employee already has Clock IN and Clock OUT today!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose action for " + name,
                "Manual Attendance",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice >= 0) {
            String action = options[choice];
            if (DatabaseOperations.saveAttendanceLog(empId, name, action)) {
                JOptionPane.showMessageDialog(this, name + " " + action + " recorded!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error saving attendance record!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        // Stop RFID reader if running
        if (rfidReader != null) {
            rfidReader.stopReading();
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close employee dashboard
            new LoginFrame().setVisible(true); // Open login frame
        }
    }
    
    @Override
    public void dispose() {
        // Clean up RFID reader when closing
        if (rfidReader != null) {
            rfidReader.stopReading();
        }
        super.dispose();
    }
}
