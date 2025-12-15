package attendance;

import java.sql.Time;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AttendanceLog {
    private int id;
    private String employeeId;
    private String employeeName;
    private Time clockIn;  // Legacy field (kept for backward compatibility)
    private Time clockOut;  // Legacy field (kept for backward compatibility)
    private Time morningClockIn;
    private Time morningClockOut;
    private Time afternoonClockIn;
    private Time afternoonClockOut;
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
    
    // New constructor with morning/afternoon fields
    public AttendanceLog(int id, String employeeId, String employeeName, 
                        Time morningClockIn, Time morningClockOut,
                        Time afternoonClockIn, Time afternoonClockOut,
                        Date logDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.morningClockIn = morningClockIn;
        this.morningClockOut = morningClockOut;
        this.afternoonClockIn = afternoonClockIn;
        this.afternoonClockOut = afternoonClockOut;
        this.logDate = logDate;
        // Set legacy fields for backward compatibility
        this.clockIn = morningClockIn;
        this.clockOut = afternoonClockOut != null ? afternoonClockOut : morningClockOut;
    }
    
    // Calculate hours worked (morning + afternoon)
    public String getHoursWorked() {
        long totalMinutes = 0;
        
        // Calculate morning hours
        if (morningClockIn != null && morningClockOut != null) {
            try {
                LocalTime in = morningClockIn.toLocalTime();
                LocalTime out = morningClockOut.toLocalTime();
                Duration duration = Duration.between(in, out);
                totalMinutes += duration.toMinutes();
            } catch (Exception e) {
                // Skip if error
            }
        }
        
        // Calculate afternoon hours
        if (afternoonClockIn != null && afternoonClockOut != null) {
            try {
                LocalTime in = afternoonClockIn.toLocalTime();
                LocalTime out = afternoonClockOut.toLocalTime();
                Duration duration = Duration.between(in, out);
                totalMinutes += duration.toMinutes();
            } catch (Exception e) {
                // Skip if error
            }
        }
        
        // Fallback to legacy fields if new fields are not set
        if (totalMinutes == 0 && clockIn != null && clockOut != null) {
            try {
                LocalTime in = clockIn.toLocalTime();
                LocalTime out = clockOut.toLocalTime();
                Duration duration = Duration.between(in, out);
                totalMinutes = duration.toMinutes();
            } catch (Exception e) {
                return "";
            }
        }
        
        if (totalMinutes > 0) {
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;
            return hours + "h " + minutes + "m";
        }
        
        return "";
    }
    
    // Get formatted clock in time (legacy - returns morning clock in)
    public String getFormattedClockIn() {
        if (morningClockIn != null) {
            try {
                LocalTime time = morningClockIn.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        // Fallback to legacy field
        if (clockIn != null) {
            try {
                LocalTime time = clockIn.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Get formatted clock out time (legacy - returns afternoon clock out if available, else morning)
    public String getFormattedClockOut() {
        if (afternoonClockOut != null) {
            try {
                LocalTime time = afternoonClockOut.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        // Fallback to morning clock out or legacy field
        if (morningClockOut != null) {
            try {
                LocalTime time = morningClockOut.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        if (clockOut != null) {
            try {
                LocalTime time = clockOut.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Get formatted morning clock in time
    public String getFormattedMorningClockIn() {
        if (morningClockIn != null) {
            try {
                LocalTime time = morningClockIn.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Get formatted morning clock out time
    public String getFormattedMorningClockOut() {
        if (morningClockOut != null) {
            try {
                LocalTime time = morningClockOut.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Get formatted afternoon clock in time
    public String getFormattedAfternoonClockIn() {
        if (afternoonClockIn != null) {
            try {
                LocalTime time = afternoonClockIn.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Get formatted afternoon clock out time
    public String getFormattedAfternoonClockOut() {
        if (afternoonClockOut != null) {
            try {
                LocalTime time = afternoonClockOut.toLocalTime();
                return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    
    // Get formatted date
    public String getFormattedDate() {
        if (logDate != null) {
            try {
                return logDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                return logDate.toString();
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
    
    public Time getMorningClockIn() { return morningClockIn; }
    public void setMorningClockIn(Time morningClockIn) { this.morningClockIn = morningClockIn; }
    
    public Time getMorningClockOut() { return morningClockOut; }
    public void setMorningClockOut(Time morningClockOut) { this.morningClockOut = morningClockOut; }
    
    public Time getAfternoonClockIn() { return afternoonClockIn; }
    public void setAfternoonClockIn(Time afternoonClockIn) { this.afternoonClockIn = afternoonClockIn; }
    
    public Time getAfternoonClockOut() { return afternoonClockOut; }
    public void setAfternoonClockOut(Time afternoonClockOut) { this.afternoonClockOut = afternoonClockOut; }
    
    public Date getLogDate() { return logDate; }
    public void setLogDate(Date logDate) { this.logDate = logDate; }
    
    @Override
    public String toString() {
        return employeeName + "," + employeeId + "," + 
               (clockIn != null ? getFormattedClockIn() : "") + "," + 
               (clockOut != null ? getFormattedClockOut() : "") + "," + 
               getFormattedDate();
    }
}
