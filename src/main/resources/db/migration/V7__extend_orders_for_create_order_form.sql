-- Extend orders table to persist all fields from Create Order form

ALTER TABLE orders ADD COLUMN IF NOT EXISTS customer_name VARCHAR(255);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS customer_contact VARCHAR(255);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS customer_code VARCHAR(50);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS requested_delivery_date DATE;

ALTER TABLE orders ADD COLUMN IF NOT EXISTS product_type VARCHAR(100);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS fabric_type VARCHAR(100);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS gsm INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS base_color VARCHAR(100);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS size_s INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS size_m INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS size_l INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS size_xl INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS print_type VARCHAR(100);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS number_of_colors INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS print_placement VARCHAR(100);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS design_reference VARCHAR(255);

ALTER TABLE orders ADD COLUMN IF NOT EXISTS order_quantity INTEGER;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS budget_threshold_percent DECIMAL(5, 2) DEFAULT 100;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS delivery_proximity_alert_enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS proximity_threshold_hours INTEGER DEFAULT 48;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS budget_overrun_alert_enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS notes TEXT;

CREATE INDEX IF NOT EXISTS idx_orders_customer_code ON orders(customer_code);
CREATE INDEX IF NOT EXISTS idx_orders_product_type ON orders(product_type);
CREATE INDEX IF NOT EXISTS idx_orders_requested_delivery_date ON orders(requested_delivery_date);
