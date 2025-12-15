-- Sample Attendance Data for Testing DTR
-- This file adds 3 example employees with attendance records
-- Run this after database_setup.sql
-- 
-- NOTE: This version works without rfid_card_id column
-- If you have rfid_card_id column, you can add it manually after running this

USE attendance_db;

-- Insert 3 example employees (without rfid_card_id to avoid column error)
    INSERT IGNORE INTO employees (employee_id, full_name, contact_number, address, position) VALUES
    ('EMP101', 'Diego, Evelina', '09171234567', '123 Barangay Fatima, General Santos City', 'Day Care Teacher'),
    ('EMP102', 'Santos, Maria', '09234567890', '456 Purok 5, Barangay Fatima, General Santos City', 'ALS Teacher'),
    ('EMP103', 'Cruz, Juan', '09345678901', '789 Purok 3, Barangay Fatima, General Santos City', 'Barangay Secretary');

-- Sample attendance records for November 2025 (Days 16-30)
-- Employee 1: Diego, Evelina
INSERT INTO attendance_logs (employee_id, employee_name, morning_clock_in, morning_clock_out, afternoon_clock_in, afternoon_clock_out, log_date) VALUES
-- Day 16 (Sunday) - No attendance
-- Day 17 (Monday)
('EMP101', 'Diego, Evelina', '07:35:00', '12:01:00', '12:55:00', '17:00:00', '2025-11-17'),
-- Day 18 (Tuesday)
('EMP101', 'Diego, Evelina', '07:40:00', '12:05:00', '12:50:00', '17:00:00', '2025-11-18'),
-- Day 19 (Wednesday)
('EMP101', 'Diego, Evelina', '07:27:00', '12:00:00', '13:00:00', '17:01:00', '2025-11-19'),
-- Day 20 (Thursday)
('EMP101', 'Diego, Evelina', '07:33:00', '12:03:00', '12:57:00', '17:03:00', '2025-11-20'),
-- Day 21 (Friday)
('EMP101', 'Diego, Evelina', '07:25:00', '12:00:00', '12:50:00', '17:00:00', '2025-11-21'),
-- Day 22 (Saturday) - No attendance
-- Day 23 (Sunday) - No attendance
-- Day 24 (Monday)
('EMP101', 'Diego, Evelina', '07:38:00', '12:00:00', '13:00:00', '17:00:00', '2025-11-24'),
-- Day 25 (Tuesday)
('EMP101', 'Diego, Evelina', '07:45:00', '12:02:00', '12:58:00', '17:01:00', '2025-11-25'),
-- Day 26 (Wednesday)
('EMP101', 'Diego, Evelina', '07:50:00', '12:05:00', '12:57:00', '17:00:00', '2025-11-26'),
-- Day 27 (Thursday)
('EMP101', 'Diego, Evelina', '07:33:00', '12:02:00', '13:00:00', '17:00:00', '2025-11-27'),
-- Day 28 (Friday)
('EMP101', 'Diego, Evelina', '07:51:00', '12:05:00', '12:50:00', '17:00:00', '2025-11-28'),
-- Day 29 (Saturday) - No attendance
-- Day 30 (Sunday) - No attendance

-- Employee 2: Santos, Maria
('EMP102', 'Santos, Maria', '07:30:00', '12:00:00', '13:00:00', '17:00:00', '2025-11-17'),
('EMP102', 'Santos, Maria', '07:35:00', '12:01:00', '12:55:00', '17:02:00', '2025-11-18'),
('EMP102', 'Santos, Maria', '07:28:00', '12:00:00', '13:05:00', '17:00:00', '2025-11-19'),
('EMP102', 'Santos, Maria', '07:40:00', '12:03:00', '12:58:00', '17:01:00', '2025-11-20'),
('EMP102', 'Santos, Maria', '07:32:00', '12:00:00', '12:52:00', '17:00:00', '2025-11-21'),
('EMP102', 'Santos, Maria', '07:42:00', '12:01:00', '13:00:00', '17:00:00', '2025-11-24'),
('EMP102', 'Santos, Maria', '07:38:00', '12:02:00', '12:55:00', '17:03:00', '2025-11-25'),
('EMP102', 'Santos, Maria', '07:45:00', '12:04:00', '12:58:00', '17:00:00', '2025-11-26'),
('EMP102', 'Santos, Maria', '07:30:00', '12:00:00', '13:02:00', '17:01:00', '2025-11-27'),
('EMP102', 'Santos, Maria', '07:48:00', '12:03:00', '12:52:00', '17:00:00', '2025-11-28'),

-- Employee 3: Cruz, Juan
('EMP103', 'Cruz, Juan', '08:00:00', '12:00:00', '13:00:00', '17:00:00', '2025-11-17'),
('EMP103', 'Cruz, Juan', '08:05:00', '12:02:00', '12:58:00', '17:01:00', '2025-11-18'),
('EMP103', 'Cruz, Juan', '07:55:00', '12:00:00', '13:05:00', '17:02:00', '2025-11-19'),
('EMP103', 'Cruz, Juan', '08:02:00', '12:01:00', '12:57:00', '17:00:00', '2025-11-20'),
('EMP103', 'Cruz, Juan', '07:58:00', '12:00:00', '13:00:00', '17:01:00', '2025-11-21'),
('EMP103', 'Cruz, Juan', '08:03:00', '12:00:00', '13:00:00', '17:00:00', '2025-11-24'),
('EMP103', 'Cruz, Juan', '08:08:00', '12:03:00', '12:55:00', '17:02:00', '2025-11-25'),
('EMP103', 'Cruz, Juan', '08:12:00', '12:05:00', '12:58:00', '17:00:00', '2025-11-26'),
('EMP103', 'Cruz, Juan', '08:00:00', '12:01:00', '13:02:00', '17:01:00', '2025-11-27'),
('EMP103', 'Cruz, Juan', '08:10:00', '12:04:00', '12:52:00', '17:00:00', '2025-11-28');

-- Note: Days 16, 22, 23, 29, 30 are weekends (Saturday/Sunday) so no attendance records
-- This matches the DTR format where weekends are marked as SATURDAY/SUNDAY

