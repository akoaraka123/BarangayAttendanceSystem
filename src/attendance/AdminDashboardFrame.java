package attendance;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardFrame extends JFrame {
    private JButton btnRegister, btnViewLogs, btnViewEmployees, btnRemove, btnChangePassword;
    private JButton btnExportAttendance, btnExportEmployees, btnGenerateReport, btnViewStatistics, btnLogout;

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ====== Header ======
        JPanel headerPanel = ThemeManager.createHeaderPanel();
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("🏢 Admin Dashboard", SwingConstants.CENTER);
        ThemeManager.styleHeaderLabel(headerLabel);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
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
        panel.setLayout(new GridLayout(4, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 300, 100, 300));

        btnRegister = new JButton("👤 Register Employee");
        btnViewLogs = new JButton("📋 View Attendance Logs");
        btnViewEmployees = new JButton("👥 View & Edit Employees");
        btnRemove = new JButton("🗑️ Remove Employee");
        btnChangePassword = new JButton("🔐 Change Password");
        btnExportAttendance = new JButton("📊 Export Attendance (CSV)");
        btnExportEmployees = new JButton("📇 Export Employees (CSV)");
        btnGenerateReport = new JButton("📄 Generate Report");
        btnViewStatistics = new JButton("📈 View Statistics");

        Dimension buttonSize = new Dimension(350, 70);
        
        // Style buttons with different colors
        ThemeManager.styleButton(btnRegister);
        ThemeManager.styleButton(btnViewLogs);
        ThemeManager.styleButton(btnViewEmployees);
        ThemeManager.styleDangerButton(btnRemove);
        ThemeManager.styleButton(btnChangePassword);
        ThemeManager.styleSuccessButton(btnExportAttendance);
        ThemeManager.styleSuccessButton(btnExportEmployees);
        ThemeManager.styleButton(btnGenerateReport);
        ThemeManager.styleButton(btnViewStatistics);
        
        JButton[] buttons = {btnRegister, btnViewLogs, btnViewEmployees, btnRemove, 
                            btnChangePassword, btnExportAttendance, btnExportEmployees, 
                            btnGenerateReport, btnViewStatistics};
        
        for (JButton b : buttons) {
            b.setPreferredSize(buttonSize);
            panel.add(b);
        }

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnRegister.addActionListener(e -> registerEmployee());
        btnViewLogs.addActionListener(e -> viewLogs());
        btnViewEmployees.addActionListener(e -> viewEmployees());
        btnRemove.addActionListener(e -> removeEmployee());
        btnChangePassword.addActionListener(e -> new ChangePasswordFrame().setVisible(true));
        btnExportAttendance.addActionListener(e -> exportAttendance());
        btnExportEmployees.addActionListener(e -> exportEmployees());
        btnGenerateReport.addActionListener(e -> generateReport());
        btnViewStatistics.addActionListener(e -> viewStatistics());
        btnLogout.addActionListener(e -> logout());
    }

    private void registerEmployee() {
        String fullName, contact, address, position;

        // Auto-generate Employee ID
        String empId = generateEmployeeId();
        
        // Show the generated ID to user - Large custom dialog
        JDialog idDialog = new JDialog(this, "Employee ID Generated", true);
        idDialog.setSize(700, 300);
        idDialog.setLocationRelativeTo(this);
        
        JPanel idPanel = new JPanel(new BorderLayout());
        idPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        idPanel.setBackground(Color.WHITE);
        
        JLabel idLabel = new JLabel("<html><div style='font-size: 24px; text-align: center;'>" +
                                   "Generated Employee ID: <b>" + empId + "</b><br><br>" +
                                   "Please continue with employee registration.</div></html>");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        idPanel.add(idLabel, BorderLayout.CENTER);
        
        JButton idOkButton = new JButton("Continue");
        ThemeManager.styleButton(idOkButton);
        idOkButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        idOkButton.setPreferredSize(new Dimension(150, 50));
        idOkButton.addActionListener(e -> idDialog.dispose());
        
        JPanel idButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        idButtonPanel.setBackground(Color.WHITE);
        idButtonPanel.add(idOkButton);
        idPanel.add(idButtonPanel, BorderLayout.SOUTH);
        
        idDialog.add(idPanel);
        idDialog.setVisible(true);

        // Full Name validation
        while (true) {
            fullName = ThemeManager.showLargeInputDialog(this, "Enter Full Name:", "Full Name");
            if (fullName == null) return;
            
            String error = InputValidator.getNameErrorMessage(fullName);
            if (error.isEmpty()) {
                fullName = InputValidator.capitalizeName(fullName);
                break;
            }
            showLargeErrorDialog(this, error, "Validation Error");
        }

        // Contact Number validation
        while (true) {
            contact = ThemeManager.showLargeInputDialog(this, "Enter Contact Number (09XXXXXXXX or +639XXXXXXXX):", "Contact Number");
            if (contact == null) return;
            
            String error = InputValidator.getPhoneErrorMessage(contact);
            if (error.isEmpty()) {
                contact = InputValidator.formatPhoneNumber(contact);
                break;
            }
            showLargeErrorDialog(this, error, "Validation Error");
        }

        // Address validation
        while (true) {
            address = ThemeManager.showLargeInputDialog(this, "Enter Address:", "Address");
            if (address == null) return;
            
            String error = InputValidator.getAddressErrorMessage(address);
            if (error.isEmpty()) {
                address = address.trim();
                break;
            }
            showLargeErrorDialog(this, error, "Validation Error");
        }

        // Position validation
        while (true) {
            position = ThemeManager.showLargeInputDialog(this, "Enter Position:", "Position");
            if (position == null) return;
            
            String error = InputValidator.getPositionErrorMessage(position);
            if (error.isEmpty()) {
                position = InputValidator.capitalizePosition(position);
                break;
            }
            showLargeErrorDialog(this, error, "Validation Error");
        }

        // Check if employee already exists
        if (DatabaseOperations.isEmployeeRegistered(empId, fullName)) {
            showLargeErrorDialog(this, "Employee already registered!", "Error");
            return;
        }

        // Auto-generate RFID Card ID / Barcode
        String rfidCardId = DatabaseOperations.generateUniqueRFIDCardIdForEmployee(empId);
        
        // Register employee
        if (DatabaseOperations.registerEmployee(empId, fullName, contact, address, position, rfidCardId)) {
            // Show success message - Large custom dialog
            JDialog successDialog = new JDialog(this, "Employee Registered", true);
            successDialog.setSize(800, 500);
            successDialog.setLocationRelativeTo(this);
            
            JPanel successPanel = new JPanel(new BorderLayout());
            successPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            successPanel.setBackground(Color.WHITE);
            
            String successMessage = "<html><div style='font-size: 22px; text-align: center; line-height: 1.6;'>" +
                                   "✅ Employee Registered Successfully!<br><br>" +
                                   "📋 Employee ID: <b>" + empId + "</b><br>" +
                                   "👤 Name: <b>" + fullName + "</b><br><br>" +
                                   "🔖 RFID Card / Barcode ID: <b>" + rfidCardId + "</b><br><br>" +
                                   "⚠️ IMPORTANT: I-save ang RFID Card ID na ito!<br>" +
                                   "I-tap/scan ang RFID card na ito sa RFID reader para mag-automatic clock in/out.<br><br>" +
                                   "🖨️ Mag-print ng barcode?</div></html>";
            
            JLabel successLabel = new JLabel(successMessage);
            successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            successLabel.setHorizontalAlignment(SwingConstants.CENTER);
            successPanel.add(successLabel, BorderLayout.CENTER);
            
            JPanel successButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            successButtonPanel.setBackground(Color.WHITE);
            successButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            
            JButton yesButton = new JButton("Yes - Print Barcode");
            JButton noButton = new JButton("No - Skip");
            ThemeManager.styleButton(yesButton);
            ThemeManager.styleButton(noButton);
            yesButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            noButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            yesButton.setPreferredSize(new Dimension(200, 50));
            noButton.setPreferredSize(new Dimension(150, 50));
            
            final boolean[] shouldPrint = {false};
            
            yesButton.addActionListener(e -> {
                shouldPrint[0] = true;
                successDialog.dispose();
            });
            
            noButton.addActionListener(e -> {
                shouldPrint[0] = false;
                successDialog.dispose();
            });
            
            successButtonPanel.add(yesButton);
            successButtonPanel.add(noButton);
            successPanel.add(successButtonPanel, BorderLayout.SOUTH);
            
            successDialog.add(successPanel);
            successDialog.setVisible(true);
            
            // If user clicked "Yes", automatic na mag-print
            if (shouldPrint[0]) {
                // Automatic print - show print dialog then print
                BarcodePrinter.printBarcode(rfidCardId, fullName, empId);
            }
        } else {
            showLargeErrorDialog(this, "Error registering employee!", "Error");
        }
    }

    private void viewLogs() {
        List<AttendanceLog> logs = DatabaseOperations.getAllAttendanceLogs();
        if (logs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No attendance logs found.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"ID", "Name", "Employee ID", "Clock IN", "Clock OUT", "Date", "Hours Worked"};
        Object[][] data = new Object[logs.size()][7];

        for (int i = 0; i < logs.size(); i++) {
            AttendanceLog log = logs.get(i);
            data[i][0] = log.getId();
            data[i][1] = log.getEmployeeName();
            data[i][2] = log.getEmployeeId();
            data[i][3] = log.getFormattedClockIn();
            data[i][4] = log.getFormattedClockOut();
            data[i][5] = log.getFormattedDate();
            data[i][6] = log.getHoursWorked();
        }

        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                String hoursText = (String) getValueAt(row, 6);
                if (hoursText != null && !hoursText.isEmpty() && hoursText.contains("h")) {
                    try { 
                        int h = Integer.parseInt(hoursText.split("h")[0].trim());
                        c.setForeground(h < 8 ? Color.RED : Color.BLACK); 
                    } catch (Exception ex) { 
                        c.setForeground(Color.BLACK); 
                    }
                } else { 
                    c.setForeground(Color.BLACK); 
                }
                return c;
            }
        };

        table.setEnabled(false);
        table.setRowHeight(30); // Increase row height for better visibility
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Larger font
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Manual column sizing for better control

        // Set optimal column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);   // ID
        columnModel.getColumn(1).setPreferredWidth(200);  // Name
        columnModel.getColumn(2).setPreferredWidth(120); // Employee ID
        columnModel.getColumn(3).setPreferredWidth(120);  // Clock IN
        columnModel.getColumn(4).setPreferredWidth(120);  // Clock OUT
        columnModel.getColumn(5).setPreferredWidth(120);  // Date
        columnModel.getColumn(6).setPreferredWidth(120);  // Hours Worked

        JScrollPane scrollPane = new JScrollPane(table);
        // Make it full view - use most of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = (int)(screenSize.width * 0.9); // 90% of screen width
        int dialogHeight = (int)(screenSize.height * 0.8); // 80% of screen height
        scrollPane.setPreferredSize(new Dimension(dialogWidth - 100, dialogHeight - 150));

        String[] options = {"OK", "REMOVE"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Attendance Logs",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) { // REMOVE
            String adminPass = ThemeManager.showPasswordDialog(this, "Enter Admin Password:", "Admin Password");
            if (!"admin123".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use search dialog with suggestions for logs
            AttendanceLog logToRemove = searchLogWithSuggestions(logs, "Remove Log");
            if (logToRemove == null) {
                return;
            }
            
            int logId = logToRemove.getId();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this log?\n" + logToRemove.toString(),
                    "Confirm Remove", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            if (DatabaseOperations.removeAttendanceLog(logId)) {
                JOptionPane.showMessageDialog(this, "Log removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error removing log!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewEmployees() {
        List<Employee> employees = DatabaseOperations.getAllEmployees();
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees registered.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"#", "Employee ID", "Full Name", "Contact", "Address", "Position", "RFID Card ID"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            Object[] row = {i + 1, emp.getEmployeeId(), emp.getFullName(), 
                          emp.getContactNumber(), emp.getAddress(), emp.getPosition(),
                          emp.getRfidCardId() != null ? emp.getRfidCardId() : "Not Set"};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setEnabled(false);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30); // Increase row height for better visibility
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Larger font

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // #
        columnModel.getColumn(1).setPreferredWidth(120); // Employee ID
        columnModel.getColumn(2).setPreferredWidth(200); // Full Name
        columnModel.getColumn(3).setPreferredWidth(150); // Contact
        columnModel.getColumn(4).setPreferredWidth(300); // Address
        columnModel.getColumn(5).setPreferredWidth(150); // Position
        columnModel.getColumn(6).setPreferredWidth(200); // RFID Card ID

        JScrollPane scrollPane = new JScrollPane(table);
        // Make it full view - use most of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = (int)(screenSize.width * 0.9); // 90% of screen width
        int dialogHeight = (int)(screenSize.height * 0.8); // 80% of screen height
        scrollPane.setPreferredSize(new Dimension(dialogWidth - 100, dialogHeight - 150));

        String[] options = {"OK", "EDIT"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Registered Employees",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) {
            // Use search dialog with suggestions
            Employee emp = searchEmployeeWithSuggestions("Edit Employee");
            if (emp == null) {
                return; 
            }
            
            // Show menu para pumili ng field na i-edit
            String[] editOptions = {
                "📋 Employee ID",
                "👤 Full Name", 
                "📞 Contact Number",
                "🏠 Address",
                "💼 Position",
                "🔖 RFID Card ID",
                "✅ Edit All Fields"
            };
            
            int fieldChoice = JOptionPane.showOptionDialog(this,
                "Select field to edit for:\n\n" +
                "Employee: " + emp.getFullName() + "\n" +
                "Employee ID: " + emp.getEmployeeId(),
                "Edit Employee - Select Field",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                editOptions,
                editOptions[0]);
            
            if (fieldChoice < 0) return; // User cancelled
            
            // Get current values
            String newEmpId = emp.getEmployeeId();
            String newName = emp.getFullName();
            String newContact = emp.getContactNumber();
            String newAddress = emp.getAddress();
            String newPosition = emp.getPosition();
            String newRFID = emp.getRfidCardId();
            
            boolean updated = false;
            String updatedField = "";
            
            // Edit specific field based on selection
            switch (fieldChoice) {
                case 0: // Employee ID
                    String inputEmpId = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + emp.getEmployeeId() + "\n\nEnter new Employee ID:", 
                        "Edit Employee ID");
                    if (inputEmpId != null && !inputEmpId.trim().isEmpty()) {
                        newEmpId = inputEmpId.trim().toUpperCase();
                        updated = true;
                        updatedField = "Employee ID";
                    }
                    break;
                    
                case 1: // Full Name
                    String inputName = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + emp.getFullName() + "\n\nEnter new Full Name:", 
                        "Edit Full Name");
                    if (inputName != null && !inputName.trim().isEmpty()) {
                        newName = InputValidator.capitalizeName(inputName.trim());
                        updated = true;
                        updatedField = "Full Name";
                    }
                    break;
                    
                case 2: // Contact Number
                    String inputContact = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + (emp.getContactNumber() != null ? emp.getContactNumber() : "Not Set") + 
                        "\n\nEnter new Contact Number:", 
                        "Edit Contact Number");
                    if (inputContact != null && !inputContact.trim().isEmpty()) {
                        String error = InputValidator.getPhoneErrorMessage(inputContact);
                        if (error.isEmpty()) {
                            newContact = InputValidator.formatPhoneNumber(inputContact);
                            updated = true;
                            updatedField = "Contact Number";
                        } else {
                            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    break;
                    
                case 3: // Address
                    String inputAddress = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + (emp.getAddress() != null ? emp.getAddress() : "Not Set") + 
                        "\n\nEnter new Address:", 
                        "Edit Address");
                    if (inputAddress != null && !inputAddress.trim().isEmpty()) {
                        newAddress = inputAddress.trim();
                        updated = true;
                        updatedField = "Address";
                    }
                    break;
                    
                case 4: // Position
                    String inputPosition = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + (emp.getPosition() != null ? emp.getPosition() : "Not Set") + 
                        "\n\nEnter new Position:", 
                        "Edit Position");
                    if (inputPosition != null && !inputPosition.trim().isEmpty()) {
                        newPosition = InputValidator.capitalizePosition(inputPosition.trim());
                        updated = true;
                        updatedField = "Position";
                    }
                    break;
                    
                case 5: // RFID Card ID
                    String currentRFID = emp.getRfidCardId();
                    String rfidInput = ThemeManager.showLargeInputDialog(this, 
                        "Current RFID Card ID: " + (currentRFID != null ? currentRFID : "Not Set") + 
                        "\n\nEnter new RFID Card ID (leave blank to remove):", 
                        "Edit RFID Card ID");
                    
                    if (rfidInput != null) {
                        if (rfidInput.trim().isEmpty()) {
                            newRFID = null;
                            updated = true;
                            updatedField = "RFID Card ID (removed)";
                        } else {
                            String rfidValue = rfidInput.trim().toUpperCase();
                            // Check if RFID card is already registered to another employee
                            if (DatabaseOperations.isRFIDCardRegistered(rfidValue)) {
                                String existingEmpId = DatabaseOperations.getEmployeeIdByRFID(rfidValue);
                                if (existingEmpId != null && !existingEmpId.equals(emp.getEmployeeId())) {
                                    JOptionPane.showMessageDialog(this, 
                                        "RFID Card already registered to another employee!", 
                                        "Error", 
                                        JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            newRFID = rfidValue;
                            updated = true;
                            updatedField = "RFID Card ID";
                        }
                    }
                    break;
                    
                case 6: // Edit All Fields
                    // Edit all fields (original behavior)
                    String allEmpId = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + emp.getEmployeeId() + "\n\nEdit Employee ID:", 
                        "Edit Employee ID");
                    if (allEmpId == null) return;
                    newEmpId = allEmpId.trim().toUpperCase();
                    
                    String allName = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + emp.getFullName() + "\n\nEdit Full Name:", 
                        "Edit Full Name");
                    if (allName == null) return;
                    newName = InputValidator.capitalizeName(allName.trim());
                    
                    String allContact = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + (emp.getContactNumber() != null ? emp.getContactNumber() : "Not Set") + 
                        "\n\nEdit Contact Number:", 
                        "Edit Contact Number");
                    if (allContact == null) return;
                    String contactError = InputValidator.getPhoneErrorMessage(allContact);
                    if (!contactError.isEmpty()) {
                        JOptionPane.showMessageDialog(this, contactError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newContact = InputValidator.formatPhoneNumber(allContact);
                    
                    String allAddress = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + (emp.getAddress() != null ? emp.getAddress() : "Not Set") + 
                        "\n\nEdit Address:", 
                        "Edit Address");
                    if (allAddress == null) return;
                    newAddress = allAddress.trim();
                    
                    String allPosition = ThemeManager.showLargeInputDialog(this, 
                        "Current: " + (emp.getPosition() != null ? emp.getPosition() : "Not Set") + 
                        "\n\nEdit Position:", 
                        "Edit Position");
                    if (allPosition == null) return;
                    newPosition = InputValidator.capitalizePosition(allPosition.trim());
                    
                    // RFID Card ID
                    String allRFID = emp.getRfidCardId();
                    String rfidMessage = "Current RFID Card ID: " + (allRFID != null ? allRFID : "Not Set") + 
                                        "\n\nDo you want to update RFID Card ID?";
                    int rfidUpdateChoice = JOptionPane.showConfirmDialog(this, 
                        rfidMessage, 
                        "Update RFID Card", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (rfidUpdateChoice == JOptionPane.YES_OPTION) {
                        String rfidInputAll = ThemeManager.showLargeInputDialog(this, 
                            "Enter/Scan RFID Card ID (leave blank to remove):", 
                            "RFID Card ID");
                        
                        if (rfidInputAll != null) {
                            if (rfidInputAll.trim().isEmpty()) {
                                newRFID = null;
                            } else {
                                String rfidValueAll = rfidInputAll.trim().toUpperCase();
                                if (DatabaseOperations.isRFIDCardRegistered(rfidValueAll)) {
                                    String existingEmpIdAll = DatabaseOperations.getEmployeeIdByRFID(rfidValueAll);
                                    if (existingEmpIdAll != null && !existingEmpIdAll.equals(emp.getEmployeeId())) {
                                        JOptionPane.showMessageDialog(this, 
                                            "RFID Card already registered to another employee!", 
                                            "Error", 
                                            JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                                newRFID = rfidValueAll;
                            }
                        }
                    }
                    
                    updated = true;
                    updatedField = "All Fields";
                    break;
            }
            
            // Update employee if any field was changed
            if (updated) {
                if (DatabaseOperations.updateEmployee(newEmpId, newName, newContact, newAddress, newPosition, newRFID)) {
                    String successMsg = "✅ Employee updated successfully!\n\nUpdated: " + updatedField;
                    if (fieldChoice == 5 && newRFID != null) {
                        successMsg += "\n\nRFID Card ID: " + newRFID;
                    }
                    JOptionPane.showMessageDialog(this, successMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating employee!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void removeEmployee() {
        List<Employee> employees = DatabaseOperations.getAllEmployees();
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees registered.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Ask for admin password first
        String adminPass = ThemeManager.showPasswordDialog(this, "Enter Admin Password:", "Admin Password");
        if (!"admin123".equals(adminPass)) { 
            JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }

        // Use search dialog with suggestions (same as searchEmployeeWithSuggestions but for removal)
        Employee emp = searchEmployeeWithSuggestions("Remove Employee");
        if (emp == null) {
            return; 
        }

        // Check if employee has attendance records
        List<AttendanceLog> empLogs = DatabaseOperations.getAllAttendanceLogs();
        boolean hasLogs = false;
        for (AttendanceLog log : empLogs) {
            if (log.getEmployeeId().equals(emp.getEmployeeId())) {
                hasLogs = true;
                break;
            }
        }

        String warningMessage = "Are you sure you want to remove employee:\n" + emp.toString();
        if (hasLogs) {
            warningMessage += "\n\nWARNING: This employee has attendance records that will also be deleted!";
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                warningMessage,
                "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (DatabaseOperations.removeEmployee(emp.getEmployeeId())) {
            JOptionPane.showMessageDialog(this, "Employee removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error removing employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportAttendance() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setSelectedFile(new java.io.File(ExcelExporter.getExportFilePath("attendance_export", "csv")));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }
            
            if (DatabaseOperations.exportAttendanceToCSV(filePath)) {
                JOptionPane.showMessageDialog(this, "Attendance data exported successfully!\nFile: " + filePath, 
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error exporting attendance data!", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportEmployees() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setSelectedFile(new java.io.File(ExcelExporter.getExportFilePath("employees_export", "csv")));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }
            
            if (DatabaseOperations.exportEmployeesToCSV(filePath)) {
                JOptionPane.showMessageDialog(this, "Employee data exported successfully!\nFile: " + filePath, 
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error exporting employee data!", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void generateReport() {
        // Create custom large dialog for report type selection
        JDialog reportTypeDialog = new JDialog(this, "Generate Report", true);
        reportTypeDialog.setSize(800, 600);
        reportTypeDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
        
        // Title label - Large and clear
        JLabel titleLabel = new JLabel("Select Report Type:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Options panel with large buttons
        JPanel optionsPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        optionsPanel.setBackground(Color.WHITE);
        
        String[] reportTypes = {
            "📅 MONTHS - Select months",
            "📆 WEEKS - Select weeks",
            "📊 YEARS - Select years",
            "🔍 SEARCH EMPLOYEE - Search for employee",
            "📋 ALL RECORDS - All records"
        };
        
        JButton[] optionButtons = new JButton[5];
        final int[] selectedChoice = {-1};
        
        for (int i = 0; i < reportTypes.length; i++) {
            final int index = i;
            optionButtons[i] = new JButton(reportTypes[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(getForeground());
                    FontMetrics fm = g2.getFontMetrics();
                    String text = getText();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.setFont(getFont());
                    g2.drawString(text, x, y);
                    g2.dispose();
                }
            };
            optionButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 22));
            optionButtons[i].setPreferredSize(new Dimension(700, 80));
            // Use same blue color as admin dashboard buttons
            optionButtons[i].setBackground(ThemeManager.PRIMARY_COLOR); // Blue like dashboard
            optionButtons[i].setForeground(Color.WHITE);
            optionButtons[i].setOpaque(true);
            optionButtons[i].setContentAreaFilled(false); // We paint it ourselves
            optionButtons[i].setBorderPainted(false);
            optionButtons[i].setFocusPainted(false);
            
            // Hover effect - light blue when hovering (same as dashboard)
            optionButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    optionButtons[index].setBackground(ThemeManager.SECONDARY_COLOR); // Light blue
                    optionButtons[index].repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    optionButtons[index].setBackground(ThemeManager.PRIMARY_COLOR); // Blue
                    optionButtons[index].repaint();
                }
            });
            
            optionButtons[i].addActionListener(e -> {
                selectedChoice[0] = index;
                reportTypeDialog.dispose();
            });
            
            optionsPanel.add(optionButtons[i]);
        }
        
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        
        // Cancel button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.addActionListener(e -> {
            selectedChoice[0] = -1;
            reportTypeDialog.dispose();
        });
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        reportTypeDialog.add(mainPanel);
        reportTypeDialog.setVisible(true);
        
        int reportTypeChoice = selectedChoice[0];
        if (reportTypeChoice < 0) return; // User cancelled
        
        List<AttendanceLog> filteredLogs = null;
        String reportTitle = "";
        
        // Filter logs based on selected type
        switch (reportTypeChoice) {
            case 0: // MONTHS
                filteredLogs = filterByMonths();
                reportTitle = "Monthly Report";
                break;
            case 1: // WEEKS
                filteredLogs = filterByWeeks();
                reportTitle = "Weekly Report";
                break;
            case 2: // YEARS
                filteredLogs = filterByYears();
                reportTitle = "Yearly Report";
                break;
            case 3: // SEARCH EMPLOYEE
                filteredLogs = filterByEmployee();
                reportTitle = "Employee Report";
                break;
            case 4: // ALL RECORDS
                filteredLogs = DatabaseOperations.getAllAttendanceLogs();
                reportTitle = "Complete Report";
                break;
        }
        
        if (filteredLogs == null || filteredLogs.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No records found for the selected criteria.", 
                "No Data", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Automatically show report (no action dialog)
        showReportDialog(filteredLogs, reportTitle);
    }
    
    /**
     * Filter logs by months
     */
    private List<AttendanceLog> filterByMonths() {
        // Get current year
        int currentYear = java.time.LocalDate.now().getYear();
        
        // Show month selection
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        
        String monthInput = (String) JOptionPane.showInputDialog(this,
            "Select month:",
            "Select Month",
            JOptionPane.QUESTION_MESSAGE,
            null,
            months,
            months[java.time.LocalDate.now().getMonthValue() - 1]);
        
        if (monthInput == null) return null;
        
        int selectedMonth = java.util.Arrays.asList(months).indexOf(monthInput) + 1;
        
        // Get year
        String yearInput = ThemeManager.showLargeInputDialog(this, 
            "Enter Year (e.g., " + currentYear + "):", 
            "Select Year");
        if (yearInput == null) return null;
        
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Filter logs by month and year
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        List<AttendanceLog> filtered = new java.util.ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            java.time.LocalDate logDate = log.getLogDate().toLocalDate();
            if (logDate.getYear() == selectedYear && logDate.getMonthValue() == selectedMonth) {
                filtered.add(log);
            }
        }
        
        return filtered;
    }
    
    /**
     * Filter logs by weeks (bi-weekly periods: 1-15 and 16-30/31)
     * Auto-generates Period 2 if Period 1 was already generated and we're in Period 2 dates
     */
    private List<AttendanceLog> filterByWeeks() {
        // Get current date
        java.time.LocalDate today = java.time.LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        int currentDay = today.getDayOfMonth();
        
        // Check if we're in Period 2 (16-30/31) of current month
        boolean isInPeriod2 = currentDay >= 16;
        
        // Calculate periods for current month
        java.time.LocalDate period1Start = java.time.LocalDate.of(currentYear, currentMonth, 1);
        java.time.LocalDate period1End = java.time.LocalDate.of(currentYear, currentMonth, 15);
        java.time.LocalDate lastDayOfMonth = java.time.LocalDate.of(currentYear, currentMonth, 1)
            .withDayOfMonth(java.time.LocalDate.of(currentYear, currentMonth, 1).lengthOfMonth());
        java.time.LocalDate period2Start = java.time.LocalDate.of(currentYear, currentMonth, 16);
        java.time.LocalDate period2End = lastDayOfMonth;
        
        // Check which periods have data
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        boolean period1HasData = hasLogsInRange(allLogs, period1Start, period1End);
        boolean period2HasData = hasLogsInRange(allLogs, period2Start, period2End);
        
        // Auto-detect: If we're in Period 2, Period 1 has data, and Period 2 has data, auto-select Period 2
        if (isInPeriod2 && period1HasData && period2HasData) {
            // Auto-generate Period 2
            int confirm = JOptionPane.showConfirmDialog(this,
                "Auto-detected: Period 1 (1-15) was already generated.\n\n" +
                "Auto-generate Period 2 (" + period2Start + " to " + period2End + ")?\n\n" +
                "Current date: " + today,
                "Auto-Generate Period 2",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Auto-select Period 2
                List<AttendanceLog> filtered = new java.util.ArrayList<>();
                for (AttendanceLog log : allLogs) {
                    java.time.LocalDate logDate = log.getLogDate().toLocalDate();
                    if (!logDate.isBefore(period2Start) && !logDate.isAfter(period2End)) {
                        filtered.add(log);
                    }
                }
                return filtered;
            }
            // If user says NO, continue with manual selection
        }
        
        // Show month selection
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        
        String monthInput = (String) JOptionPane.showInputDialog(this,
            "Select month:",
            "Select Month for Bi-Weekly Period",
            JOptionPane.QUESTION_MESSAGE,
            null,
            months,
            months[currentMonth - 1]);
        
        if (monthInput == null) return null;
        
        int selectedMonth = java.util.Arrays.asList(months).indexOf(monthInput) + 1;
        
        // Get year
        String yearInput = ThemeManager.showLargeInputDialog(this, 
            "Enter Year (e.g., " + currentYear + "):", 
            "Select Year");
        if (yearInput == null) return null;
        
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Recalculate periods for selected month
        period1Start = java.time.LocalDate.of(selectedYear, selectedMonth, 1);
        period1End = java.time.LocalDate.of(selectedYear, selectedMonth, 15);
        lastDayOfMonth = java.time.LocalDate.of(selectedYear, selectedMonth, 1)
            .withDayOfMonth(java.time.LocalDate.of(selectedYear, selectedMonth, 1).lengthOfMonth());
        period2Start = java.time.LocalDate.of(selectedYear, selectedMonth, 16);
        period2End = lastDayOfMonth;
        
        // Recheck which periods have data for selected month
        period1HasData = hasLogsInRange(allLogs, period1Start, period1End);
        period2HasData = hasLogsInRange(allLogs, period2Start, period2End);
        
        // Build selection message
        StringBuilder message = new StringBuilder();
        message.append("Select bi-weekly period for ").append(monthInput).append(" ").append(selectedYear).append(":\n\n");
        
        if (period1HasData) {
            message.append("1. Period 1: ").append(period1Start).append(" to ").append(period1End).append(" (Has data)\n");
        } else {
            message.append("1. Period 1: ").append(period1Start).append(" to ").append(period1End).append("\n");
        }
        
        if (period2HasData) {
            message.append("2. Period 2: ").append(period2Start).append(" to ").append(period2End).append(" (Has data)\n");
        } else {
            message.append("2. Period 2: ").append(period2Start).append(" to ").append(period2End).append("\n");
        }
        
        message.append("\nNote: If Period 1 was already generated, select Period 2.");
        
        String periodChoice = ThemeManager.showLargeInputDialog(this, message.toString(), "Select Bi-Weekly Period");
        if (periodChoice == null) return null;
        
        java.time.LocalDate weekStart, weekEnd;
        
        try {
            int choice = Integer.parseInt(periodChoice.trim());
            if (choice == 1) {
                weekStart = period1Start;
                weekEnd = period1End;
            } else if (choice == 2) {
                weekStart = period2Start;
                weekEnd = period2End;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid choice! Please select 1 or 2.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter 1 or 2.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Filter logs by date range
        List<AttendanceLog> filtered = new java.util.ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            java.time.LocalDate logDate = log.getLogDate().toLocalDate();
            if (!logDate.isBefore(weekStart) && !logDate.isAfter(weekEnd)) {
                filtered.add(log);
            }
        }
        
        return filtered;
    }
    
    /**
     * Check if there are logs in the specified date range
     */
    private boolean hasLogsInRange(List<AttendanceLog> logs, java.time.LocalDate start, java.time.LocalDate end) {
        for (AttendanceLog log : logs) {
            java.time.LocalDate logDate = log.getLogDate().toLocalDate();
            if (!logDate.isBefore(start) && !logDate.isAfter(end)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Filter logs by years
     */
    private List<AttendanceLog> filterByYears() {
        int currentYear = java.time.LocalDate.now().getYear();
        
        String yearInput = ThemeManager.showLargeInputDialog(this, 
            "Select year (e.g., " + currentYear + "):", 
            "Select Year");
        if (yearInput == null) return null;
        
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Filter logs by year
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        List<AttendanceLog> filtered = new java.util.ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            java.time.LocalDate logDate = log.getLogDate().toLocalDate();
            if (logDate.getYear() == selectedYear) {
                filtered.add(log);
            }
        }
        
        return filtered;
    }
    
    /**
     * Helper method to search for employee with real-time suggestions
     * Returns Employee object if found, null otherwise
     */
    private Employee searchEmployeeWithSuggestions(String dialogTitle) {
        List<Employee> allEmployees = DatabaseOperations.getAllEmployees();
        if (allEmployees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees registered.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        
        // Create custom search dialog with suggestions
        JDialog searchDialog = new JDialog(this, dialogTitle, true);
        searchDialog.setLayout(new BorderLayout());
        searchDialog.setSize(500, 300);
        searchDialog.setLocationRelativeTo(this);
        searchDialog.setResizable(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel for label and text field
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Label
        JLabel label = new JLabel("Enter Employee Number, Employee ID, or Full Name:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(label, BorderLayout.NORTH);
        
        // Text field
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(450, 35));
        topPanel.add(searchField, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Suggestions list - use a panel that can show/hide
        JPanel suggestionsPanel = new JPanel(new BorderLayout());
        suggestionsPanel.setPreferredSize(new Dimension(450, 200));
        suggestionsPanel.setVisible(false);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> suggestionsList = new JList<>(listModel);
        suggestionsList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        suggestionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionsList.setFocusable(true);
        JScrollPane listScrollPane = new JScrollPane(suggestionsList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Suggestions"));
        listScrollPane.setPreferredSize(new Dimension(450, 200));
        suggestionsPanel.add(listScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(suggestionsPanel, BorderLayout.CENTER);
        
        // Store selected input
        final String[] selectedInput = {null};
        
        // Update suggestions as user types
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
            
            private void updateSuggestions() {
                String input = searchField.getText().trim().toLowerCase();
                listModel.clear();
                
                if (input.isEmpty()) {
                    suggestionsPanel.setVisible(false);
                    searchDialog.pack();
                    return;
                }
                
                List<String> matches = new ArrayList<>();
                for (int i = 0; i < allEmployees.size(); i++) {
                    Employee emp = allEmployees.get(i);
                    String empName = emp.getFullName();
                    String empId = emp.getEmployeeId();
                    String empNameNoSpaces = empName.replaceAll("\\s+", "").toLowerCase();
                    String inputNoSpaces = input.replaceAll("\\s+", "");
                    
                    // Check if matches - use contains for better matching
                    boolean matchesInput = empName.toLowerCase().contains(input) ||
                                         empNameNoSpaces.contains(inputNoSpaces) ||
                                         empId.toLowerCase().contains(input) ||
                                         String.valueOf(i + 1).equals(input);
                    
                    if (matchesInput) {
                        matches.add((i + 1) + ". " + empName + " (" + empId + ")");
                    }
                }
                
                if (!matches.isEmpty()) {
                    for (String match : matches) {
                        listModel.addElement(match);
                    }
                    suggestionsPanel.setVisible(true);
                    // Resize dialog to show suggestions
                    int newHeight = Math.min(300 + 200 + (matches.size() > 5 ? 50 : 0), 600);
                    searchDialog.setSize(500, newHeight);
                    searchDialog.validate();
                    searchDialog.repaint();
                } else {
                    suggestionsPanel.setVisible(false);
                    searchDialog.setSize(500, 300);
                    searchDialog.validate();
                    searchDialog.repaint();
                }
            }
        });
        
        // Double-click on list item to select (acts like OK) - more accurate selection
        suggestionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Only handle double-click to ensure accurate selection
                if (e.getClickCount() == 2) {
                    // Get the exact index at the mouse location
                    int index = suggestionsList.locationToIndex(e.getPoint());
                    
                    // Verify the point is actually within the bounds of the item
                    if (index >= 0 && index < listModel.getSize()) {
                        java.awt.Rectangle cellBounds = suggestionsList.getCellBounds(index, index);
                        if (cellBounds != null && cellBounds.contains(e.getPoint())) {
                            String selected = listModel.getElementAt(index);
                            // Extract employee number from "1. Name (ID)"
                            String empNum = selected.split("\\.")[0].trim();
                            selectedInput[0] = empNum;
                            searchDialog.dispose();
                        }
                    }
                }
            }
        });
        
        // Also handle keyboard selection (Enter key)
        suggestionsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = suggestionsList.getSelectedIndex();
                if (index >= 0) {
                    String selected = listModel.getElementAt(index);
                    String empNum = selected.split("\\.")[0].trim();
                    searchField.setText(empNum);
                }
            }
        });
        
        // Enter key on list to confirm selection
        suggestionsList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    int index = suggestionsList.getSelectedIndex();
                    if (index >= 0) {
                        String selected = listModel.getElementAt(index);
                        String empNum = selected.split("\\.")[0].trim();
                        selectedInput[0] = empNum;
                        searchDialog.dispose();
                    }
                }
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        ThemeManager.styleButton(okButton);
        ThemeManager.styleButton(cancelButton);
        
        okButton.addActionListener(e -> {
            selectedInput[0] = searchField.getText().trim();
            searchDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            selectedInput[0] = null;
            searchDialog.dispose();
        });
        
        // Enter key to submit
        searchField.addActionListener(e -> {
            selectedInput[0] = searchField.getText().trim();
            searchDialog.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        searchDialog.add(mainPanel);
        
        searchField.requestFocus();
        searchDialog.setVisible(true);
        
        String input = selectedInput[0];
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        
        input = input.trim();
        Employee foundEmployee = null;
        
        // Try to find employee
        // Check if it's a number
        try {
            int empIndex = Integer.parseInt(input);
            if (empIndex >= 1 && empIndex <= allEmployees.size()) {
                foundEmployee = allEmployees.get(empIndex - 1);
            }
        } catch (NumberFormatException e) {
            // Try Employee ID or Name
            String upperInput = input.toUpperCase();
            for (Employee emp : allEmployees) {
                if (emp.getEmployeeId().equalsIgnoreCase(upperInput)) {
                    foundEmployee = emp;
                    break;
                }
                
                String empNameNoSpaces = emp.getFullName().replaceAll("\\s+", "").toLowerCase();
                String inputNoSpaces = input.replaceAll("\\s+", "").toLowerCase();
                
                if (emp.getFullName().equalsIgnoreCase(input) || 
                    emp.getFullName().toLowerCase().contains(input.toLowerCase()) ||
                    empNameNoSpaces.equals(inputNoSpaces) ||
                    empNameNoSpaces.contains(inputNoSpaces)) {
                    foundEmployee = emp;
                    break;
                }
            }
        }
        
        if (foundEmployee == null) {
            JOptionPane.showMessageDialog(this, 
                "❌ Employee not found!", 
                "Employee Not Found", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        return foundEmployee;
    }
    
    /**
     * Helper method to search for log with real-time suggestions
     * Returns AttendanceLog object if found, null otherwise
     */
    private AttendanceLog searchLogWithSuggestions(List<AttendanceLog> logs, String dialogTitle) {
        if (logs == null || logs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No attendance logs found.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        
        // Create custom search dialog with suggestions
        JDialog searchDialog = new JDialog(this, dialogTitle, true);
        searchDialog.setLayout(new BorderLayout());
        searchDialog.setSize(600, 400);
        searchDialog.setLocationRelativeTo(this);
        searchDialog.setResizable(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel for label and text field
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Label
        JLabel label = new JLabel("Enter Log ID, Employee ID, or Employee Name:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(label, BorderLayout.NORTH);
        
        // Text field
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(550, 35));
        topPanel.add(searchField, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Suggestions list - use a panel that can show/hide
        JPanel suggestionsPanel = new JPanel(new BorderLayout());
        suggestionsPanel.setPreferredSize(new Dimension(550, 250));
        suggestionsPanel.setVisible(false);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> suggestionsList = new JList<>(listModel);
        suggestionsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        suggestionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionsList.setFocusable(true);
        JScrollPane listScrollPane = new JScrollPane(suggestionsList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Suggestions"));
        listScrollPane.setPreferredSize(new Dimension(550, 250));
        suggestionsPanel.add(listScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(suggestionsPanel, BorderLayout.CENTER);
        
        // Store selected input
        final AttendanceLog[] selectedLog = {null};
        
        // Update suggestions as user types
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions();
            }
            
            private void updateSuggestions() {
                String input = searchField.getText().trim().toLowerCase();
                listModel.clear();
                
                if (input.isEmpty()) {
                    suggestionsPanel.setVisible(false);
                    searchDialog.pack();
                    return;
                }
                
                List<String> matches = new ArrayList<>();
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                
                for (AttendanceLog log : logs) {
                    String logIdStr = String.valueOf(log.getId());
                    String empId = log.getEmployeeId();
                    String empName = log.getEmployeeName();
                    String empNameNoSpaces = empName.replaceAll("\\s+", "").toLowerCase();
                    String inputNoSpaces = input.replaceAll("\\s+", "");
                    String dateStr = dateFormat.format(log.getLogDate());
                    
                    // Check if matches
                    boolean matchesInput = logIdStr.equals(input) ||
                                         empId.toLowerCase().contains(input) ||
                                         empName.toLowerCase().contains(input) ||
                                         empNameNoSpaces.contains(inputNoSpaces);
                    
                    if (matchesInput) {
                        String clockIn = log.getClockIn() != null ? log.getFormattedClockIn() : "N/A";
                        String clockOut = log.getClockOut() != null ? log.getFormattedClockOut() : "N/A";
                        matches.add("Log #" + logIdStr + " - " + empName + " (" + empId + ") - " + dateStr + " [" + clockIn + " - " + clockOut + "]");
                    }
                }
                
                if (!matches.isEmpty()) {
                    for (String match : matches) {
                        listModel.addElement(match);
                    }
                    suggestionsPanel.setVisible(true);
                    // Resize dialog to show suggestions
                    int newHeight = Math.min(300 + 250 + (matches.size() > 5 ? 50 : 0), 700);
                    searchDialog.setSize(600, newHeight);
                    searchDialog.validate();
                    searchDialog.repaint();
                } else {
                    suggestionsPanel.setVisible(false);
                    searchDialog.setSize(600, 300);
                    searchDialog.validate();
                    searchDialog.repaint();
                }
            }
        });
        
        // Double-click on list item to select (acts like OK) - more accurate selection
        suggestionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Only handle double-click to ensure accurate selection
                if (e.getClickCount() == 2) {
                    // Get the exact index at the mouse location
                    int index = suggestionsList.locationToIndex(e.getPoint());
                    
                    // Verify the point is actually within the bounds of the item
                    if (index >= 0 && index < listModel.getSize()) {
                        java.awt.Rectangle cellBounds = suggestionsList.getCellBounds(index, index);
                        if (cellBounds != null && cellBounds.contains(e.getPoint())) {
                            String selected = listModel.getElementAt(index);
                            // Extract Log ID from "Log #123 - Name (ID) - Date [Time]"
                            String logIdStr = selected.split(" - ")[0].replace("Log #", "").trim();
                            try {
                                int logId = Integer.parseInt(logIdStr);
                                for (AttendanceLog log : logs) {
                                    if (log.getId() == logId) {
                                        selectedLog[0] = log;
                                        searchDialog.dispose();
                                        return;
                                    }
                                }
                            } catch (NumberFormatException ex) {
                                // Ignore
                            }
                        }
                    }
                }
            }
        });
        
        // Also handle keyboard selection (Enter key)
        suggestionsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = suggestionsList.getSelectedIndex();
                if (index >= 0) {
                    String selected = listModel.getElementAt(index);
                    // Extract Log ID and set in search field
                    String logIdStr = selected.split(" - ")[0].replace("Log #", "").trim();
                    searchField.setText(logIdStr);
                }
            }
        });
        
        // Enter key on list to confirm selection
        suggestionsList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    int index = suggestionsList.getSelectedIndex();
                    if (index >= 0) {
                        String selected = listModel.getElementAt(index);
                        String logIdStr = selected.split(" - ")[0].replace("Log #", "").trim();
                        try {
                            int logId = Integer.parseInt(logIdStr);
                            for (AttendanceLog log : logs) {
                                if (log.getId() == logId) {
                                    selectedLog[0] = log;
                                    searchDialog.dispose();
                                    return;
                                }
                            }
                        } catch (NumberFormatException ex) {
                            // Ignore
                        }
                    }
                }
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        ThemeManager.styleButton(okButton);
        ThemeManager.styleButton(cancelButton);
        
        okButton.addActionListener(e -> {
            String input = searchField.getText().trim();
            if (!input.isEmpty()) {
                // Try to find log by input
                try {
                    int logId = Integer.parseInt(input);
                    for (AttendanceLog log : logs) {
                        if (log.getId() == logId) {
                            selectedLog[0] = log;
                            break;
                        }
                    }
                } catch (NumberFormatException ex) {
                    // Try Employee ID or Name
                    String upperInput = input.toUpperCase();
                    String inputNoSpaces = input.replaceAll("\\s+", "").toLowerCase();
                    
                    for (AttendanceLog log : logs) {
                        if (log.getEmployeeId().equalsIgnoreCase(upperInput)) {
                            selectedLog[0] = log;
                            break;
                        }
                        
                        String empNameNoSpaces = log.getEmployeeName().replaceAll("\\s+", "").toLowerCase();
                        if (log.getEmployeeName().equalsIgnoreCase(input) ||
                            log.getEmployeeName().toLowerCase().contains(input.toLowerCase()) ||
                            empNameNoSpaces.equals(inputNoSpaces) ||
                            empNameNoSpaces.contains(inputNoSpaces)) {
                            selectedLog[0] = log;
                            break;
                        }
                    }
                }
            }
            searchDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            selectedLog[0] = null;
            searchDialog.dispose();
        });
        
        // Enter key to submit
        searchField.addActionListener(e -> {
            String input = searchField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int logId = Integer.parseInt(input);
                    for (AttendanceLog log : logs) {
                        if (log.getId() == logId) {
                            selectedLog[0] = log;
                            break;
                        }
                    }
                } catch (NumberFormatException ex) {
                    String upperInput = input.toUpperCase();
                    String inputNoSpaces = input.replaceAll("\\s+", "").toLowerCase();
                    
                    for (AttendanceLog log : logs) {
                        if (log.getEmployeeId().equalsIgnoreCase(upperInput)) {
                            selectedLog[0] = log;
                            break;
                        }
                        
                        String empNameNoSpaces = log.getEmployeeName().replaceAll("\\s+", "").toLowerCase();
                        if (log.getEmployeeName().equalsIgnoreCase(input) ||
                            log.getEmployeeName().toLowerCase().contains(input.toLowerCase()) ||
                            empNameNoSpaces.equals(inputNoSpaces) ||
                            empNameNoSpaces.contains(inputNoSpaces)) {
                            selectedLog[0] = log;
                            break;
                        }
                    }
                }
            }
            searchDialog.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        searchDialog.add(mainPanel);
        
        searchField.requestFocus();
        searchDialog.setVisible(true);
        
        return selectedLog[0];
    }
    
    /**
     * Filter logs by employee with real-time search suggestions
     */
    private List<AttendanceLog> filterByEmployee() {
        Employee foundEmployee = searchEmployeeWithSuggestions("Search Employee");
        if (foundEmployee == null) {
            return null;
        }
        
        // Filter logs by employee
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        List<AttendanceLog> filtered = new java.util.ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            if (log.getEmployeeId().equals(foundEmployee.getEmployeeId())) {
                filtered.add(log);
            }
        }
        
        return filtered;
    }
    
    /**
     * Generate custom report with filtered logs
     */
    private boolean generateCustomReport(List<AttendanceLog> logs, String filePath, String reportTitle) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.text.SimpleDateFormat reportFormat = new java.text.SimpleDateFormat("MMMM dd, yyyy hh:mm a");
            
            // Report header
            writer.println("=================================================");
            writer.println("    BARANGAY ATTENDANCE SYSTEM REPORT");
            writer.println("    " + reportTitle.toUpperCase());
            writer.println("=================================================");
            writer.println("Generated on: " + reportFormat.format(new java.util.Date()));
            writer.println("Total Records: " + logs.size());
            writer.println();
            
            // Summary statistics
            int totalEmployees = 0;
            double totalHours = 0;
            java.util.Set<String> uniqueEmployees = new java.util.HashSet<>();
            
            for (AttendanceLog log : logs) {
                if (log.getClockIn() != null) {
                    uniqueEmployees.add(log.getEmployeeId());
                    
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
            
            totalEmployees = uniqueEmployees.size();
            
            writer.println("SUMMARY STATISTICS:");
            writer.println("----------------");
            writer.printf("Total Unique Employees: %d%n", totalEmployees);
            writer.printf("Total Hours Worked: %.1f%n", totalHours);
            writer.printf("Average Hours per Employee: %.1f%n", 
                totalEmployees > 0 ? totalHours / totalEmployees : 0);
            writer.println();
            
            // Detailed records
            writer.println("DETAILED ATTENDANCE RECORDS:");
            writer.println("----------------------------");
            writer.printf("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
                "Date", "Employee ID", "Name", "Clock In", "Clock Out", "Hours");
            writer.printf("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
                "----", "-----------", "----", "--------", "--------", "-----");
            
            for (AttendanceLog log : logs) {
                String date = dateFormat.format(log.getLogDate());
                String empId = log.getEmployeeId();
                String empName = log.getEmployeeName();
                String clockIn = log.getClockIn() != null ? log.getFormattedClockIn() : "";
                String clockOut = log.getClockOut() != null ? log.getFormattedClockOut() : "";
                String hoursWorked = log.getHoursWorked();
                
                writer.printf("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
                    date, empId, empName, clockIn, clockOut, hoursWorked);
            }
            
            writer.println();
            writer.println("=================================================");
            writer.println("    END OF REPORT");
            writer.println("=================================================");
            
            return true;
        } catch (java.io.IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Show report in a dialog
     */
    private void showReportDialog(List<AttendanceLog> logs, String reportTitle) {
        // Create report text
        StringBuilder reportText = new StringBuilder();
        reportText.append("=================================================\n");
        reportText.append("    BARANGAY ATTENDANCE SYSTEM REPORT\n");
        reportText.append("    ").append(reportTitle.toUpperCase()).append("\n");
        reportText.append("=================================================\n");
        reportText.append("Total Records: ").append(logs.size()).append("\n\n");
        
        // Summary
        java.util.Set<String> uniqueEmployees = new java.util.HashSet<>();
        double totalHours = 0;
        
        for (AttendanceLog log : logs) {
            if (log.getClockIn() != null) {
                uniqueEmployees.add(log.getEmployeeId());
                
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
        
        reportText.append("SUMMARY:\n");
        reportText.append("Total Unique Employees: ").append(uniqueEmployees.size()).append("\n");
        reportText.append("Total Hours Worked: ").append(String.format("%.1f", totalHours)).append("\n\n");
        
        // Table header
        reportText.append(String.format("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
            "Date", "Employee ID", "Name", "Clock In", "Clock Out", "Hours"));
        reportText.append(String.format("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
            "----", "-----------", "----", "--------", "--------", "-----"));
        
        // Table data
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        for (AttendanceLog log : logs) {
            String date = dateFormat.format(log.getLogDate());
            String empId = log.getEmployeeId();
            String empName = log.getEmployeeName();
            String clockIn = log.getClockIn() != null ? log.getFormattedClockIn() : "";
            String clockOut = log.getClockOut() != null ? log.getFormattedClockOut() : "";
            String hoursWorked = log.getHoursWorked();
            
            reportText.append(String.format("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
                date, empId, empName, clockIn, clockOut, hoursWorked));
        }
        
        // Display in custom dialog with Generate Report button
        JTextArea textArea = new JTextArea(reportText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Larger font
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = (int)(screenSize.width * 0.9); // 90% width
        int dialogHeight = (int)(screenSize.height * 0.85); // 85% height
        scrollPane.setPreferredSize(new Dimension(dialogWidth, dialogHeight));
        
        // Create custom dialog with buttons
        JDialog reportDialog = new JDialog(this, reportTitle, true);
        reportDialog.setLayout(new BorderLayout());
        reportDialog.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel with OK and Generate Report
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton generateButton = new JButton("📄 Generate Report");
        ThemeManager.styleButton(okButton);
        ThemeManager.styleButton(generateButton);
        
        okButton.addActionListener(e -> reportDialog.dispose());
        
        generateButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        fileChooser.setSelectedFile(new java.io.File(ExcelExporter.getExportFilePath("attendance_report", "txt")));
        
            int result = fileChooser.showSaveDialog(reportDialog);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }
            
                if (generateCustomReport(logs, filePath, reportTitle)) {
                    JOptionPane.showMessageDialog(reportDialog, 
                        "✅ Attendance report generated successfully!\n\nFile: " + filePath, 
                        "Report Success", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                    JOptionPane.showMessageDialog(reportDialog, 
                        "❌ Error generating report!", 
                        "Report Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(generateButton);
        buttonPanel.add(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);
        reportDialog.setSize(dialogWidth + 50, dialogHeight + 100);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setVisible(true);
    }
    
    private void viewStatistics() {
        // Create custom large dialog for statistics type selection
        JDialog statTypeDialog = new JDialog(this, "View Attendance Statistics", true);
        statTypeDialog.setSize(900, 650);
        statTypeDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
        
        // Title label - Large and clear
        JLabel titleLabel = new JLabel("Select Attendance Statistics Type:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Options panel with large buttons
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        optionsPanel.setBackground(Color.WHITE);
        
        String[] statTypes = {
            "📅 MONTHLY STATISTICS - Monthly attendance stats",
            "📊 YEARLY STATISTICS - Yearly attendance stats",
            "👤 EMPLOYEE ATTENDANCE - Employee attendance rate",
            "📈 OVERALL RATE - Overall attendance rate"
        };
        
        JButton[] optionButtons = new JButton[4];
        final int[] selectedChoice = {-1};
        
        for (int i = 0; i < statTypes.length; i++) {
            final int index = i;
            optionButtons[i] = new JButton(statTypes[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(getForeground());
                    FontMetrics fm = g2.getFontMetrics();
                    String text = getText();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.setFont(getFont());
                    g2.drawString(text, x, y);
                    g2.dispose();
                }
            };
            optionButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 22));
            optionButtons[i].setPreferredSize(new Dimension(800, 90));
            // Use same blue color as admin dashboard buttons
            optionButtons[i].setBackground(ThemeManager.PRIMARY_COLOR); // Blue like dashboard
            optionButtons[i].setForeground(Color.WHITE);
            optionButtons[i].setOpaque(true);
            optionButtons[i].setContentAreaFilled(false); // We paint it ourselves
            optionButtons[i].setBorderPainted(false);
            optionButtons[i].setFocusPainted(false);
            
            // Hover effect - light blue when hovering (same as dashboard)
            optionButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    optionButtons[index].setBackground(ThemeManager.SECONDARY_COLOR); // Light blue
                    optionButtons[index].repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    optionButtons[index].setBackground(ThemeManager.PRIMARY_COLOR); // Blue
                    optionButtons[index].repaint();
                }
            });
            
            optionButtons[i].addActionListener(e -> {
                selectedChoice[0] = index;
                statTypeDialog.dispose();
            });
            
            optionsPanel.add(optionButtons[i]);
        }
        
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        
        // Cancel button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.addActionListener(e -> {
            selectedChoice[0] = -1;
            statTypeDialog.dispose();
        });
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        statTypeDialog.add(mainPanel);
        statTypeDialog.setVisible(true);
        
        int statTypeChoice = selectedChoice[0];
        if (statTypeChoice < 0) return; // User cancelled
        
        // Create custom large dialog for statistics action selection
        JDialog actionDialog = new JDialog(this, "Statistics Action", true);
        actionDialog.setSize(700, 400);
        actionDialog.setLocationRelativeTo(this);
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        actionPanel.setBackground(Color.WHITE);
        
        // Title label - Large and clear
        JLabel actionTitleLabel = new JLabel("What would you like to do?", SwingConstants.CENTER);
        actionTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        actionTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        actionPanel.add(actionTitleLabel, BorderLayout.NORTH);
        
        // Options panel with large buttons
        JPanel actionOptionsPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        actionOptionsPanel.setBackground(Color.WHITE);
        
        String[] actions = {"⚙️ Process attendance data", "👁️ Show Attendance Statistics"};
        JButton[] actionButtons = new JButton[2];
        final int[] selectedAction = {-1};
        
        for (int i = 0; i < actions.length; i++) {
            final int index = i;
            actionButtons[i] = new JButton(actions[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(getForeground());
                    FontMetrics fm = g2.getFontMetrics();
                    String text = getText();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.setFont(getFont());
                    g2.drawString(text, x, y);
                    g2.dispose();
                }
            };
            actionButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 22));
            actionButtons[i].setPreferredSize(new Dimension(600, 80));
            // Use same blue color as admin dashboard buttons
            actionButtons[i].setBackground(ThemeManager.PRIMARY_COLOR); // Blue like dashboard
            actionButtons[i].setForeground(Color.WHITE);
            actionButtons[i].setOpaque(true);
            actionButtons[i].setContentAreaFilled(false); // We paint it ourselves
            actionButtons[i].setBorderPainted(false);
            actionButtons[i].setFocusPainted(false);
            
            // Hover effect - light blue when hovering (same as dashboard)
            actionButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    actionButtons[index].setBackground(ThemeManager.SECONDARY_COLOR); // Light blue
                    actionButtons[index].repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    actionButtons[index].setBackground(ThemeManager.PRIMARY_COLOR); // Blue
                    actionButtons[index].repaint();
                }
            });
            
            actionButtons[i].addActionListener(e -> {
                selectedAction[0] = index;
                actionDialog.dispose();
            });
            
            actionOptionsPanel.add(actionButtons[i]);
        }
        
        actionPanel.add(actionOptionsPanel, BorderLayout.CENTER);
        
        // Cancel button
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionButtonPanel.setBackground(Color.WHITE);
        JButton actionCancelButton = new JButton("Cancel");
        actionCancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        actionCancelButton.setPreferredSize(new Dimension(150, 50));
        actionCancelButton.addActionListener(e -> {
            selectedAction[0] = -1;
            actionDialog.dispose();
        });
        actionButtonPanel.add(actionCancelButton);
        actionPanel.add(actionButtonPanel, BorderLayout.SOUTH);
        
        actionDialog.add(actionPanel);
        actionDialog.setVisible(true);
        
        int actionChoice = selectedAction[0];
        if (actionChoice < 0) return; // User cancelled
        
        String statisticsText = "";
        String statisticsTitle = "";
        
        // Generate statistics based on selected type
        switch (statTypeChoice) {
            case 0: // MONTHLY STATISTICS
                statisticsText = generateMonthlyStatistics();
                statisticsTitle = "Monthly Attendance Statistics";
                break;
            case 1: // YEARLY STATISTICS
                statisticsText = generateYearlyStatistics();
                statisticsTitle = "Yearly Attendance Statistics";
                break;
            case 2: // EMPLOYEE ATTENDANCE
                statisticsText = generateEmployeeStatistics();
                statisticsTitle = "Employee Attendance Statistics";
                break;
            case 3: // OVERALL RATE
                statisticsText = generateOverallStatistics();
                statisticsTitle = "Overall Attendance Rate";
                break;
        }
        
        if (statisticsText == null || statisticsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No data available for the selected criteria.", 
                "No Data", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (actionChoice == 0) {
            // Process Data - Save to file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files (*.txt)", "txt"));
            fileChooser.setSelectedFile(new java.io.File(ExcelExporter.getExportFilePath("attendance_statistics", "txt")));
            
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".txt")) {
                    filePath += ".txt";
                }
                
                try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {
                    writer.print(statisticsText);
                    JOptionPane.showMessageDialog(this, 
                        "✅ Attendance statistics processed and saved successfully!\n\nFile: " + filePath, 
                        "Statistics Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (java.io.IOException e) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Error saving statistics!", 
                        "Statistics Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // Show Statistics - Display in dialog
            showStatisticsDialog(statisticsText, statisticsTitle);
        }
    }
    
    /**
     * Generate monthly statistics
     */
    private String generateMonthlyStatistics() {
        int currentYear = java.time.LocalDate.now().getYear();
        
        // Show month selection
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        
        String monthInput = (String) JOptionPane.showInputDialog(this,
            "Select month:",
            "Monthly Statistics",
            JOptionPane.QUESTION_MESSAGE,
            null,
            months,
            months[java.time.LocalDate.now().getMonthValue() - 1]);
        
        if (monthInput == null) return null;
        
        int selectedMonth = java.util.Arrays.asList(months).indexOf(monthInput) + 1;
        
        // Get year
        String yearInput = ThemeManager.showLargeInputDialog(this, 
            "Enter Year (e.g., " + currentYear + "):", 
            "Select Year");
        if (yearInput == null) return null;
        
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Filter logs by month and year
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        List<AttendanceLog> filtered = new ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            java.time.LocalDate logDate = log.getLogDate().toLocalDate();
            if (logDate.getYear() == selectedYear && logDate.getMonthValue() == selectedMonth) {
                filtered.add(log);
            }
        }
        
        return calculateStatistics(filtered, "Monthly Statistics for " + monthInput + " " + selectedYear);
    }
    
    /**
     * Generate yearly statistics
     */
    private String generateYearlyStatistics() {
        int currentYear = java.time.LocalDate.now().getYear();
        
        String yearInput = ThemeManager.showLargeInputDialog(this, 
            "Select year (e.g., " + currentYear + "):", 
            "Yearly Statistics");
        if (yearInput == null) return null;
        
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Filter logs by year
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        List<AttendanceLog> filtered = new ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            java.time.LocalDate logDate = log.getLogDate().toLocalDate();
            if (logDate.getYear() == selectedYear) {
                filtered.add(log);
            }
        }
        
        return calculateStatistics(filtered, "Yearly Statistics for " + selectedYear);
    }
    
    /**
     * Generate employee statistics
     */
    private String generateEmployeeStatistics() {
        Employee foundEmployee = searchEmployeeWithSuggestions("Employee Statistics");
        if (foundEmployee == null) {
            return null;
        }
        
        String empId = foundEmployee.getEmployeeId();
        String empName = foundEmployee.getFullName();
        
        // Filter logs by employee
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        List<AttendanceLog> filtered = new ArrayList<>();
        
        for (AttendanceLog log : allLogs) {
            if (log.getEmployeeId().equals(empId)) {
                filtered.add(log);
            }
        }
        
        return calculateEmployeeStatistics(filtered, empName, empId);
    }
    
    /**
     * Generate overall statistics
     */
    private String generateOverallStatistics() {
        List<AttendanceLog> allLogs = DatabaseOperations.getAllAttendanceLogs();
        return calculateStatistics(allLogs, "Overall Attendance Statistics");
    }
    
    /**
     * Calculate general statistics in table format
     */
    private String calculateStatistics(List<AttendanceLog> logs, String title) {
        if (logs == null || logs.isEmpty()) {
            return "No data available.";
        }
        
        // Get all employees
        List<Employee> allEmployees = DatabaseOperations.getAllEmployees();
        
        // Create a map to store statistics per employee
        Map<String, EmployeeStats> employeeStatsMap = new HashMap<>();
        
        // Initialize all employees
        for (Employee emp : allEmployees) {
            employeeStatsMap.put(emp.getEmployeeId(), new EmployeeStats(emp.getFullName(), emp.getEmployeeId()));
        }
        
        // Calculate statistics for each employee
        for (AttendanceLog log : logs) {
            String empId = log.getEmployeeId();
            EmployeeStats stats = employeeStatsMap.get(empId);
            
            if (stats != null) {
                stats.totalRecords++;
                if (log.getClockIn() != null) {
                    stats.totalClockIns++;
                }
                if (log.getClockOut() != null) {
                    stats.totalClockOuts++;
                }
            }
        }
        
        // Build table format
        StringBuilder stats = new StringBuilder();
        stats.append("=================================================\n");
        stats.append("    ").append(title.toUpperCase()).append("\n");
        stats.append("=================================================\n\n");
        
        // Table header (4 columns only)
        stats.append(String.format("%-25s %-15s %-18s %-18s\n", 
            "Full name", "Total record", "total clock ins", "total clock outs"));
        stats.append(String.format("%-25s %-15s %-18s %-18s\n", 
            "-------------------------", "---------------", "------------------", "------------------"));
        
        // Table rows - one per employee (4 columns)
        int totalEmployees = allEmployees.size();
        for (Employee emp : allEmployees) {
            EmployeeStats empStats = employeeStatsMap.get(emp.getEmployeeId());
            if (empStats != null) {
                stats.append(String.format("%-25s %-15d %-18d %-18d\n", 
                    empStats.fullName, 
                    empStats.totalRecords, 
                    empStats.totalClockIns, 
                    empStats.totalClockOuts));
            }
        }
        
        // Summary - total employees at the bottom, right-aligned under "total clock outs" column
        stats.append("\n");
        // Right-align the total employees number under the "total clock outs" column
        // Width of first 3 columns: 25 + 15 + 18 = 58, then right-align in the 18-width column
        stats.append(String.format("%58s%18d\n", "", totalEmployees));
        
        stats.append("\n");
        
        return stats.toString();
    }
    
    /**
     * Helper class to store employee statistics
     */
    private static class EmployeeStats {
        String fullName;
        String employeeId;
        int totalRecords = 0;
        int totalClockIns = 0;
        int totalClockOuts = 0;
        
        EmployeeStats(String fullName, String employeeId) {
            this.fullName = fullName;
            this.employeeId = employeeId;
        }
    }
    
    /**
     * Calculate employee-specific statistics
     */
    private String calculateEmployeeStatistics(List<AttendanceLog> logs, String empName, String empId) {
        if (logs == null || logs.isEmpty()) {
            return "No attendance records found for this employee.";
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=================================================\n");
        stats.append("    EMPLOYEE ATTENDANCE STATISTICS\n");
        stats.append("=================================================\n\n");
        stats.append("Employee: ").append(empName).append("\n");
        stats.append("Employee ID: ").append(empId).append("\n\n");
        
        int totalLogs = logs.size();
        int totalClockIns = 0;
        int totalClockOuts = 0;
        double totalHours = 0;
        Set<String> uniqueDates = new HashSet<>();
        
        for (AttendanceLog log : logs) {
            if (log.getClockIn() != null) {
                totalClockIns++;
            }
            if (log.getClockOut() != null) {
                totalClockOuts++;
            }
            
            uniqueDates.add(log.getLogDate().toString());
            
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
        
        // Calculate attendance rate (days present / total possible days)
        // For simplicity, we'll use unique dates as attendance days
        int attendanceDays = uniqueDates.size();
        
        stats.append("EMPLOYEE STATISTICS:\n");
        stats.append("-------------------\n");
        stats.append(String.format("Total Records: %d\n", totalLogs));
        stats.append(String.format("Total Clock-Ins: %d\n", totalClockIns));
        stats.append(String.format("Total Clock-Outs: %d\n", totalClockOuts));
        stats.append(String.format("Days Present: %d\n", attendanceDays));
        stats.append(String.format("Total Hours Worked: %.1f hours\n", totalHours));
        stats.append(String.format("Average Hours per Day: %.1f hours\n", 
            attendanceDays > 0 ? totalHours / attendanceDays : 0));
        stats.append("\n");
        
        // Recent attendance (last 10 records)
        stats.append("RECENT ATTENDANCE:\n");
        stats.append("------------------\n");
        int count = 0;
        for (int i = logs.size() - 1; i >= 0 && count < 10; i--, count++) {
            AttendanceLog log = logs.get(i);
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(log.getLogDate());
            String clockIn = log.getClockIn() != null ? log.getFormattedClockIn() : "N/A";
            String clockOut = log.getClockOut() != null ? log.getFormattedClockOut() : "N/A";
            String hours = log.getHoursWorked();
            
            stats.append(String.format("%s | In: %s | Out: %s | Hours: %s\n", 
                date, clockIn, clockOut, hours.isEmpty() ? "N/A" : hours));
        }
        
        return stats.toString();
    }
    
    /**
     * Show statistics in a dialog
     */
    private void showStatisticsDialog(String statisticsText, String title) {
        JTextArea textArea = new JTextArea(statisticsText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Larger font
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = (int)(screenSize.width * 0.9); // 90% width
        int dialogHeight = (int)(screenSize.height * 0.85); // 85% height
        scrollPane.setPreferredSize(new Dimension(dialogWidth, dialogHeight));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show large error dialog for better visibility
     */
    private void showLargeErrorDialog(Component parent, String message, String title) {
        JDialog errorDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parent), title, true);
        errorDialog.setSize(700, 300);
        errorDialog.setLocationRelativeTo(parent);
        
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        errorPanel.setBackground(Color.WHITE);
        
        JLabel errorLabel = new JLabel("<html><div style='font-size: 22px; text-align: center; width: 640px;'>" + 
                                       message.replace("\n", "<br>") + "</div></html>");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        errorLabel.setForeground(ThemeManager.DANGER_COLOR);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        
        JButton errorOkButton = new JButton("OK");
        ThemeManager.styleButton(errorOkButton);
        errorOkButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        errorOkButton.setPreferredSize(new Dimension(150, 50));
        errorOkButton.addActionListener(e -> errorDialog.dispose());
        
        JPanel errorButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorButtonPanel.setBackground(Color.WHITE);
        errorButtonPanel.add(errorOkButton);
        errorPanel.add(errorButtonPanel, BorderLayout.SOUTH);
        
        errorDialog.add(errorPanel);
        errorDialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close admin dashboard
            new LoginFrame().setVisible(true); // Open login frame
        }
    }
    
    private String generateEmployeeId() {
        List<Employee> employees = DatabaseOperations.getAllEmployees();
        int maxNumber = 0;
        
        // Find the highest existing employee number
        for (Employee emp : employees) {
            String empId = emp.getEmployeeId();
            if (empId.startsWith("EMP")) {
                try {
                    String numberPart = empId.substring(3); // Remove "EMP"
                    int number = Integer.parseInt(numberPart);
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid formats
                }
            }
        }
        
        // Generate next number
        int nextNumber = maxNumber + 1;
        return "EMP" + String.format("%03d", nextNumber); // Format as EMP001, EMP002, etc.
    }
}
