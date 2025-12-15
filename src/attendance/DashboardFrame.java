package attendance;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {
    JButton btnManual, btnLogout;
    private RFIDReader rfidReader;
    private JLabel rfidStatusLabel;
    
    // Cooldown mechanism para maiwasan ang duplicate readings
    private String lastScannedCardId = null;
    private long lastScanTime = 0;
    private static final long COOLDOWN_PERIOD = 3000; // 3 seconds cooldown (para hindi mag-double scan, pero pwede mag-reread after)
    
    // Flag para sa input dialog - kapag true, RFID input ay pupunta sa dialog
    private JTextField activeInputDialogField = null;

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
     * Automatic RFID reading - kapag may card na na-tap, auto-fill ang text field
     */
    private void enterRFIDCardID() {
        // Show helpful message with tip for automatic RFID reading
        String message = "Enter RFID Card ID:\n\n" +
                        "💡 Tip: Tap your RFID card to auto-fill";
        
        String rfidCardId = showInputDialogWithRFID(this, message, "Enter RFID Card ID");
        
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
        final String cleanedCardId = rfidCardId.trim().toUpperCase();
        
        // Check if there's an active input dialog - if yes, fill it instead of processing
        final JTextField activeField = activeInputDialogField;
        if (activeField != null && activeField.isEnabled()) {
            SwingUtilities.invokeLater(() -> {
                activeField.setText(cleanedCardId);
                // Trigger action event to auto-submit
                activeField.postActionEvent();
            });
            return; // Don't process normally - let dialog handle it
        }
        
        // Check cooldown period - maiwasan ang duplicate readings within 3 seconds
        // Pero pwede mag-reread after 3 seconds
        long currentTime = System.currentTimeMillis();
        if (lastScannedCardId != null && lastScannedCardId.equals(cleanedCardId)) {
            long timeSinceLastScan = currentTime - lastScanTime;
            if (timeSinceLastScan < COOLDOWN_PERIOD) {
                // Same card scanned within cooldown period - ignore para maiwasan ang double scan
                long remainingSeconds = (COOLDOWN_PERIOD - timeSinceLastScan) / 1000;
                System.out.println("RFID Card ignored (cooldown): " + cleanedCardId + " - " + remainingSeconds + " seconds remaining");
                // Show message pero hindi blocking
                return;
            }
        }
        
        // Different card or same card after cooldown - process it
        
        // Update last scanned card and time
        lastScannedCardId = cleanedCardId;
        lastScanTime = currentTime;
        
        // Try to get employee by RFID card ID
        String empId = DatabaseOperations.getEmployeeIdByRFID(cleanedCardId);
        
        if (empId == null) {
            // RFID card not registered
            JOptionPane.showMessageDialog(this, 
                "❌ RFID Card not registered!\n\n" +
                "Card ID: " + cleanedCardId + "\n\n" +
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

        if (action.equals("MORNING_IN")) {
            message = "Good morning, " + name + "!\nReady to Clock IN for morning?";
            buttonText = "🌅 Morning Clock IN";
        } else if (action.equals("MORNING_OUT")) {
            message = "Good day, " + name + "!\nReady to Clock OUT for morning?";
            buttonText = "🌅 Morning Clock OUT";
        } else if (action.equals("AFTERNOON_IN")) {
            message = "Good afternoon, " + name + "!\nReady to Clock IN for afternoon?";
            buttonText = "🌆 Afternoon Clock IN";
        } else if (action.equals("AFTERNOON_OUT")) {
            message = "Good day, " + name + "!\nReady to Clock OUT for afternoon?";
            buttonText = "🌆 Afternoon Clock OUT";
        } else if (action.equals("WAIT_FOR_BREAK")) {
            JOptionPane.showMessageDialog(this, 
                "⏰ " + name + "\n\n" +
                "Morning attendance completed.\n" +
                "Please wait for break time (12:00 PM - 1:00 PM)\n" +
                "before clocking in for afternoon.",
                "Break Time", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(this, 
                "You have already completed all attendance for today!\n\n" +
                "🌅 Morning: IN & OUT\n" +
                "🌆 Afternoon: IN & OUT", 
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

        if (action.equals("MORNING_IN")) {
            actionType = "Clock IN";
            successMessage = "✅ Morning Clock IN recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🌅 Morning IN: " + java.time.LocalTime.now().toString().substring(0, 5) + "\n\n" +
                           "💡 Next: Clock OUT at 12:00 PM";
        } else if (action.equals("MORNING_OUT")) {
            actionType = "Clock OUT";
            successMessage = "✅ Morning Clock OUT recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🌅 Morning OUT: " + java.time.LocalTime.now().toString().substring(0, 5) + "\n\n" +
                           "💡 Break time: 12:00 PM - 1:00 PM\n" +
                           "💡 Next: Clock IN for afternoon at 1:00 PM";
        } else if (action.equals("AFTERNOON_IN")) {
            actionType = "Clock IN";
            successMessage = "✅ Afternoon Clock IN recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🌆 Afternoon IN: " + java.time.LocalTime.now().toString().substring(0, 5) + "\n\n" +
                           "💡 Next: Clock OUT at 5:00 PM";
        } else if (action.equals("AFTERNOON_OUT")) {
            actionType = "Clock OUT";
            successMessage = "✅ Afternoon Clock OUT recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🌆 Afternoon OUT: " + java.time.LocalTime.now().toString().substring(0, 5) + "\n\n" +
                           "🎉 All attendance completed for today!";
        } else if (action.equals("WAIT_FOR_BREAK")) {
            JOptionPane.showMessageDialog(this, 
                "⏰ " + name + "\n\n" +
                "Morning attendance completed.\n" +
                "Please wait for break time (12:00 PM - 1:00 PM)\n" +
                "before clocking in for afternoon.",
                "Break Time", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        } else if (action.equals("ALL_DONE")) {
            JOptionPane.showMessageDialog(this, 
                "✅ " + name + "\n\n" +
                "You have already completed all attendance for today!\n\n" +
                "🌅 Morning: IN & OUT\n" +
                "🌆 Afternoon: IN & OUT",
                "Attendance Already Recorded", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            // Fallback for unknown status
            actionType = "Clock IN";
            successMessage = "✅ Clock IN recorded!\n\n" +
                           "👤 Employee: " + name + "\n" +
                           "🕐 Time: " + java.time.LocalTime.now().toString().substring(0, 5);
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
        String empId;
        String name = null;
        
        // Employee ID or RFID Card ID input
        while (true) {
            String message = "Enter Employee ID or RFID Card ID:";
            
            String input = ThemeManager.showLargeInputDialog(this, message, "Employee ID / RFID Card ID");
            if (input == null) return;
            
            input = input.trim().toUpperCase();
            
            // Check if input matches Employee ID pattern
            if (InputValidator.isValidEmployeeId(input)) {
                // Valid Employee ID format - get name from database automatically
                empId = input;
                name = DatabaseOperations.getEmployeeName(empId);
                if (name != null) {
                    // Name found - proceed automatically
                    break;
                } else {
                    // Employee ID not found in database
                    JOptionPane.showMessageDialog(this, 
                        "❌ Employee ID not found!\n\n" +
                        "Employee ID: " + empId + "\n\n" +
                        "Please check the Employee ID or contact admin.",
                        "Employee Not Found", 
                        JOptionPane.ERROR_MESSAGE);
                    // Continue loop to ask again
                }
            } else {
                // Try to look up as RFID Card ID
                String foundEmpId = DatabaseOperations.getEmployeeIdByRFID(input);
                if (foundEmpId != null) {
                    // RFID Card ID found - get Employee ID and Name automatically
                    empId = foundEmpId;
                    name = DatabaseOperations.getEmployeeNameByRFID(input);
                    if (name != null) {
                        // Skip name input since we got it from RFID
                        break;
                    } else {
                        // RFID found but name is missing (shouldn't happen, but handle it)
                        JOptionPane.showMessageDialog(this, 
                            "⚠️ RFID Card found but employee name is missing!\n\n" +
                            "Please contact admin to fix employee record.",
                            "Data Error", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                
                // Not a valid Employee ID and not found as RFID
                JOptionPane.showMessageDialog(this, 
                    "❌ Invalid input!\n\n" +
                    "Please enter:\n" +
                    "• Valid Employee ID (e.g., EMP001)\n" +
                    "• Or registered RFID Card ID",
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // Verify employee name was found (should always be set by now)
        if (name == null) {
            JOptionPane.showMessageDialog(this,
                    "❌ Employee name not found!\nPlease contact admin to fix employee record.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verify employee is registered
        if (!DatabaseOperations.isEmployeeRegistered(empId, name)) {
            JOptionPane.showMessageDialog(this,
                    "❌ Employee not registered!\nPlease register first before logging attendance.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Automatic process attendance (no confirmation needed, just like RFID)
        autoProcessAttendance(empId, name);
    }
    
    /**
     * Show input dialog with automatic RFID reading support
     * Kapag may RFID card na na-tap, automatic na mag-fill ang text field
     */
    private String showInputDialogWithRFID(Component parent, String message, String title) {
        // Create custom dialog
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 200);
        dialog.setLocationRelativeTo(parent);
        
        // Panel for content
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Message label
        JLabel label = new JLabel("<html><div style='font-size: 16px; margin-bottom: 10px;'>" + message + "</div></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Text field
        JTextField textField = new JTextField();
        ThemeManager.styleTextField(textField);
        textField.setPreferredSize(new Dimension(450, 50));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        // Auto-focus on text field
        textField.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                textField.requestFocusInWindow();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
        
        // Enter key to submit
        textField.addActionListener(e -> {
            if (!textField.getText().trim().isEmpty()) {
                activeInputDialogField = null; // Clear before disposing
                dialog.dispose();
            }
        });
        
        // Also listen for text changes from RFID input
        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                checkAndClose();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {}
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
            
            private void checkAndClose() {
                String text = textField.getText().trim();
                // If text is long enough (likely RFID), auto-close after short delay
                if (text.length() >= 8) {
                    Timer timer = new Timer(500, e -> {
                        if (dialog.isVisible() && !textField.getText().trim().isEmpty()) {
                            activeInputDialogField = null;
                            dialog.dispose();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        ThemeManager.styleButton(okButton);
        ThemeManager.styleButton(cancelButton);
        
        okButton.addActionListener(e -> dialog.dispose());
        cancelButton.addActionListener(e -> {
            textField.setText("");
            dialog.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        
        // Set active input field for RFID detection
        activeInputDialogField = textField;
        
        // Show dialog
        dialog.setVisible(true);
        
        // Clear active input field after dialog closes
        activeInputDialogField = null;
        
        // Return text field value (empty if cancelled)
        String result = textField.getText().trim();
        return result.isEmpty() ? null : result;
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
