package attendance;

import java.sql.Time;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;

public class AttendanceLog {
    private int id;
    private String employeeId;
    private String employeeName;
    private Time clockIn;
    private Time clockOut;
    private Date logDate;
    
    public AttendanceLog(String employeeId, String employeeName, Time clockIn, Time clockOut, Date logDate) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.logDate = logDate;
    }
    
    public AttendanceLog(int id, String employeeId, String employeeName, Time clockIn, Time clockOut, Date logDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.logDate = logDate;
    }
    
    // Calculate hours worked
    public String getHoursWorked() {
        if (clockIn != null && clockOut != null) {
            try {
                LocalTime in = clockIn.toLocalTime();
                LocalTime out = clockOut.toLocalTime();
                Duration duration = Duration.between(in, out);
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                return hours + "h " + minutes + "m";
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public Time getClockIn() { return clockIn; }
    public void setClockIn(Time clockIn) { this.clockIn = clockIn; }
    
    public Time getClockOut() { return clockOut; }
    public void setClockOut(Time clockOut) { this.clockOut = clockOut; }
    
    public Date getLogDate() { return logDate; }
    public void setLogDate(Date logDate) { this.logDate = logDate; }
    
    @Override
    public String toString() {
        return employeeName + "," + employeeId + "," + 
               (clockIn != null ? clockIn.toString() : "") + "," + 
               (clockOut != null ? clockOut.toString() : "") + "," + 
               logDate.toString();
    }
}
