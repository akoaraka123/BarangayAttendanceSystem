-- Add 20 Random Employees and Attendance Logs for Current Month
-- Run this in MySQL/XAMPP phpMyAdmin

USE attendance_db;

-- Delete existing test data first (if you want to refresh the data)
-- Uncomment the lines below if you want to delete existing test employees and their logs
-- DELETE FROM attendance_logs WHERE employee_id IN ('EMP003', 'EMP004', 'EMP005', 'EMP006', 'EMP007', 'EMP008', 'EMP009', 'EMP010', 'EMP011', 'EMP012', 'EMP013', 'EMP014', 'EMP015', 'EMP016', 'EMP017', 'EMP018', 'EMP019', 'EMP020', 'EMP021', 'EMP022');
-- DELETE FROM employees WHERE employee_id IN ('EMP003', 'EMP004', 'EMP005', 'EMP006', 'EMP007', 'EMP008', 'EMP009', 'EMP010', 'EMP011', 'EMP012', 'EMP013', 'EMP014', 'EMP015', 'EMP016', 'EMP017', 'EMP018', 'EMP019', 'EMP020', 'EMP021', 'EMP022');

-- Insert 20 random employees (using INSERT IGNORE to skip if already exists)
INSERT IGNORE INTO employees (employee_id, full_name, contact_number, address, position, rfid_card_id) VALUES
('EMP003', 'Maria Santos', '+639123456789', '123 Purok 1, Barangay Poblacion', 'Barangay Secretary', 'RFID003ABC'),
('EMP004', 'Juan Dela Cruz', '+639234567890', '456 Purok 2, Barangay San Isidro', 'Barangay Treasurer', 'RFID004DEF'),
('EMP005', 'Ana Garcia', '+639345678901', '789 Purok 3, Barangay San Jose', 'Barangay Councilor', 'RFID005GHI'),
('EMP006', 'Carlos Rodriguez', '+639456789012', '321 Purok 4, Barangay San Pedro', 'Barangay Kagawad', 'RFID006JKL'),
('EMP007', 'Rosa Martinez', '+639567890123', '654 Purok 5, Barangay San Juan', 'Barangay Secretary', 'RFID007MNO'),
('EMP008', 'Pedro Fernandez', '+639678901234', '987 Purok 6, Barangay San Miguel', 'Barangay Treasurer', 'RFID008PQR'),
('EMP009', 'Carmen Lopez', '+639789012345', '147 Purok 7, Barangay San Antonio', 'Barangay Councilor', 'RFID009STU'),
('EMP010', 'Miguel Torres', '+639890123456', '258 Purok 8, Barangay San Francisco', 'Barangay Kagawad', 'RFID010VWX'),
('EMP011', 'Elena Reyes', '+639901234567', '369 Purok 9, Barangay San Pablo', 'Barangay Secretary', 'RFID011YZA'),
('EMP012', 'Roberto Cruz', '+639012345678', '741 Purok 10, Barangay San Mateo', 'Barangay Treasurer', 'RFID012BCD'),
('EMP013', 'Luz Villanueva', '+639123450987', '852 Purok 11, Barangay San Nicolas', 'Barangay Councilor', 'RFID013EFG'),
('EMP014', 'Antonio Mendoza', '+639234561098', '963 Purok 12, Barangay San Rafael', 'Barangay Kagawad', 'RFID014HIJ'),
('EMP015', 'Sofia Ramos', '+639345672109', '159 Purok 13, Barangay San Lorenzo', 'Barangay Secretary', 'RFID015KLM'),
('EMP016', 'Fernando Aquino', '+639456783210', '357 Purok 14, Barangay San Vicente', 'Barangay Treasurer', 'RFID016NOP'),
('EMP017', 'Isabel Castillo', '+639567894321', '468 Purok 15, Barangay San Agustin', 'Barangay Councilor', 'RFID017QRS'),
('EMP018', 'Ricardo Morales', '+639678905432', '579 Purok 16, Barangay San Gabriel', 'Barangay Kagawad', 'RFID018TUV'),
('EMP019', 'Patricia Gutierrez', '+639789016543', '680 Purok 17, Barangay San Isidro', 'Barangay Secretary', 'RFID019WXY'),
('EMP020', 'Jose Navarro', '+639890127654', '791 Purok 18, Barangay San Roque', 'Barangay Treasurer', 'RFID020ZAB'),
('EMP021', 'Gloria Herrera', '+639901238765', '802 Purok 19, Barangay San Sebastian', 'Barangay Councilor', 'RFID021CDE'),
('EMP022', 'Manuel Jimenez', '+639012349876', '913 Purok 20, Barangay San Fernando', 'Barangay Kagawad', 'RFID022FGH');

-- Get current month and year
SET @current_year = YEAR(CURDATE());
SET @current_month = MONTH(CURDATE());
SET @current_date = CURDATE();

-- Insert attendance logs for current month (November 2025 example, but will use current month)
-- Clock in: 8:00 AM, Clock out: 5:00 PM for each day
-- Generate logs for all working days (Monday to Friday) for the current month

-- Function to generate attendance logs for current month
-- This will insert logs for each employee for working days (Mon-Fri) in current month

-- Delete existing logs for current month first (optional - comment out if you want to keep existing)
-- DELETE FROM attendance_logs WHERE YEAR(log_date) = @current_year AND MONTH(log_date) = @current_month;

-- Insert attendance logs for each employee
-- We'll generate logs for working days (Monday-Friday) in the current month
-- Using DATE(CONCAT()) to construct dates for current month

-- Delete existing attendance logs for test employees first (to avoid duplicates)
-- Note: EMP003 and EMP004 are excluded since they're already executed separately
DELETE FROM attendance_logs WHERE employee_id IN ('EMP005', 'EMP006', 'EMP007', 'EMP008', 'EMP009', 'EMP010', 'EMP011', 'EMP012', 'EMP013', 'EMP014', 'EMP015', 'EMP016', 'EMP017', 'EMP018', 'EMP019', 'EMP020', 'EMP021', 'EMP022');

-- Insert attendance logs for each employee (10 days per employee, days 1-10)
INSERT INTO attendance_logs (employee_id, employee_name, clock_in, clock_out, log_date) VALUES
('EMP005', 'Ana Garcia', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:05:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:15:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP005', 'Ana Garcia', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:15:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP007', 'Rosa Martinez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:05:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP008', 'Pedro Fernandez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP009', 'Carmen Lopez', '08:00:00', '17:20:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP010', 'Miguel Torres', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:05:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP011', 'Elena Reyes', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP012', 'Roberto Cruz', '08:00:00', '17:20:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP013', 'Luz Villanueva', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:05:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP014', 'Antonio Mendoza', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP015', 'Sofia Ramos', '08:00:00', '17:20:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP016', 'Fernando Aquino', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:05:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP017', 'Isabel Castillo', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP018', 'Ricardo Morales', '08:00:00', '17:20:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP019', 'Patricia Gutierrez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:05:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP020', 'Jose Navarro', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP021', 'Gloria Herrera', '08:00:00', '17:20:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:10:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:15:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:00:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:20:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:00:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:10:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP022', 'Manuel Jimenez', '08:05:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))));

-- Note: The DATE(CONCAT()) will use the current month
-- For example, if today is November 2025, it will create dates like 2025-11-01, 2025-11-02, etc.
-- If you want to specify a different month, replace CURDATE() with a specific date like '2025-11-01'








