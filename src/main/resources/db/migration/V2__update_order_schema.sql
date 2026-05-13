-- Migration V2: Update Order Schema for General Printing Business
-- This migration restructures orders to support materials, labor, and machine tracking

-- =============================================
-- 1. ADD NEW COLUMNS TO ORDERS TABLE
-- =============================================

-- Add warehouse and progress tracking fields
ALTER TABLE orders ADD COLUMN warehouse_origin VARCHAR(50);
ALTER TABLE orders ADD COLUMN completion_progress DECIMAL(5, 2) DEFAULT 0 CHECK (completion_progress >= 0 AND completion_progress <= 100);
ALTER TABLE orders ADD COLUMN current_stage VARCHAR(100);

-- Add financial tracking columns
ALTER TABLE orders ADD COLUMN quoted_price DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN estimated_labor_cost DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN estimated_material_cost DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN estimated_machine_cost DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN actual_labor_cost DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN actual_material_cost DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN actual_machine_cost DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN profit_loss DECIMAL(15, 2) DEFAULT 0;
ALTER TABLE orders ADD COLUMN margin_percentage DECIMAL(5, 2) DEFAULT 0;

-- Update priority field to match HTML requirements
ALTER TABLE orders ALTER COLUMN priority TYPE VARCHAR(50);
COMMENT ON COLUMN orders.priority IS 'Priority levels: CRITICAL_HIGH, STANDARD_NORMAL, LOW_DEFERRED';

-- Update status field comment
COMMENT ON COLUMN orders.status IS 'Status: PENDING, RUNNING, COMPLETED, ON_HOLD, CANCELLED, READY_TO_SHIP, SHIPPED';

-- =============================================
-- 2. CREATE MACHINES TABLE
-- =============================================

CREATE TABLE machines (
    id BIGSERIAL PRIMARY KEY,
    machine_code VARCHAR(50) UNIQUE NOT NULL,
    machine_name VARCHAR(255) NOT NULL,
    machine_type VARCHAR(100),
    description TEXT,
    location VARCHAR(100),
    hourly_cost DECIMAL(10, 2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'OPERATIONAL', -- OPERATIONAL, MAINTENANCE, BROKEN, RETIRED
    purchase_date DATE,
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_machines_code ON machines(machine_code);
CREATE INDEX idx_machines_status ON machines(status);

-- =============================================
-- 3. CREATE ORDER MATERIALS TABLE
-- =============================================

CREATE TABLE order_materials (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    inventory_item_id BIGINT REFERENCES inventory_items(id),
    material_code VARCHAR(50),
    material_name VARCHAR(255) NOT NULL,
    description TEXT,
    quantity DECIMAL(15, 3) NOT NULL,
    unit_of_measure VARCHAR(20) DEFAULT 'KG',
    unit_cost DECIMAL(15, 2) NOT NULL,
    total_cost DECIMAL(15, 2) NOT NULL,
    stock_status VARCHAR(50) DEFAULT 'IN_STOCK', -- IN_STOCK, LOW_STOCK, OUT_OF_STOCK, REQUIRED_PO
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_materials_order_id ON order_materials(order_id);
CREATE INDEX idx_order_materials_inventory_item_id ON order_materials(inventory_item_id);

-- =============================================
-- 4. CREATE ORDER LABOR TABLE
-- =============================================

CREATE TABLE order_labor (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    labor_id BIGINT REFERENCES labor_master(id),
    operator_code VARCHAR(50),
    operator_name VARCHAR(255),
    shift_role VARCHAR(100) NOT NULL,
    duration_hours DECIMAL(10, 2) NOT NULL,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    total_cost DECIMAL(15, 2) NOT NULL,
    shift_date DATE DEFAULT CURRENT_DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_labor_order_id ON order_labor(order_id);
CREATE INDEX idx_order_labor_labor_id ON order_labor(labor_id);

-- =============================================
-- 5. CREATE ORDER MACHINES TABLE
-- =============================================

CREATE TABLE order_machines (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    machine_id BIGINT REFERENCES machines(id),
    machine_code VARCHAR(50),
    machine_name VARCHAR(255),
    process_description VARCHAR(255) NOT NULL,
    uptime_hours DECIMAL(10, 2) NOT NULL,
    hourly_cost DECIMAL(10, 2) NOT NULL,
    total_cost DECIMAL(15, 2) NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_machines_order_id ON order_machines(order_id);
CREATE INDEX idx_order_machines_machine_id ON order_machines(machine_id);

-- =============================================
-- 6. CREATE ORDER TRANSACTIONS TABLE (Audit Log)
-- =============================================

CREATE TABLE order_transactions (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transaction_type VARCHAR(50) NOT NULL, -- MATERIAL_DEDUCTION, LABOR_ENTRY, MACHINE_ALLOCATION, STOCK_ADJUSTMENT, STATUS_CHANGE
    action_description VARCHAR(500) NOT NULL,
    quantity_or_duration VARCHAR(50), -- e.g., "120 kg", "1 Shift", "2.5 h"
    user_name VARCHAR(100),
    cost_impact DECIMAL(15, 2), -- Can be NULL for non-cost transactions
    notes TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_transactions_order_id ON order_transactions(order_id);
CREATE INDEX idx_order_transactions_date ON order_transactions(transaction_date);
CREATE INDEX idx_order_transactions_type ON order_transactions(transaction_type);

-- =============================================
-- 7. CREATE ORDER ALERTS TABLE
-- =============================================

CREATE TABLE order_alerts (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    alert_type VARCHAR(50) NOT NULL, -- DELIVERY_PROXIMITY, BUDGET_OVERRUN, LOW_INVENTORY, DELAY
    alert_name VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    threshold_value VARCHAR(100), -- e.g., "48 Hours", "100%"
    alert_status VARCHAR(50) DEFAULT 'ACTIVE', -- ACTIVE, TRIGGERED, RESOLVED, DISMISSED
    triggered_at TIMESTAMP,
    resolved_at TIMESTAMP,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_alerts_order_id ON order_alerts(order_id);
CREATE INDEX idx_order_alerts_type ON order_alerts(alert_type);
CREATE INDEX idx_order_alerts_status ON order_alerts(alert_status);

-- =============================================
-- 8. REMOVE PAPER-SPECIFIC COLUMNS FROM ORDER_ITEMS
-- =============================================

-- Drop printing-specific columns (keeping the table generic)
ALTER TABLE order_items DROP COLUMN IF EXISTS paper_type;
ALTER TABLE order_items DROP COLUMN IF EXISTS paper_size;
ALTER TABLE order_items DROP COLUMN IF EXISTS color_type;
ALTER TABLE order_items DROP COLUMN IF EXISTS printing_sides;
ALTER TABLE order_items DROP COLUMN IF EXISTS finishing;
ALTER TABLE order_items DROP COLUMN IF EXISTS artwork_file_url;

-- Add generic specification field
ALTER TABLE order_items ADD COLUMN specifications JSONB;
COMMENT ON COLUMN order_items.specifications IS 'Generic JSON field for product-specific specifications';

-- =============================================
-- 9. UPDATE INVENTORY CATEGORIES
-- =============================================

-- Insert common inventory categories for general printing business
INSERT INTO inventory_categories (name, description, is_active) VALUES 
('Dyes & Inks', 'Printing dyes, inks, and colorants', TRUE),
('Papers & Media', 'Paper stocks, vinyl, canvas, and other printing media', TRUE),
('Chemicals', 'Solvents, cleaning agents, and other chemicals', TRUE),
('Consumables', 'General consumables and supplies', TRUE),
('Raw Materials', 'Raw materials for production', TRUE)
ON CONFLICT (name) DO NOTHING;

-- =============================================
-- 10. UPDATE PRODUCT CATEGORIES
-- =============================================

-- Insert common product categories for general printing business
INSERT INTO product_categories (name, description, is_active) VALUES 
('Banners & Signage', 'Vinyl banners, posters, and large format signage', TRUE),
('Brochures & Flyers', 'Marketing materials and promotional prints', TRUE),
('Books & Binding', 'Book printing and binding services', TRUE),
('Business Cards', 'Business cards and personal stationery', TRUE),
('Custom Printing', 'Custom and specialty printing services', TRUE)
ON CONFLICT (name) DO NOTHING;

-- =============================================
-- 11. CREATE VIEWS FOR REPORTING
-- =============================================

-- View: Order Summary with Costs
CREATE OR REPLACE VIEW v_order_summary AS
SELECT 
    o.id AS order_id,
    o.order_number,
    c.company_name AS customer_name,
    o.order_date,
    o.required_date,
    o.delivery_date,
    o.status,
    o.priority,
    o.warehouse_origin,
    o.completion_progress,
    o.current_stage,
    o.quoted_price,
    o.estimated_labor_cost,
    o.estimated_material_cost,
    o.estimated_machine_cost,
    (o.estimated_labor_cost + o.estimated_material_cost + o.estimated_machine_cost) AS total_estimated_cost,
    o.actual_labor_cost,
    o.actual_material_cost,
    o.actual_machine_cost,
    (o.actual_labor_cost + o.actual_material_cost + o.actual_machine_cost) AS total_actual_cost,
    o.profit_loss,
    o.margin_percentage,
    o.created_at
FROM orders o
LEFT JOIN customers c ON o.customer_id = c.id;

-- View: Order Materials Summary
CREATE OR REPLACE VIEW v_order_materials_summary AS
SELECT 
    om.order_id,
    o.order_number,
    COUNT(om.id) AS material_count,
    SUM(om.total_cost) AS total_materials_cost
FROM order_materials om
INNER JOIN orders o ON om.order_id = o.id
GROUP BY om.order_id, o.order_number;

-- View: Order Labor Summary
CREATE OR REPLACE VIEW v_order_labor_summary AS
SELECT 
    ol.order_id,
    o.order_number,
    COUNT(ol.id) AS labor_count,
    SUM(ol.duration_hours) AS total_hours,
    SUM(ol.total_cost) AS total_labor_cost
FROM order_labor ol
INNER JOIN orders o ON ol.order_id = o.id
GROUP BY ol.order_id, o.order_number;

-- =============================================
-- 12. CREATE TRIGGERS FOR AUTO-CALCULATION
-- =============================================

-- Function to calculate order profit/loss
CREATE OR REPLACE FUNCTION calculate_order_profit()
RETURNS TRIGGER AS $$
BEGIN
    NEW.profit_loss := NEW.quoted_price - (NEW.actual_labor_cost + NEW.actual_material_cost + NEW.actual_machine_cost);
    
    IF NEW.quoted_price > 0 THEN
        NEW.margin_percentage := (NEW.profit_loss / NEW.quoted_price) * 100;
    ELSE
        NEW.margin_percentage := 0;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to auto-calculate profit on insert/update
CREATE TRIGGER trg_calculate_order_profit
BEFORE INSERT OR UPDATE ON orders
FOR EACH ROW
EXECUTE FUNCTION calculate_order_profit();

-- =============================================
-- 13. ADD SAMPLE DATA
-- =============================================

-- Update existing orders with new fields (optional)
UPDATE orders SET 
    warehouse_origin = 'WH-Main-01',
    completion_progress = 0,
    current_stage = 'Pending',
    quoted_price = total_amount,
    estimated_labor_cost = total_amount * 0.25,
    estimated_material_cost = total_amount * 0.50,
    estimated_machine_cost = total_amount * 0.15
WHERE warehouse_origin IS NULL;

-- Insert sample machines
INSERT INTO machines (machine_code, machine_name, machine_type, location, hourly_cost, status) VALUES 
('PRN-HEIDEL-X', 'Heidelberg Multi-Color Press', 'Screen Press', 'Production Floor A', 120.00, 'OPERATIONAL'),
('CUT-POLAR-92', 'Polar 92 Paper Cutter', 'Cutting Machine', 'Production Floor A', 45.00, 'OPERATIONAL'),
('BIND-MULLER-M', 'Muller Martini Binding System', 'Binding Machine', 'Bindery', 85.00, 'OPERATIONAL'),
('DIGITAL-HP-900', 'HP Indigo 9000 Digital Press', 'Digital Press', 'Digital Print Room', 150.00, 'OPERATIONAL')
ON CONFLICT (machine_code) DO NOTHING;

COMMENT ON TABLE orders IS 'Main orders table with cost tracking and progress monitoring';
COMMENT ON TABLE order_materials IS 'Materials used for each order';
COMMENT ON TABLE order_labor IS 'Labor assignments and hours for orders';
COMMENT ON TABLE order_machines IS 'Machine allocations and usage for orders';
COMMENT ON TABLE order_transactions IS 'Audit log of all order-related transactions';
COMMENT ON TABLE order_alerts IS 'Alert configurations and status for orders';
COMMENT ON TABLE machines IS 'Master data for production machines and equipment';
