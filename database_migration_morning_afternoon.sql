-- Migration script to add morning and afternoon attendance support
-- Run this script to update existing database

USE attendance_db;

-- Add new columns for morning and afternoon attendance
ALTER TABLE attendance_logs 
ADD COLUMN morning_clock_in TIME NULL AFTER clock_in,
ADD COLUMN morning_clock_out TIME NULL AFTER morning_clock_in,
ADD COLUMN afternoon_clock_in TIME NULL AFTER morning_clock_out,
ADD COLUMN afternoon_clock_out TIME NULL AFTER afternoon_clock_in;

-- Migrate existing data: move clock_in to morning_clock_in and clock_out to morning_clock_out
UPDATE attendance_logs 
SET morning_clock_in = clock_in,
    morning_clock_out = clock_out
WHERE clock_in IS NOT NULL OR clock_out IS NOT NULL;

-- Note: The old clock_in and clock_out columns will be kept for backward compatibility
-- but new attendance records will use morning_clock_in, morning_clock_out, afternoon_clock_in, afternoon_clock_out

