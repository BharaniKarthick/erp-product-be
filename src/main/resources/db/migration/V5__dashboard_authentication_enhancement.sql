-- V5: Dashboard and Authentication Enhancements
-- Created for: Dashboard metrics optimization and user session tracking

-- ============================================================
-- 1. Create System Status Table
-- ============================================================
-- Track production lines, machine status, and system health
CREATE TABLE IF NOT EXISTS system_status (
    id BIGSERIAL PRIMARY KEY,
    component_name VARCHAR(100) NOT NULL,
    component_type VARCHAR(50) NOT NULL, -- 'PRODUCTION_LINE', 'MACHINE', 'SYSTEM', 'NETWORK'
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- 'ACTIVE', 'INACTIVE', 'MAINTENANCE', 'ERROR'
    status_message TEXT,
    last_checked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uptime_hours DECIMAL(10, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_system_status_type CHECK (component_type IN ('PRODUCTION_LINE', 'MACHINE', 'SYSTEM', 'NETWORK')),
    CONSTRAINT chk_system_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE', 'ERROR'))
);

-- Index for quick status lookups
CREATE INDEX idx_system_status_component_type ON system_status(component_type);
CREATE INDEX idx_system_status_status ON system_status(status);
CREATE INDEX idx_system_status_last_checked ON system_status(last_checked_at DESC);

-- Insert default production lines
INSERT INTO system_status (component_name, component_type, status, status_message, uptime_hours) VALUES
('Production Line A', 'PRODUCTION_LINE', 'ACTIVE', 'Operating normally', 1248.5),
('Production Line B', 'PRODUCTION_LINE', 'ACTIVE', 'Operating normally', 987.2),
('Production Line C', 'PRODUCTION_LINE', 'ACTIVE', 'Operating normally', 1560.8),
('Printing Machine 1', 'MACHINE', 'ACTIVE', 'High utilization', 2340.0),
('Printing Machine 2', 'MACHINE', 'ACTIVE', 'Standard operation', 1890.5),
('Network Infrastructure', 'NETWORK', 'ACTIVE', 'All systems operational', 8760.0),
('ERP System', 'SYSTEM', 'ACTIVE', 'Database healthy', 4320.0);

-- ============================================================
-- 2. Create User Sessions Table
-- ============================================================
-- Track user login sessions, station IDs, and session duration
CREATE TABLE IF NOT EXISTS user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    station_id VARCHAR(50), -- Workstation identifier (e.g., 'WS-001', 'ADMIN-PC-02')
    ip_address VARCHAR(45), -- IPv4 or IPv6
    user_agent TEXT, -- Browser/client information
    login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_time TIMESTAMP,
    last_activity TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    session_duration_minutes INTEGER, -- Calculated on logout
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for session management
CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_token ON user_sessions(session_token);
CREATE INDEX idx_user_sessions_active ON user_sessions(is_active, last_activity DESC);
CREATE INDEX idx_user_sessions_login_time ON user_sessions(login_time DESC);

-- ============================================================
-- 3. Create Materialized View for Dashboard KPIs
-- ============================================================
-- Pre-computed aggregations for faster dashboard loading
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_dashboard_kpi AS
SELECT 
    COUNT(DISTINCT o.id) AS total_orders,
    COUNT(DISTINCT CASE WHEN o.status IN ('IN_PRODUCTION', 'PENDING') THEN o.id END) AS active_orders,
    COUNT(DISTINCT CASE WHEN o.status = 'COMPLETED' THEN o.id END) AS completed_orders,
    COALESCE(SUM(o.total_amount), 0) AS total_revenue,
    COALESCE(SUM(
        COALESCE((SELECT SUM(om.unit_cost * om.quantity) FROM order_materials om WHERE om.order_id = o.id), 0) +
        COALESCE((SELECT SUM(ol.hourly_rate * ol.duration_hours) FROM order_labor ol WHERE ol.order_id = o.id), 0) +
        COALESCE((SELECT SUM(ohm.hourly_cost * ohm.uptime_hours) FROM order_machines ohm WHERE ohm.order_id = o.id), 0)
    ), 0) AS total_cost,
    COALESCE(SUM(o.total_amount), 0) - COALESCE(SUM(
        COALESCE((SELECT SUM(om.unit_cost * om.quantity) FROM order_materials om WHERE om.order_id = o.id), 0) +
        COALESCE((SELECT SUM(ol.hourly_rate * ol.duration_hours) FROM order_labor ol WHERE ol.order_id = o.id), 0) +
        COALESCE((SELECT SUM(ohm.hourly_cost * ohm.uptime_hours) FROM order_machines ohm WHERE ohm.order_id = o.id), 0)
    ), 0) AS net_profit,
    CURRENT_TIMESTAMP AS last_updated
FROM orders o;

-- Create unique index for materialized view refresh
CREATE UNIQUE INDEX idx_mv_dashboard_kpi_refresh ON mv_dashboard_kpi(last_updated);

-- ============================================================
-- 4. Create Materialized View for Low Inventory Alerts
-- ============================================================
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_low_inventory_alerts AS
SELECT 
    ii.id,
    ii.item_code,
    ii.name,
    ii.current_quantity,
    ii.reorder_point,
    ii.unit_of_measure,
    CASE 
        WHEN ii.current_quantity = 0 THEN 'Critical'
        WHEN ii.current_quantity <= ii.reorder_point THEN 'Low'
        ELSE 'Normal'
    END AS status,
    CURRENT_TIMESTAMP AS last_updated
FROM inventory_items ii
WHERE ii.current_quantity <= ii.reorder_point
ORDER BY ii.current_quantity ASC
LIMIT 10;

-- Create unique index for materialized view
CREATE UNIQUE INDEX idx_mv_low_inventory_id ON mv_low_inventory_alerts(id);

-- ============================================================
-- 5. Create Function to Refresh Dashboard Materialized Views
-- ============================================================
CREATE OR REPLACE FUNCTION refresh_dashboard_views() 
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_dashboard_kpi;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_low_inventory_alerts;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
-- 6. Add Dashboard-Specific Indexes for Performance
-- ============================================================

-- Index for recent orders query (used in dashboard)
-- Note: Removed WHERE clause as CURRENT_DATE is not immutable and cannot be used in partial indexes
CREATE INDEX IF NOT EXISTS idx_orders_recent_date ON orders(order_date DESC);

-- Index for delayed orders query
CREATE INDEX IF NOT EXISTS idx_orders_delayed ON orders(required_date, status);

-- Index for negative profit orders (composite for cost calculation)
CREATE INDEX IF NOT EXISTS idx_order_materials_cost ON order_materials(order_id, unit_cost, quantity);
CREATE INDEX IF NOT EXISTS idx_order_labor_cost ON order_labor(order_id, hourly_rate, duration_hours);
CREATE INDEX IF NOT EXISTS idx_order_machines_cost ON order_machines(order_id, hourly_cost, uptime_hours);

-- ============================================================
-- 7. Create View for Active User Sessions
-- ============================================================
CREATE OR REPLACE VIEW v_active_user_sessions AS
SELECT 
    us.id AS session_id,
    us.user_id,
    u.username,
    u.first_name,
    u.last_name,
    r.name AS role_name,
    us.station_id,
    us.ip_address,
    us.login_time,
    us.last_activity,
    EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - us.login_time))/60 AS session_minutes
FROM user_sessions us
JOIN users u ON us.user_id = u.id
LEFT JOIN roles r ON u.role_id = r.id
WHERE us.is_active = TRUE
ORDER BY us.login_time DESC;

-- ============================================================
-- 8. Create Audit Log Table for Dashboard Access
-- ============================================================
CREATE TABLE IF NOT EXISTS dashboard_audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL, -- 'VIEW_DASHBOARD', 'EXPORT_REPORT', 'VIEW_ALERTS', etc.
    resource VARCHAR(100), -- Specific resource accessed
    ip_address VARCHAR(45),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB -- Additional context
);

-- Index for audit log queries
CREATE INDEX idx_dashboard_audit_user ON dashboard_audit_log(user_id, timestamp DESC);
CREATE INDEX idx_dashboard_audit_action ON dashboard_audit_log(action, timestamp DESC);
CREATE INDEX idx_dashboard_audit_timestamp ON dashboard_audit_log(timestamp DESC);

-- ============================================================
-- 9. Add Customer Tier Column to Customers Table
-- ============================================================
-- For dashboard recent orders customer tier classification
ALTER TABLE customers ADD COLUMN IF NOT EXISTS tier VARCHAR(50) DEFAULT 'Standard';

-- Update existing customers with tier based on orders
UPDATE customers c
SET tier = CASE
    WHEN (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.id) >= 50 THEN 'Bulk Contract'
    WHEN (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.id) >= 20 THEN 'Priority Client'
    WHEN (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.id) >= 1 THEN 'Standard'
    ELSE 'New Client'
END;

-- Index for customer tier
CREATE INDEX IF NOT EXISTS idx_customers_tier ON customers(tier);

-- ============================================================
-- 10. Insert Default Admin User (for login testing)
-- ============================================================
-- Password: 'admin123' (BCrypt hash)
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Administrator with full access'),
('MANAGER', 'Manager with operational access'),
('OPERATOR', 'Operator with limited access'),
('VIEWER', 'Read-only access')
ON CONFLICT (name) DO NOTHING;

-- Insert test admin user (password: admin123)
-- BCrypt hash for 'admin123'
INSERT INTO users (username, email, password_hash, first_name, last_name, role_id, is_active)
VALUES (
    'admin',
    'admin@printflow.com',
    '$2a$10$rZ3.8l7eJP4qKqK5yR4D0uy8xQ0nZ7l2X/Y8P0Q1mN3K4L5M6N7O8',  -- admin123
    'System',
    'Administrator',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    TRUE
)
ON CONFLICT (username) DO NOTHING;

-- ============================================================
-- 11. Comments for Documentation
-- ============================================================
COMMENT ON TABLE system_status IS 'Tracks production lines and system component status for dashboard';
COMMENT ON TABLE user_sessions IS 'User login sessions with station tracking';
COMMENT ON TABLE dashboard_audit_log IS 'Audit log for dashboard access and actions';
COMMENT ON MATERIALIZED VIEW mv_dashboard_kpi IS 'Pre-computed KPI metrics for faster dashboard loading';
COMMENT ON MATERIALIZED VIEW mv_low_inventory_alerts IS 'Cached low inventory alerts';
COMMENT ON FUNCTION refresh_dashboard_views IS 'Refresh all dashboard materialized views';
