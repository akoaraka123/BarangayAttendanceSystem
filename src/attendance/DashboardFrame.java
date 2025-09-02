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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== HEADER =====
        JLabel headerLabel = new JLabel("Barangay Attendance System", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));

        // ===== BUTTON PANEL =====
        JPanel panel = new JPanel(new GridLayout(2, 1, 40, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 400, 100, 400));

        btnScanID = new JButton("📷  Scan ID");
        btnManual = new JButton("✍️  Manual Entry");

        Dimension buttonSize = new Dimension(400, 90);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 28);

        btnScanID.setFont(buttonFont);
        btnManual.setFont(buttonFont);
        btnScanID.setPreferredSize(buttonSize);
        btnManual.setPreferredSize(buttonSize);
        btnScanID.setFocusPainted(false);
        btnManual.setFocusPainted(false);

        panel.add(btnScanID);
        panel.add(btnManual);

        setLayout(new BorderLayout());
        add(headerLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnScanID.addActionListener(e -> scanEmployeeID());
        btnManual.addActionListener(e -> manualEntry());
    }

    private void scanEmployeeID() {
        String empId = JOptionPane.showInputDialog(this, "Enter/Scan Employee ID:");
        if (empId == null || empId.trim().isEmpty()) return;

        String name = getEmployeeName(empId);
        if (name == null) {
            JOptionPane.showMessageDialog(this, "❌ Employee not registered!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Check for today's log
        String todayAction = getTodayAction(empId, name);

        if (todayAction.equals("BOTH_DONE")) {
            JOptionPane.showMessageDialog(this, "❌ Employee already has Clock IN and Clock OUT today!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (todayAction.equals("NO_CLOCKIN") || todayAction.equals("CAN_CLOCKIN")) {
            saveLog(name, empId, "Clock IN");
            JOptionPane.showMessageDialog(this, "Employee " + name + " (" + empId + ") Clock IN recorded!");
        } else if (todayAction.equals("CAN_CLOCKOUT")) {
            saveLog(name, empId, "Clock OUT");
            JOptionPane.showMessageDialog(this, "Employee " + name + " (" + empId + ") Clock OUT recorded!");
        }
    }

    private void manualEntry() {
        String empId = JOptionPane.showInputDialog(this, "Enter Employee ID:");
        if (empId == null || empId.trim().isEmpty()) return;

        String name = JOptionPane.showInputDialog(this, "Enter Employee Name:");
        if (name == null || name.trim().isEmpty()) return;

        if (!isEmployeeRegistered(empId, name)) {
            JOptionPane.showMessageDialog(this,
                    "❌ Employee not registered!\nPlease register first before logging attendance.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String todayAction = getTodayAction(empId, name);
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
            saveLog(name, empId, options[choice]);
            JOptionPane.showMessageDialog(this, name + " " + options[choice] + " recorded!");
        }
    }

    private boolean isEmployeeRegistered(String empId, String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("employees.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    if (parts[0].trim().equals(empId.trim()) && parts[1].trim().equalsIgnoreCase(name.trim())) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading employees file: " + e.getMessage());
        }
        return false;
    }

    // ✅ Returns status of employee today
    private String getTodayAction(String empId, String name) {
        String today = LocalDate.now().toString();
        boolean hasClockIn = false;
        boolean hasClockOut = false;

        try (BufferedReader br = new BufferedReader(new FileReader("attendance_logs.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(name) && parts[1].equals(empId) && parts[4].equals(today)) {
                    hasClockIn = !parts[2].isEmpty();
                    hasClockOut = !parts[3].isEmpty();
                }
            }
        } catch (IOException ignored) {}

        if (!hasClockIn) return "NO_CLOCKIN";
        if (hasClockIn && !hasClockOut) return "CAN_CLOCKOUT";
        return "BOTH_DONE";
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
                        if (action.equals("Clock IN")) timeIn = time;
                        else if (action.equals("Clock OUT")) timeOut = time;
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
            allLogs.append(action.equals("Clock IN") ? time : "").append(",");
            allLogs.append(action.equals("Clock OUT") ? time : "").append(",");
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
