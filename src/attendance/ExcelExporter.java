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
            
            for (AttendanceLog log : logs) {
                String date = dateFormat.format(log.getLogDate());
                String empId = log.getEmployeeId();
                String empName = log.getEmployeeName();
                String clockIn = log.getFormattedClockIn();
                String clockOut = log.getFormattedClockOut();
                String hoursWorked = log.getHoursWorked();
                
                // Properly quote fields that might contain commas
                String quotedDate = date;
                String quotedEmpId = empId;
                String quotedEmpName = empName.contains(",") ? "\"" + empName + "\"" : empName;
                String quotedClockIn = clockIn;
                String quotedClockOut = clockOut;
                String quotedHours = hoursWorked;
                
                writer.printf("%s,%s,%s,%s,%s,%s%n", 
                    quotedDate, quotedEmpId, quotedEmpName, quotedClockIn, quotedClockOut, quotedHours);
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting attendance to CSV: " + e.getMessage());
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
                // Properly quote fields that might contain commas
                String quotedEmpId = emp.getEmployeeId();
                String quotedName = emp.getFullName().contains(",") ? "\"" + emp.getFullName() + "\"" : emp.getFullName();
                String quotedContact = emp.getContactNumber().contains(",") ? "\"" + emp.getContactNumber() + "\"" : emp.getContactNumber();
                String quotedAddress = emp.getAddress().contains(",") ? "\"" + emp.getAddress() + "\"" : emp.getAddress();
                String quotedPosition = emp.getPosition().contains(",") ? "\"" + emp.getPosition() + "\"" : emp.getPosition();
                String quotedRegDate = dateFormat.format(new Date());
                
                writer.printf("%s,%s,%s,%s,%s,%s%n", 
                    quotedEmpId, quotedName, quotedContact, quotedAddress, quotedPosition, quotedRegDate);
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
                
                // Use clean spacing for better readability
                writer.printf("%-12s %-12s %-20s %-10s %-10s %-10s%n", 
                    date, empId, empName, clockIn, clockOut, hoursWorked);
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
