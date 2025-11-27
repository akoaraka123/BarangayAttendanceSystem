package attendance;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

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
        
        // Show the generated ID to user
        JOptionPane.showMessageDialog(this, 
            "Generated Employee ID: " + empId + "\n\nPlease continue with employee registration.", 
            "Employee ID Generated", 
            JOptionPane.INFORMATION_MESSAGE);

        // Full Name validation
        while (true) {
            fullName = ThemeManager.showLargeInputDialog(this, "Enter Full Name:", "Full Name");
            if (fullName == null) return;
            
            String error = InputValidator.getNameErrorMessage(fullName);
            if (error.isEmpty()) {
                fullName = InputValidator.capitalizeName(fullName);
                break;
            }
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        // Position validation
        while (true) {
            position = ThemeManager.showLargeInputDialog(this, "Enter Position:", "Position");
            if (position == null) return;
            
            String error = InputValidator.getPositionErrorMessage(position);
            if (error.isEmpty()) {
                position = InputValidator.capitalizeName(position);
                break;
            }
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        // Check if employee already exists
        if (DatabaseOperations.isEmployeeRegistered(empId, fullName)) {
            JOptionPane.showMessageDialog(this, "Employee already registered!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Register employee
        if (DatabaseOperations.registerEmployee(empId, fullName, contact, address, position)) {
            JOptionPane.showMessageDialog(this, "Employee registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error registering employee!", "Error", JOptionPane.ERROR_MESSAGE);
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(850, 450));

        String[] options = {"OK", "REMOVE"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Attendance Logs",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) { // REMOVE
            String adminPass = ThemeManager.showLargeInputDialog(this, "Enter Admin Password:", "Admin Password");
            if (!"admin123".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String inputId = ThemeManager.showLargeInputDialog(this, "Enter the Log ID to remove:", "Remove Log");
            int logId;
            try { 
                logId = Integer.parseInt(inputId); 
            } catch (NumberFormatException e) { 
                JOptionPane.showMessageDialog(this, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE); 
                return; 
            }

            // Find the log
            AttendanceLog logToRemove = null;
            for (AttendanceLog log : logs) {
                if (log.getId() == logId) {
                    logToRemove = log;
                    break;
                }
            }

            if (logToRemove == null) {
                JOptionPane.showMessageDialog(this, "Log ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

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

        String[] columnNames = {"#", "Employee ID", "Full Name", "Contact", "Address", "Position"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            Object[] row = {i + 1, emp.getEmployeeId(), emp.getFullName(), 
                          emp.getContactNumber(), emp.getAddress(), emp.getPosition()};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setEnabled(false);
        table.setFillsViewportHeight(true);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(4).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 400));

        String[] options = {"OK", "EDIT"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Registered Employees",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) {
            String inputIndex = ThemeManager.showLargeInputDialog(this, "Enter the employee number to edit:", "Edit Employee");
            int empIndex;
            try { 
                empIndex = Integer.parseInt(inputIndex); 
            } catch (NumberFormatException e) { 
                JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE); 
                return; 
            }

            if (empIndex < 1 || empIndex > employees.size()) { 
                JOptionPane.showMessageDialog(this, "Employee number out of range!", "Error", JOptionPane.ERROR_MESSAGE); 
                return; 
            }

            Employee emp = employees.get(empIndex - 1);
            
            String newEmpId = ThemeManager.showLargeInputDialog(this, "Edit Employee ID:", "Edit Employee ID");
            if (newEmpId == null) return;
            String newName = ThemeManager.showLargeInputDialog(this, "Edit Full Name:", "Edit Full Name");
            if (newName == null) return;
            String newContact = ThemeManager.showLargeInputDialog(this, "Edit Contact Number:", "Edit Contact Number");
            if (newContact == null) return;
            String newAddress = ThemeManager.showLargeInputDialog(this, "Edit Address:", "Edit Address");
            if (newAddress == null) return;
            String newPosition = ThemeManager.showLargeInputDialog(this, "Edit Position:", "Edit Position");
            if (newPosition == null) return;

            if (DatabaseOperations.updateEmployee(emp.getEmployeeId(), newName, newContact, newAddress, newPosition)) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error updating employee!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeEmployee() {
        List<Employee> employees = DatabaseOperations.getAllEmployees();
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees registered.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Registered Employees:\n");
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            sb.append((i + 1)).append(". ").append(emp.getFullName()).append(" (").append(emp.getEmployeeId()).append(")\n");
        }

        String adminPass = ThemeManager.showLargeInputDialog(this, "Enter Admin Password:", "Admin Password");
        if (!"admin123".equals(adminPass)) { 
            JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }

        String inputIndex = ThemeManager.showLargeInputDialog(this, sb.toString() + "\nEnter the employee number to remove:", "Remove Employee");
        int empIndex;
        try { 
            empIndex = Integer.parseInt(inputIndex); 
        } catch (NumberFormatException e) { 
            JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }

        if (empIndex < 1 || empIndex > employees.size()) { 
            JOptionPane.showMessageDialog(this, "Employee number out of range!", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }

        Employee emp = employees.get(empIndex - 1);

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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        fileChooser.setSelectedFile(new java.io.File(ExcelExporter.getExportFilePath("attendance_report", "txt")));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }
            
            if (DatabaseOperations.generateAttendanceReport(filePath)) {
                JOptionPane.showMessageDialog(this, "Attendance report generated successfully!\nFile: " + filePath, 
                    "Report Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error generating report!", "Report Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewStatistics() {
        String stats = DatabaseOperations.getAttendanceStatistics();
        JTextArea textArea = new JTextArea(stats);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Attendance Statistics", JOptionPane.INFORMATION_MESSAGE);
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
