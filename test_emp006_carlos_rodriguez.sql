-- Test SQL for Carlos Rodriguez (EMP006) only
-- Run this in MySQL/XAMPP phpMyAdmin

USE attendance_db;

-- Delete existing attendance logs for EMP006 first
DELETE FROM attendance_logs WHERE employee_id = 'EMP006';

-- Insert attendance logs for Carlos Rodriguez (10 days, days 1-10)
INSERT INTO attendance_logs (employee_id, employee_name, clock_in, clock_out, log_date) VALUES
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(1, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:15:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(2, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(3, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:10:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(4, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:05:00', '17:15:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(5, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(6, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:20:00', '17:05:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(7, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(8, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:15:00', '17:10:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(9, 2, '0'))))),
('EMP006', 'Carlos Rodriguez', '08:00:00', '17:00:00', DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(MONTH(CURDATE()), 2, '0'), '-', LPAD(10, 2, '0'))))));

