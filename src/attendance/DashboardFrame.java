package attendance;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DashboardFrame extends JFrame {
    JButton btnScanID, btnManual;

    public DashboardFrame() {
        setTitle("Barangay Attendance - Employee Dashboard");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        btnScanID = new JButton("Scan ID");
        btnManual = new JButton("Manual Entry");

        panel.add(btnScanID);
        panel.add(btnManual);
        add(panel);

        btnScanID.addActionListener(e -> scanEmployeeID());
        btnManual.addActionListener(e -> manualEntry());
    }

    private void scanEmployeeID() {
        String empId = JOptionPane.showInputDialog(this, "Enter/Scan Employee ID:");
        if (empId == null || empId.trim().isEmpty()) return;

        String name = getEmployeeName(empId);
        if (name == null) name = "Unknown";

        String action = determineAction(empId, name);
        saveLog(name, empId, action);

        JOptionPane.showMessageDialog(this, "Employee " + name + " (" + empId + ") " + action + " recorded!");
    }

    private void manualEntry() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID:");
        if (empId == null || empId.trim().isEmpty()) return;

        String name = JOptionPane.showInputDialog(this, "Enter Employee Name:");
        if (name == null || name.trim().isEmpty()) return;

        String[] options = {"Time IN", "Time OUT"};
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

        if (choice == 0) saveLog(name, empId, "TIME IN");
        else if (choice == 1) saveLog(name, empId, "TIME OUT");

        JOptionPane.showMessageDialog(this, name + " " + options[choice] + " recorded!");
    }

    private String determineAction(String empId, String name) {
        String today = LocalDate.now().toString();
        String lastAction = "";

        try (BufferedReader br = new BufferedReader(new FileReader("attendance_logs.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(name) && parts[1].equals(empId) && parts[4].equals(today)) {
                    lastAction = parts[2].isEmpty() ? "TIME OUT" : "TIME IN";
                }
            }
        } catch (IOException ignored) {}

        return lastAction.equals("TIME IN") ? "TIME OUT" : "TIME IN";
    }

    private void saveLog(String name, String empId, String action) {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String timeIn = "";
        String timeOut = "";

        File file = new File("attendance_logs.txt");
        StringBuilder allLogs = new StringBuilder();
        boolean foundToday = false;

        try {
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6 && parts[0].equals(name) && parts[1].equals(empId) && parts[4].equals(date)) {
                        foundToday = true;
                        timeIn = parts[2];
                        timeOut = parts[3];
                        if (action.equals("TIME IN")) timeIn = time;
                        else if (action.equals("TIME OUT")) timeOut = time;
                        line = name + "," + empId + "," + timeIn + "," + timeOut + "," + date + "," + time;
                    }
                    allLogs.append(line).append("\n");
                }
                br.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading logs: " + e.getMessage());
            return;
        }

        if (!foundToday) {
            allLogs.append(name).append(",").append(empId).append(",");
            allLogs.append(action.equals("TIME IN") ? time : "").append(",");
            allLogs.append(action.equals("TIME OUT") ? time : "").append(",");
            allLogs.append(date).append(",").append(time).append("\n");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.print(allLogs.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving log: " + e.getMessage());
        }
    }

    private String getEmployeeName(String empId) {
        try (BufferedReader br = new BufferedReader(new FileReader("employees.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(empId)) return parts[1];
            }
        } catch (IOException ignored) {}
        return null;
    }
}
