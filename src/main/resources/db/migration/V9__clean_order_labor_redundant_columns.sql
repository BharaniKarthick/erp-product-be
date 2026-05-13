-- Remove redundant labor metadata columns from order_labor.
-- These fields are derived from labor_master via the labor_id FK.
-- order_labor now stores only order-specific operational data:
--   labor_id (FK, required), duration_hours, shift_date, total_cost, notes.

-- Step 1: Drop the materialized view that depends on order_labor.hourly_rate
-- (and its unique index - must be dropped first)
DROP INDEX IF EXISTS idx_mv_dashboard_kpi_refresh;
DROP MATERIALIZED VIEW IF EXISTS mv_dashboard_kpi;

-- Step 2: Make labor_id non-nullable (was nullable previously)
ALTER TABLE order_labor ALTER COLUMN labor_id SET NOT NULL;

-- Step 3: Drop total_cost's NOT NULL constraint temporarily so existing rows survive the column drops
ALTER TABLE order_labor ALTER COLUMN total_cost DROP NOT NULL;

-- Step 4: Drop redundant columns that duplicate labor_master
ALTER TABLE order_labor DROP COLUMN IF EXISTS operator_code;
ALTER TABLE order_labor DROP COLUMN IF EXISTS operator_name;
ALTER TABLE order_labor DROP COLUMN IF EXISTS shift_role;
ALTER TABLE order_labor DROP COLUMN IF EXISTS hourly_rate;

-- Step 5: Recompute total_cost for existing rows using the joined labor_master hourly_rate
UPDATE order_labor ol
SET total_cost = ol.duration_hours * lm.hourly_rate
FROM labor_master lm
WHERE lm.id = ol.labor_id;

-- Step 6: Recreate mv_dashboard_kpi using labor_master for hourly_rate (joined via labor_id)
CREATE MATERIALIZED VIEW mv_dashboard_kpi AS
SELECT
    COUNT(DISTINCT o.id) AS total_orders,
    COUNT(DISTINCT CASE WHEN o.status IN ('IN_PRODUCTION', 'PENDING') THEN o.id END) AS active_orders,
    COUNT(DISTINCT CASE WHEN o.status = 'COMPLETED' THEN o.id END) AS completed_orders,
    COALESCE(SUM(o.total_amount), 0) AS total_revenue,
    COALESCE(SUM(
        COALESCE((SELECT SUM(om.unit_cost * om.quantity) FROM order_materials om WHERE om.order_id = o.id), 0) +
        COALESCE((SELECT SUM(lm.hourly_rate * ol.duration_hours) FROM order_labor ol JOIN labor_master lm ON lm.id = ol.labor_id WHERE ol.order_id = o.id), 0) +
        COALESCE((SELECT SUM(ohm.hourly_cost * ohm.uptime_hours) FROM order_machines ohm WHERE ohm.order_id = o.id), 0)
    ), 0) AS total_cost,
    COALESCE(SUM(o.total_amount), 0) - COALESCE(SUM(
        COALESCE((SELECT SUM(om.unit_cost * om.quantity) FROM order_materials om WHERE om.order_id = o.id), 0) +
        COALESCE((SELECT SUM(lm.hourly_rate * ol.duration_hours) FROM order_labor ol JOIN labor_master lm ON lm.id = ol.labor_id WHERE ol.order_id = o.id), 0) +
        COALESCE((SELECT SUM(ohm.hourly_cost * ohm.uptime_hours) FROM order_machines ohm WHERE ohm.order_id = o.id), 0)
    ), 0) AS net_profit,
    CURRENT_TIMESTAMP AS last_updated
FROM orders o;

-- Restore unique index for materialized view concurrent refresh
CREATE UNIQUE INDEX idx_mv_dashboard_kpi_refresh ON mv_dashboard_kpi(last_updated);

