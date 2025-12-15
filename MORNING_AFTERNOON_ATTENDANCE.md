# Morning and Afternoon Attendance Feature

## Overview
The Barangay Attendance System now supports separate morning and afternoon attendance tracking. Employees can clock in and out twice per day:
- **Morning**: Clock IN (8:00 AM) → Clock OUT (12:00 PM)
- **Break Time**: 12:00 PM - 1:00 PM
- **Afternoon**: Clock IN (1:00 PM) → Clock OUT (5:00 PM)

## Database Changes

### New Columns Added
The `attendance_logs` table now includes:
- `morning_clock_in` - Morning clock in time
- `morning_clock_out` - Morning clock out time
- `afternoon_clock_in` - Afternoon clock in time
- `afternoon_clock_out` - Afternoon clock out time

### Migration
For existing databases, run the migration script:
```sql
mysql -u root attendance_db < database_migration_morning_afternoon.sql
```

This will:
1. Add the new columns to the `attendance_logs` table
2. Migrate existing `clock_in` and `clock_out` data to `morning_clock_in` and `morning_clock_out`

## How It Works

### Employee Workflow
1. **Morning IN** (8:00 AM): Employee scans RFID card → System records morning clock in
2. **Morning OUT** (12:00 PM): Employee scans RFID card → System records morning clock out
3. **Break Time** (12:00 PM - 1:00 PM): System prevents afternoon clock in during break
4. **Afternoon IN** (1:00 PM): Employee scans RFID card → System records afternoon clock in
5. **Afternoon OUT** (5:00 PM): Employee scans RFID card → System records afternoon clock out

### Status Messages
- **MORNING_IN**: Ready to clock in for morning
- **MORNING_OUT**: Ready to clock out for morning
- **WAIT_FOR_BREAK**: Morning completed, waiting for break time (12:00 PM - 1:00 PM)
- **AFTERNOON_IN**: Ready to clock in for afternoon (after 12:00 PM)
- **AFTERNOON_OUT**: Ready to clock out for afternoon
- **ALL_DONE**: All attendance completed for the day

## Admin Dashboard Updates

### Attendance Logs Table
The admin dashboard now displays:
- Morning IN
- Morning OUT
- Afternoon IN
- Afternoon OUT
- Total Hours Worked (morning + afternoon)

### Reports
All reports and exports now include separate columns for morning and afternoon attendance.

## Technical Details

### Modified Files
1. `database_setup.sql` - Updated schema with new columns
2. `database_migration_morning_afternoon.sql` - Migration script for existing databases
3. `DatabaseOperations.java` - Updated `getTodayAction()` and `saveAttendanceLog()` methods
4. `DashboardFrame.java` - Updated messages and attendance processing
5. `AttendanceLog.java` - Added morning/afternoon fields and getters
6. `AdminDashboardFrame.java` - Updated table display and reports

### Backward Compatibility
- Legacy `clock_in` and `clock_out` columns are kept for backward compatibility
- Existing data is automatically migrated to morning fields
- The system gracefully handles both old and new data formats

## Usage Instructions

### For Employees
1. Scan your RFID card in the morning (around 8:00 AM) to clock in
2. Scan your RFID card at 12:00 PM to clock out for morning
3. Take your break (12:00 PM - 1:00 PM)
4. Scan your RFID card at 1:00 PM to clock in for afternoon
5. Scan your RFID card at 5:00 PM to clock out for afternoon

### For Admins
- View all attendance logs with morning and afternoon times separately
- Export reports with detailed morning/afternoon breakdown
- Statistics now count morning and afternoon attendance separately

## Notes
- Break time is enforced: Employees cannot clock in for afternoon before 12:00 PM
- Hours worked calculation includes both morning and afternoon hours
- All existing features (RFID scanning, manual entry, exports) work with the new system

