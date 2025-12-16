-- Migration script to add soft delete support for users table
-- Run this in MySQL/XAMPP phpMyAdmin

USE attendance_db;

-- Add deleted_at column for soft delete
ALTER TABLE users 
ADD COLUMN deleted_at TIMESTAMP NULL DEFAULT NULL;

-- Add index for better query performance
CREATE INDEX idx_users_deleted_at ON users(deleted_at);

-- Update existing queries to exclude deleted users
-- Note: Authentication should still work for deleted users if needed
-- But user management should filter them out

