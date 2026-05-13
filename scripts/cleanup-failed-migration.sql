-- Clean up failed Flyway migration V5
-- Run this script before restarting the application

-- Connect to the database
\c erp_db

-- Check current Flyway status
SELECT version, description, type, script, installed_on, success 
FROM flyway_schema_history 
ORDER BY installed_rank DESC 
LIMIT 5;

-- Delete the failed V5 migration entry
DELETE FROM flyway_schema_history 
WHERE version = '5' AND success = false;

-- Verify deletion
SELECT version, description, type, script, installed_on, success 
FROM flyway_schema_history 
ORDER BY installed_rank DESC 
LIMIT 5;

-- Success message
SELECT 'Failed migration V5 removed. You can now restart the application.' AS status;
