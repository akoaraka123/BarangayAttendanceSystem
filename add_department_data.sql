-- Add Department Data
-- Run this in MySQL/XAMPP phpMyAdmin after running database_migration_department.sql

USE attendance_db;

-- Insert departments (using the same names from database_migration_department.sql)
-- IGNORE will skip if department already exists
INSERT IGNORE INTO departments (department_name, description) VALUES
('Administration', 'Administrative and clerical services'),
('Health Services', 'Health and medical services'),
('Social Services', 'Social welfare and community services'),
('Security', 'Security and safety services'),
('Maintenance', 'Facilities and maintenance services');

-- Assign employees to departments based on their positions
-- Barangay Secretary -> Administration (department_id = 1)
UPDATE employees SET department_id = 1 WHERE position LIKE '%Secretary%' OR position LIKE '%Secretary';

-- Day Care Teacher -> Social Services (department_id = 3)
UPDATE employees SET department_id = 3 WHERE position LIKE '%Day Care%' OR position LIKE '%Day Care Teacher%';

-- ALS Teacher -> Social Services (department_id = 3) 
UPDATE employees SET department_id = 3 WHERE position LIKE '%ALS Teacher%' OR position LIKE '%ALS%';

-- If you want to assign specific employees by employee_id:
-- UPDATE employees SET department_id = 1 WHERE employee_id = 'EMP103'; -- Barangay Secretary -> Administration
-- UPDATE employees SET department_id = 3 WHERE employee_id = 'EMP101'; -- Day Care Teacher -> Social Services
-- UPDATE employees SET department_id = 3 WHERE employee_id = 'EMP102'; -- ALS Teacher -> Social Services

