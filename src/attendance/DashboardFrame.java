package attendance;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    JButton btnScanID, btnManual, btnLogout;

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

        btnScanID = new JButton("📷 Scan Employee ID");
        btnManual = new JButton("✍️ Manual Entry");

        Dimension buttonSize = new Dimension(450, 100);
        
        // Style buttons
        ThemeManager.styleSuccessButton(btnScanID);
        ThemeManager.styleButton(btnManual);
        
        btnScanID.setPreferredSize(buttonSize);
        btnManual.setPreferredSize(buttonSize);

        panel.add(btnScanID);
        panel.add(btnManual);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnScanID.addActionListener(e -> scanEmployeeID());
        btnManual.addActionListener(e -> manualEntry());
        btnLogout.addActionListener(e -> logout());
    }

    private void scanEmployeeID() {
        String empId;
        
        // Employee ID validation
        while (true) {
            empId = ThemeManager.showLargeInputDialog(this, "Enter/Scan Employee ID:", "Employee ID");
            if (empId == null) return;
            
            String error = InputValidator.getEmpIdErrorMessage(empId);
            if (error.isEmpty()) {
                empId = empId.trim().toUpperCase();
                break;
            }
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        String name = DatabaseOperations.getEmployeeName(empId);
        if (name == null) {
            JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
            JOptionPane.showMessageDialog(this, "You have already completed both Clock IN and OUT today!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, message, "Confirm Action", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (DatabaseOperations.saveAttendanceLog(empId, name, buttonText.contains("IN") ? "Clock IN" : "Clock OUT")) {
                JOptionPane.showMessageDialog(this, buttonText + " recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error recording attendance!", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close employee dashboard
            new LoginFrame().setVisible(true); // Open login frame
        }
    }
}
