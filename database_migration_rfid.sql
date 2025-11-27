-- Migration script para mag-add ng RFID support sa existing database
-- Run this script kung may existing database na

USE attendance_db;

-- Add RFID card ID column sa employees table
ALTER TABLE employees 
ADD COLUMN rfid_card_id VARCHAR(50) UNIQUE AFTER position;

-- Update existing employees (optional - set to NULL kung walang RFID card)
-- UPDATE employees SET rfid_card_id = NULL WHERE rfid_card_id IS NULL;

-- Verify the changes
SELECT * FROM employees LIMIT 5;

