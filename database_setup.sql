-- Barangay Attendance System Database Schema
-- Run this in MySQL/XAMPP phpMyAdmin

-- Create database
CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

-- Drop existing tables to start fresh
DROP TABLE IF EXISTS attendance_logs;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS users;

-- Users table for login
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'employee') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employees table
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20),
    address TEXT,
    position VARCHAR(50),
    rfid_card_id VARCHAR(50) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Attendance logs table
CREATE TABLE attendance_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    clock_in TIME,  -- Legacy column (kept for backward compatibility)
    clock_out TIME,  -- Legacy column (kept for backward compatibility)
    morning_clock_in TIME,
    morning_clock_out TIME,
    afternoon_clock_in TIME,
    afternoon_clock_out TIME,
    log_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    UNIQUE KEY unique_daily_log (employee_id, log_date)
);

-- Insert admin user (plain text for now)
INSERT INTO users (email, password, role) 
VALUES ('admin@barangay.com', 'admin123', 'admin');

-- Insert employee user (plain text for now)
INSERT INTO users (email, password, role) 
VALUES ('employee@barangay.com', 'employee123', 'employee');

-- Insert sample employees for testing
INSERT INTO employees (employee_id, full_name, contact_number, address, position) VALUES
('EMP001', 'Juan Santos', '09123456789', '123 Main St, Brgy. Poblacion', 'Barangay Secretary'),
('EMP002', 'Maria Reyes', '09234567890', '456 Oak Ave, Brgy. San Isidro', 'Barangay Treasurer'),
('EMP003', 'Jose Cruz', '09345678901', '789 Pine Rd, Brgy. Sta. Cruz', 'Barangay Kagawad'),
('EMP004', 'Ana Garcia', '09456789012', '321 Elm St, Brgy. San Roque', 'Barangay Health Worker');
