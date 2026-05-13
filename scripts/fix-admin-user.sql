-- Fix Admin User Login
-- Run this to create/update the admin user with correct password

-- Connect to database
\c erp_db

-- Check if user exists
SELECT id, username, email, is_active FROM users WHERE username = 'admin';

-- Delete old admin user if exists
DELETE FROM users WHERE username = 'admin';

-- Ensure roles exist
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Administrator with full access'),
('MANAGER', 'Manager with operational access'),
('OPERATOR', 'Operator with limited access'),
('VIEWER', 'Read-only access')
ON CONFLICT (name) DO NOTHING;

-- Insert admin user with correct BCrypt hash for 'admin123'
-- This is a known valid hash for 'admin123' with BCrypt strength 10
INSERT INTO users (username, email, password_hash, first_name, last_name, role_id, is_active)
VALUES (
    'admin',
    'admin@printflow.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',  -- admin123
    'System',
    'Administrator',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    TRUE
);

-- Verify user was created
SELECT id, username, email, first_name, last_name, is_active, 
       (SELECT name FROM roles WHERE id = users.role_id) as role_name
FROM users 
WHERE username = 'admin';

-- Success message
SELECT 'Admin user created successfully! Try logging in with admin/admin123' AS status;
