package attendance;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminDashboardFrame extends JFrame {
    private JButton btnRegister, btnViewLogs, btnViewEmployees, btnRemove;

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ====== Header ======
        JLabel header = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 36));
        header.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        // ====== Button Panel ======
        JPanel panel = new JPanel(new GridLayout(4, 1, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 500, 100, 500));

        btnRegister = new JButton("Register Employee");
        btnViewLogs = new JButton("View Attendance Logs");
        btnViewEmployees = new JButton("View & Edit Registered Employees");
        btnRemove = new JButton("Remove Employee");

        Dimension buttonSize = new Dimension(450, 70);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 22);
        JButton[] buttons = {btnRegister, btnViewLogs, btnViewEmployees, btnRemove};
        for (JButton b : buttons) {
            b.setPreferredSize(buttonSize);
            b.setFont(buttonFont);
            panel.add(b);
        }

        // Layout
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Actions
        btnRegister.addActionListener(e -> registerEmployee());
        btnViewLogs.addActionListener(e -> viewLogs());
        btnViewEmployees.addActionListener(e -> viewEmployees());
        btnRemove.addActionListener(e -> removeEmployee());
    }

    private void registerEmployee() {
        String empId, fullName, contact, address, position;

        while (true) {
            empId = JOptionPane.showInputDialog(this, "Enter Employee ID:");
            if (empId == null) return;
            if (!empId.trim().isEmpty()) break;
            JOptionPane.showMessageDialog(this, "Employee ID is required!");
        }

        while (true) {
            fullName = JOptionPane.showInputDialog(this, "Enter Full Name:");
            if (fullName == null) return;
            if (!fullName.trim().isEmpty()) break;
            JOptionPane.showMessageDialog(this, "Full Name is required!");
        }

        while (true) {
            contact = JOptionPane.showInputDialog(this, "Enter Contact Number:");
            if (contact == null) return;
            if (!contact.trim().isEmpty()) break;
            JOptionPane.showMessageDialog(this, "Contact Number is required!");
        }

        while (true) {
            address = JOptionPane.showInputDialog(this, "Enter Address:");
            if (address == null) return;
            if (!address.trim().isEmpty()) break;
            JOptionPane.showMessageDialog(this, "Address is required!");
        }

        while (true) {
            position = JOptionPane.showInputDialog(this, "Enter Position:");
            if (position == null) return;
            if (!position.trim().isEmpty()) break;
            JOptionPane.showMessageDialog(this, "Position is required!");
        }

        try (FileWriter fw = new FileWriter("employees.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(empId + "," + fullName + "," + contact + "," + address + "," + position);
            JOptionPane.showMessageDialog(this, "Employee Registered Successfully!");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving employee: " + ex.getMessage());
        }
    }

    private void viewLogs() {
        File file = new File("attendance_logs.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No logs found.");
            return;
        }

        String[] lines;
        try {
            lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading logs file.");
            return;
        }

        String[] columnNames = {"#", "Name", "Employee ID", "Clock IN", "Clock OUT", "Date", "Hours Worked"};
        Object[][] data = new Object[lines.length][7];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            data[i][0] = i + 1;
            for (int j = 0; j < parts.length && j < 6; j++) data[i][j + 1] = parts[j];

            String clockInStr = parts.length > 2 ? parts[2] : "";
            String clockOutStr = parts.length > 3 ? parts[3] : "";
            String hoursWorked = "";

            if (!clockInStr.isEmpty() && !clockOutStr.isEmpty()) {
                try {
                    LocalTime clockIn = LocalTime.parse(clockInStr, formatter);
                    LocalTime clockOut = LocalTime.parse(clockOutStr, formatter);
                    Duration duration = Duration.between(clockIn, clockOut);
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    hoursWorked = hours + "h " + minutes + "m";
                } catch (Exception ignored) {}
            }
            data[i][6] = hoursWorked;
        }

        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                String hoursText = (String) getValueAt(row, 6);
                if (hoursText != null && !hoursText.isEmpty() && hoursText.contains("h")) {
                    try { int h = Integer.parseInt(hoursText.split("h")[0].trim());
                        c.setForeground(h < 8 ? Color.RED : Color.BLACK); }
                    catch (Exception ex) { c.setForeground(Color.BLACK); }
                } else { c.setForeground(Color.BLACK); }
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
            String adminPass = JOptionPane.showInputDialog(this, "Enter Admin Password:");
            if (!"admin123".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.");
                return;
            }

            StringBuilder sb = new StringBuilder("Attendance Logs:\n");
            for (int i = 0; i < lines.length; i++) sb.append(i + 1).append(". ").append(lines[i]).append("\n");

            String inputIndex = JOptionPane.showInputDialog(this, sb.toString() + "\nEnter the log number to remove:");
            int logIndex;
            try { logIndex = Integer.parseInt(inputIndex); }
            catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid number!"); return; }

            if (logIndex < 1 || logIndex > lines.length) {
                JOptionPane.showMessageDialog(this, "Log number out of range!"); return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove log:\n" + lines[logIndex - 1],
                    "Confirm Remove", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String[] newLines = new String[lines.length - 1];
            for (int i = 0, j = 0; i < lines.length; i++)
                if (i != logIndex - 1) newLines[j++] = lines[i];

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : newLines) pw.println(l);
            } catch (IOException e) { JOptionPane.showMessageDialog(this, "Error removing log."); return; }

            JOptionPane.showMessageDialog(this, "Log removed successfully!");
        }
    }

    private void viewEmployees() {
        File file = new File("employees.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No employees registered.");
            return;
        }

        String[] lines;
        try { lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new); }
        catch (IOException e) { JOptionPane.showMessageDialog(this, "Error reading employees file."); return; }

        String[] columnNames = {"#", "Employee ID", "Full Name", "Contact", "Address", "Position"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",", -1);
            Object[] row = {i + 1, parts[0], parts[1], parts[2], parts[3], parts[4]};
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
            String inputIndex = JOptionPane.showInputDialog(this, "Enter the employee number to edit:");
            int empIndex;
            try { empIndex = Integer.parseInt(inputIndex); }
            catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid number!"); return; }

            if (empIndex < 1 || empIndex > lines.length) { JOptionPane.showMessageDialog(this, "Employee number out of range!"); return; }

            String[] parts = lines[empIndex - 1].split(",", -1);
            String newEmpId = JOptionPane.showInputDialog(this, "Edit Employee ID:", parts[0]);
            if (newEmpId == null) return;
            String newName = JOptionPane.showInputDialog(this, "Edit Full Name:", parts[1]);
            if (newName == null) return;
            String newContact = JOptionPane.showInputDialog(this, "Edit Contact Number:", parts[2]);
            if (newContact == null) return;
            String newAddress = JOptionPane.showInputDialog(this, "Edit Address:", parts[3]);
            if (newAddress == null) return;
            String newPosition = JOptionPane.showInputDialog(this, "Edit Position:", parts[4]);
            if (newPosition == null) return;

            lines[empIndex - 1] = newEmpId + "," + newName + "," + newContact + "," + newAddress + "," + newPosition;

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : lines) pw.println(l);
            } catch (IOException e) { JOptionPane.showMessageDialog(this, "Error saving employee."); return; }

            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
        }
    }

    private void removeEmployee() {
        File file = new File("employees.txt");
        if (!file.exists()) { JOptionPane.showMessageDialog(this, "No employees registered."); return; }

        String[] lines;
        try { lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new); }
        catch (IOException e) { JOptionPane.showMessageDialog(this, "Error reading employees file."); return; }

        StringBuilder sb = new StringBuilder("Registered Employees:\n");
        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            sb.append((i + 1)).append(". ").append(parts[1]).append(" (").append(parts[0]).append(")\n");
        }

        String adminPass = JOptionPane.showInputDialog(this, "Enter Admin Password:");
        if (!"admin123".equals(adminPass)) { JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password."); return; }

        String inputIndex = JOptionPane.showInputDialog(this, sb.toString() + "\nEnter the employee number to remove:");
        int empIndex;
        try { empIndex = Integer.parseInt(inputIndex); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid number!"); return; }

        if (empIndex < 1 || empIndex > lines.length) { JOptionPane.showMessageDialog(this, "Employee number out of range!"); return; }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove employee:\n" + lines[empIndex - 1],
                "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String[] newLines = new String[lines.length - 1];
        for (int i = 0, j = 0; i < lines.length; i++)
            if (i != empIndex - 1) newLines[j++] = lines[i];

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (String l : newLines) pw.println(l);
        } catch (IOException e) { JOptionPane.showMessageDialog(this, "Error removing employee."); return; }

        JOptionPane.showMessageDialog(this, "Employee removed successfully!");
    }
}
