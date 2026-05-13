-- Database Setup Script for ERP System
-- PostgreSQL
-- Run this script as PostgreSQL superuser (postgres)

-- 1. Create database
CREATE DATABASE erp_db;

-- 2. Create user (update password to match application.properties)
CREATE USER erp_user WITH PASSWORD 'Cotton@100';

-- 3. Grant database privileges
GRANT ALL PRIVILEGES ON DATABASE erp_db TO erp_user;

-- 4. Connect to the database
\c erp_db

-- 5. Grant schema privileges (REQUIRED FOR FLYWAY)
GRANT ALL ON SCHEMA public TO erp_user;
GRANT CREATE ON SCHEMA public TO erp_user;
GRANT USAGE ON SCHEMA public TO erp_user;

-- 6. Grant privileges on existing objects
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO erp_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO erp_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO erp_user;

-- 7. Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO erp_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO erp_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON FUNCTIONS TO erp_user;

-- 8. (Optional) Make erp_user the owner
-- Uncomment if you want erp_user to own the database
-- ALTER DATABASE erp_db OWNER TO erp_user;

-- Verify setup
SELECT 'Database setup completed successfully!' AS status;
