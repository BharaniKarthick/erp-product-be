-- Migration V3: Enhance Inventory Management
-- Add specifications and alert configuration fields to inventory items

-- =============================================
-- 1. ADD NEW COLUMNS TO INVENTORY_ITEMS TABLE
-- =============================================

-- Add specification columns for textiles and materials
ALTER TABLE inventory_items ADD COLUMN IF NOT EXISTS weight VARCHAR(100);
ALTER TABLE inventory_items ADD COLUMN IF NOT EXISTS width VARCHAR(100);
ALTER TABLE inventory_items ADD COLUMN IF NOT EXISTS supplier VARCHAR(255);

-- Add alert configuration
ALTER TABLE inventory_items ADD COLUMN IF NOT EXISTS low_stock_alerts_enabled BOOLEAN DEFAULT TRUE;

COMMENT ON COLUMN inventory_items.weight IS 'Material weight specification (e.g., "180 GSM")';
COMMENT ON COLUMN inventory_items.width IS 'Material width specification (e.g., "150 CM")';
COMMENT ON COLUMN inventory_items.supplier IS 'Supplier or manufacturer name';
COMMENT ON COLUMN inventory_items.low_stock_alerts_enabled IS 'Enable/disable low stock alerts for this item';

-- =============================================
-- 2. UPDATE EXISTING CATEGORIES
-- =============================================

-- Add more specific categories for printing business
INSERT INTO inventory_categories (name, description, is_active) VALUES 
('Solvents', 'Industrial solvents and cleaning agents', TRUE),
('Auxiliaries', 'Auxiliary chemicals and additives', TRUE),
('Raw Materials', 'Raw materials for production', TRUE),
('Textiles', 'Fabric and textile materials', TRUE)
ON CONFLICT (name) DO NOTHING;

-- =============================================
-- 3. ADD SAMPLE INVENTORY ITEMS
-- =============================================

-- Insert sample items if not exists
DO $$
BEGIN
    -- Cyanine Blue Dye XL
    IF NOT EXISTS (SELECT 1 FROM inventory_items WHERE item_code = 'DYE-CYA-042') THEN
        INSERT INTO inventory_items (item_code, name, description, category_id, unit_of_measure, 
                                      current_quantity, minimum_quantity, reorder_point, unit_cost, 
                                      location, low_stock_alerts_enabled, is_active)
        SELECT 'DYE-CYA-042', 'Cyanine Blue Dye XL', 'High-quality cyanine blue dye for textile printing',
               id, 'kg', 450.00, 100.00, 100.00, 12.40, 'Warehouse A, Shelf 2',
               TRUE, TRUE
        FROM inventory_categories WHERE name = 'Dyes & Inks'
        LIMIT 1;
    END IF;
    
    -- Isopropyl Alcohol 99%
    IF NOT EXISTS (SELECT 1 FROM inventory_items WHERE item_code = 'SLV-IPA-109') THEN
        INSERT INTO inventory_items (item_code, name, description, category_id, unit_of_measure,
                                      current_quantity, minimum_quantity, reorder_point, unit_cost,
                                      location, low_stock_alerts_enabled, is_active)
        SELECT 'SLV-IPA-109', 'Isopropyl Alcohol 99%', 'High-purity isopropyl alcohol solvent',
               id, 'L', 12.50, 50.00, 50.00, 4.15, 'Warehouse A, Chemical Storage',
               TRUE, TRUE
        FROM inventory_categories WHERE name = 'Solvents'
        LIMIT 1;
    END IF;
    
    -- Sodium Hydroxide Pellets
    IF NOT EXISTS (SELECT 1 FROM inventory_items WHERE item_code = 'CHM-SOH-882') THEN
        INSERT INTO inventory_items (item_code, name, description, category_id, unit_of_measure,
                                      current_quantity, minimum_quantity, reorder_point, unit_cost,
                                      location, low_stock_alerts_enabled, is_active)
        SELECT 'CHM-SOH-882', 'Sodium Hydroxide Pellets', 'Industrial grade sodium hydroxide',
               id, 'kg', 1200.00, 200.00, 250.00, 0.85, 'Warehouse A, Chemical Storage',
               TRUE, TRUE
        FROM inventory_categories WHERE name = 'Chemicals'
        LIMIT 1;
    END IF;
    
    -- Premium Organic Cotton
    IF NOT EXISTS (SELECT 1 FROM inventory_items WHERE item_code = 'TX-COT-882') THEN
        INSERT INTO inventory_items (item_code, name, description, category_id, unit_of_measure,
                                      current_quantity, minimum_quantity, reorder_point, unit_cost,
                                      location, weight, width, supplier, low_stock_alerts_enabled, is_active)
        SELECT 'TX-COT-882', 'Premium Organic Cotton', 'High-quality organic cotton fabric',
               id, 'm', 1240.00, 200.00, 300.00, 12.50, 'Aisle 4, Shelf B',
               '180 GSM', '150 CM', 'Ethical Fibers Co.', TRUE, TRUE
        FROM inventory_categories WHERE name = 'Raw Materials'
        LIMIT 1;
    END IF;
END $$;

-- =============================================
-- 4. CREATE SAMPLE TRANSACTIONS
-- =============================================

-- Insert sample transaction if not exists
DO $$
DECLARE
    item_id_var BIGINT;
BEGIN
    -- Get the inventory item ID
    SELECT id INTO item_id_var FROM inventory_items WHERE item_code = 'DYE-CYA-042' LIMIT 1;
    
    IF item_id_var IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM inventory_transactions 
        WHERE inventory_item_id = item_id_var 
        AND reference_number = 'PO-88231'
    ) THEN
        INSERT INTO inventory_transactions (
            transaction_type, inventory_item_id, reference_number, reference_type,
            quantity, unit_cost, total_cost, balance_after, transaction_date, notes
        ) VALUES (
            'PURCHASE', item_id_var, 'PO-88231', 'PURCHASE_ORDER',
            100.00, 12.40, 1240.00, 450.00, '2024-03-15', 
            'Purchase Order - Regular stock replenishment'
        );
    END IF;
END $$;

COMMENT ON TABLE inventory_items IS 'Inventory items including materials, chemicals, dyes, and textiles';
COMMENT ON TABLE inventory_transactions IS 'Inventory movement transactions with audit trail';
