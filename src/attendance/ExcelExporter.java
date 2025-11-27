package attendance;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelExporter {
    
    // Export attendance logs to CSV (can be opened in Excel)
    public static boolean exportAttendanceToCSV(List<AttendanceLog> logs, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write CSV header
            writer.println("Date,Employee ID,Employee Name,Clock In,Clock Out,Hours Worked");
            
            // Write data
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            
            for (AttendanceLog log : logs) {
                String date = dateFormat.format(log.getLogDate());
                String empId = log.getEmployeeId();
                String empName = log.getEmployeeName();
                String clockIn = log.getClockIn() != null ? timeFormat.format(log.getClockIn()) : "";
                String clockOut = log.getClockOut() != null ? timeFormat.format(log.getClockOut()) : "";
                String hoursWorked = log.getHoursWorked();
                
                writer.printf("%s,%s,%s,%s,%s,%s%n", 
                    date, empId, empName, clockIn, clockOut, hoursWorked);
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }
    
    // Export employees to CSV
    public static boolean exportEmployeesToCSV(List<Employee> employees, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write CSV header
            writer.println("Employee ID,Full Name,Contact Number,Address,Position,Registration Date");
            
            // Write data
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for (Employee emp : employees) {
                writer.printf("%s,%s,%s,%s,%s,%s%n", 
                    emp.getEmployeeId(), 
                    emp.getFullName(), 
                    emp.getContactNumber(), 
                    emp.getAddress(), 
                    emp.getPosition(),
                    dateFormat.format(new Date())); // Current date as registration date
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting employees to CSV: " + e.getMessage());
            return false;
        }
    }
    
    // Generate attendance summary report
    public static boolean generateAttendanceSummary(List<AttendanceLog> logs, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat reportFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
            
            // Report header
            writer.println("=================================================");
            writer.println("    BARANGAY ATTENDANCE SYSTEM REPORT");
            writer.println("=================================================");
            writer.println("Generated on: " + reportFormat.format(new Date()));
            writer.println("Total Records: " + logs.size());
            writer.println();
            
            // Summary statistics
            int totalEmployees = 0;
            int presentToday = 0;
            double totalHours = 0;
            
            // Get today's date
            String today = dateFormat.format(new Date());
            
            for (AttendanceLog log : logs) {
                if (log.getClockIn() != null) {
                    totalEmployees++;
                    if (log.getLogDate().toString().equals(today)) {
                        presentToday++;
                    }
                    
                    // Calculate hours
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
            
            writer.println("SUMMARY STATISTICS:");
            writer.println("----------------");
            writer.printf("Total Unique Employees: %d%n", totalEmployees);
            writer.printf("Present Today: %d%n", presentToday);
            writer.printf("Total Hours Worked: %.1f%n", totalHours);
            writer.printf("Average Hours per Employee: %.1f%n", 
                totalEmployees > 0 ? totalHours / totalEmployees : 0);
            writer.println();
            
            // Detailed records
            writer.println("DETAILED ATTENDANCE RECORDS:");
            writer.println("----------------------------");
            writer.println("Date\t\tEmployee ID\tName\t\tClock In\tClock Out\tHours");
            writer.println("----\t\t-----------\t----\t\t--------\t--------\t-----");
            
            for (AttendanceLog log : logs) {
                String date = dateFormat.format(log.getLogDate());
                String empId = log.getEmployeeId();
                String empName = log.getEmployeeName();
                String clockIn = log.getClockIn() != null ? new SimpleDateFormat("HH:mm").format(log.getClockIn()) : "";
                String clockOut = log.getClockOut() != null ? new SimpleDateFormat("HH:mm").format(log.getClockOut()) : "";
                String hoursWorked = log.getHoursWorked();
                
                writer.printf("%s\t%s\t\t%s\t\t%s\t%s\t\t%s%n", 
                    date, empId, empName.length() > 15 ? 
                    empName.substring(0, 12) + "..." : empName, clockIn, clockOut, hoursWorked);
            }
            
            writer.println();
            writer.println("=================================================");
            writer.println("    END OF REPORT");
            writer.println("=================================================");
            
            return true;
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
            return false;
        }
    }
    
    // Get default file path with timestamp
    public static String getExportFilePath(String prefix, String extension) {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = timestampFormat.format(new Date());
        return prefix + "_" + timestamp + "." + extension;
    }
}
