ALTER TABLE order_materials
ADD COLUMN IF NOT EXISTS material_type VARCHAR(20) DEFAULT 'ACTUAL';

UPDATE order_materials
SET material_type = 'ACTUAL'
WHERE material_type IS NULL OR TRIM(material_type) = '';

CREATE INDEX IF NOT EXISTS idx_order_materials_order_type
ON order_materials(order_id, material_type);
