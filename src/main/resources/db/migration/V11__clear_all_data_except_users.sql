-- Clear all database data except User Management tables
-- Preserves: roles, users, user_sessions, dashboard_audit_log, audit_log (user-related data)
-- Clears: All business data (customers, orders, inventory, labor, machines, etc.)
-- Note: TRUNCATE ... CASCADE automatically handles foreign key constraints

-- =============================================
-- CLEAR ORDER-RELATED DATA
-- =============================================

TRUNCATE TABLE order_alerts CASCADE;
TRUNCATE TABLE order_transactions CASCADE;
TRUNCATE TABLE order_machines CASCADE;
TRUNCATE TABLE order_labor CASCADE;
TRUNCATE TABLE order_materials CASCADE;
TRUNCATE TABLE order_items CASCADE;
TRUNCATE TABLE orders CASCADE;

-- =============================================
-- CLEAR PRODUCTION DATA
-- =============================================

TRUNCATE TABLE labor_master CASCADE;
TRUNCATE TABLE machines CASCADE;

-- =============================================
-- CLEAR INVENTORY DATA
-- =============================================

TRUNCATE TABLE inventory_transactions CASCADE;
TRUNCATE TABLE inventory_items CASCADE;
TRUNCATE TABLE inventory_categories CASCADE;

-- =============================================
-- CLEAR PRODUCT & CUSTOMER DATA
-- =============================================

TRUNCATE TABLE printing_specifications CASCADE;
TRUNCATE TABLE products CASCADE;
TRUNCATE TABLE product_categories CASCADE;
TRUNCATE TABLE customers CASCADE;

-- =============================================
-- CLEAR SYSTEM SETTINGS (Non-user-related)
-- =============================================

TRUNCATE TABLE system_settings CASCADE;
TRUNCATE TABLE system_status CASCADE;

-- =============================================
-- PRESERVED USER-RELATED DATA:
-- =============================================
-- roles (NOT CLEARED)
-- users (NOT CLEARED)
-- user_sessions (NOT CLEARED)
-- audit_log (NOT CLEARED)
-- dashboard_audit_log (NOT CLEARED)
--
-- All other tables have been cleared and are ready for new data.
