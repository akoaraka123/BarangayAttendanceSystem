-- Migration script to add Department feature
-- Run this in MySQL/XAMPP phpMyAdmin

USE attendance_db;

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add department_id column to employees table
ALTER TABLE employees 
ADD COLUMN department_id INT NULL,
ADD FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL;

-- Insert sample departments
INSERT INTO departments (department_name, description) VALUES
('Administration', 'Administrative and clerical services'),
('Health Services', 'Health and medical services'),
('Social Services', 'Social welfare and community services'),
('Security', 'Security and safety services'),
('Maintenance', 'Facilities and maintenance services');

