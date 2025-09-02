package attendance;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AdminDashboardFrame extends JFrame {
    private JButton btnRegister, btnViewLogs, btnViewEmployees;

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        btnRegister = new JButton("Register Employee");
        btnViewLogs = new JButton("View Attendance Logs");
        btnViewEmployees = new JButton("View & Edit Registered Employees");

        panel.add(btnRegister);
        panel.add(btnViewLogs);
        panel.add(btnViewEmployees);

        add(panel);

        // Actions
        btnRegister.addActionListener(e -> registerEmployee());
        btnViewLogs.addActionListener(e -> viewLogs());
        btnViewEmployees.addActionListener(e -> viewEmployees());
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

        String[] columnNames = {"#", "Name", "Employee ID", "Time IN", "Time OUT", "Date", "Time"};
        Object[][] data = new Object[lines.length][7];

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            data[i][0] = i + 1; // index
            for (int j = 0; j < parts.length && j < 6; j++) {
                data[i][j + 1] = parts[j]; // shift by 1 for index
            }
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);

        String[] options = {"OK", "REMOVE"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Attendance Logs",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) {
            String adminPass = JOptionPane.showInputDialog(this, "Enter Admin Password:");
            if (!"admin123".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.");
                return;
            }

            String inputIndex = JOptionPane.showInputDialog(this, "Enter the log number to remove:");
            int logIndex;
            try {
                logIndex = Integer.parseInt(inputIndex);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number!");
                return;
            }

            if (logIndex < 1 || logIndex > lines.length) {
                JOptionPane.showMessageDialog(this, "Log number out of range!");
                return;
            }

            String[] newLines = new String[lines.length - 1];
            for (int i = 0, j = 0; i < lines.length; i++) {
                if (i != logIndex - 1) newLines[j++] = lines[i];
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : newLines) pw.println(l);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error removing log.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Attendance log removed successfully!");
        }
    }

    private void viewEmployees() {
        File file = new File("employees.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No employees registered.");
            return;
        }

        String[] lines;
        try {
            lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading employees file.");
            return;
        }

        StringBuilder display = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String[] data = lines[i].split(",");
            if (data.length == 5) {
                display.append(i + 1).append(". ID: ").append(data[0])
                        .append(", Name: ").append(data[1])
                        .append(", Contact: ").append(data[2])
                        .append(", Address: ").append(data[3])
                        .append(", Position: ").append(data[4])
                        .append("\n");
            }
        }

        JTextArea textArea = new JTextArea(display.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        String[] options = {"OK", "EDIT"};
        int choice = JOptionPane.showOptionDialog(this, scrollPane, "Registered Employees",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 1) {
            String adminPass = JOptionPane.showInputDialog(this, "Enter Admin Password:");
            if (!"admin123".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Unauthorized! Incorrect password.");
                return;
            }

            String inputIndex = JOptionPane.showInputDialog(this, "Enter the employee number to edit:");
            int empIndex;
            try {
                empIndex = Integer.parseInt(inputIndex);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number!");
                return;
            }

            if (empIndex < 1 || empIndex > lines.length) {
                JOptionPane.showMessageDialog(this, "Employee number out of range!");
                return;
            }

            String[] empData = lines[empIndex - 1].split(",");
            if (empData.length != 5) {
                JOptionPane.showMessageDialog(this, "Invalid employee record!");
                return;
            }

            String newID = JOptionPane.showInputDialog(this, "Edit Employee ID:", empData[0]);
            String newName = JOptionPane.showInputDialog(this, "Edit Full Name:", empData[1]);
            String newContact = JOptionPane.showInputDialog(this, "Edit Contact:", empData[2]);
            String newAddress = JOptionPane.showInputDialog(this, "Edit Address:", empData[3]);
            String newPosition = JOptionPane.showInputDialog(this, "Edit Position:", empData[4]);

            StringBuilder changes = new StringBuilder("Changes made:\n");
            if (!empData[0].equals(newID)) changes.append("ID: ").append(empData[0]).append(" -> ").append(newID).append("\n");
            if (!empData[1].equals(newName)) changes.append("Name: ").append(empData[1]).append(" -> ").append(newName).append("\n");
            if (!empData[2].equals(newContact)) changes.append("Contact: ").append(empData[2]).append(" -> ").append(newContact).append("\n");
            if (!empData[3].equals(newAddress)) changes.append("Address: ").append(empData[3]).append(" -> ").append(newAddress).append("\n");
            if (!empData[4].equals(newPosition)) changes.append("Position: ").append(empData[4]).append(" -> ").append(newPosition).append("\n");

            JOptionPane.showMessageDialog(this, changes.toString());

            lines[empIndex - 1] = String.join(",", newID, newName, newContact, newAddress, newPosition);
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : lines) pw.println(l);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving changes.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Employee record updated successfully!");
        }
    }
}
