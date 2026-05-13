-- Database Setup and Permission Fix Script
-- Run this script as PostgreSQL superuser (postgres) to fix permission issues

-- Connect to the database (run: psql -U postgres)

-- 1. Create database if not exists
-- Run this manually first: CREATE DATABASE erp_db;

-- 2. Connect to erp_db
\c erp_db

-- 3. Create user if not exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = 'erp_user') THEN
        CREATE USER erp_user WITH PASSWORD 'Cotton@100';
    END IF;
END
$$;

-- 4. Grant all privileges on database
GRANT ALL PRIVILEGES ON DATABASE erp_db TO erp_user;

-- 5. Grant schema permissions (THIS FIXES THE ERROR)
GRANT ALL ON SCHEMA public TO erp_user;
GRANT CREATE ON SCHEMA public TO erp_user;
GRANT USAGE ON SCHEMA public TO erp_user;

-- 6. Grant permissions on all existing tables
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO erp_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO erp_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO erp_user;

-- 7. Grant default permissions for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO erp_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO erp_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON FUNCTIONS TO erp_user;

-- 8. Make erp_user owner of the database (optional but recommended)
-- ALTER DATABASE erp_db OWNER TO erp_user;

-- 9. Verify permissions
\dt
\du erp_user

-- Success message
SELECT 'Permissions granted successfully! You can now run the Spring Boot application.' AS status;
