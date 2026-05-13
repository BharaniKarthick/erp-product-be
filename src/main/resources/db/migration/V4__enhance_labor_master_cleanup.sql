-- Migration V4: Enhance Labor Master & Clean Up Unused Tables
-- Add fields for shift management, photos, and approval workflow
-- Remove unused production tables that are not integrated into the ERP workflow

-- =============================================
-- 1. ENHANCE LABOR_MASTER TABLE
-- =============================================

-- Add shift management fields
ALTER TABLE labor_master ADD COLUMN IF NOT EXISTS shift_type VARCHAR(20) DEFAULT 'DAY';
ALTER TABLE labor_master ADD COLUMN IF NOT EXISTS shift_wage DECIMAL(10, 2);
ALTER TABLE labor_master ADD COLUMN IF NOT EXISTS photo_url VARCHAR(500);
ALTER TABLE labor_master ADD COLUMN IF NOT EXISTS approval_status VARCHAR(20) DEFAULT 'APPROVED';

COMMENT ON COLUMN labor_master.shift_type IS 'Employee shift preference: DAY, NIGHT, ROTATING';
COMMENT ON COLUMN labor_master.shift_wage IS 'Standard wage per 8-hour shift';
COMMENT ON COLUMN labor_master.photo_url IS 'URL or path to employee profile photo';
COMMENT ON COLUMN labor_master.approval_status IS 'Registration approval status: DRAFT, PENDING_APPROVAL, APPROVED, REJECTED';

-- Update existing records to have default values
UPDATE labor_master SET 
    shift_type = 'DAY',
    shift_wage = COALESCE(daily_rate, hourly_rate * 8, 0),
    approval_status = 'APPROVED'
WHERE shift_type IS NULL OR shift_wage IS NULL OR approval_status IS NULL;

-- =============================================
-- 2. DROP UNUSED TABLES
-- =============================================

-- Drop production-related tables that are not integrated into current workflow
-- These tables were part of initial schema but not used by any services/controllers

DROP TABLE IF EXISTS production_assignments CASCADE;
DROP TABLE IF EXISTS production_jobs CASCADE;
DROP TABLE IF EXISTS order_status_history CASCADE;

-- =============================================
-- 3. CREATE INDEXES FOR PERFORMANCE
-- =============================================

CREATE INDEX IF NOT EXISTS idx_labor_master_department ON labor_master(department);
CREATE INDEX IF NOT EXISTS idx_labor_master_active ON labor_master(is_active);
CREATE INDEX IF NOT EXISTS idx_labor_master_approval_status ON labor_master(approval_status);
CREATE INDEX IF NOT EXISTS idx_labor_master_shift_type ON labor_master(shift_type);

-- =============================================
-- 4. CREATE VIEW FOR LABOR ANALYTICS
-- =============================================

-- View: Labor Summary by Department
CREATE OR REPLACE VIEW v_labor_summary_by_department AS
SELECT 
    department,
    COUNT(*) AS total_employees,
    COUNT(CASE WHEN is_active = TRUE THEN 1 END) AS active_employees,
    COUNT(CASE WHEN approval_status = 'PENDING_APPROVAL' THEN 1 END) AS pending_approval,
    AVG(shift_wage) AS avg_shift_wage,
    SUM(shift_wage) AS total_shift_wages
FROM labor_master
WHERE is_active = TRUE
GROUP BY department;

-- View: Labor Roles Distribution
CREATE OR REPLACE VIEW v_labor_roles_distribution AS
SELECT 
    job_title AS role,
    COUNT(*) AS employee_count,
    AVG(shift_wage) AS avg_wage,
    MIN(shift_wage) AS min_wage,
    MAX(shift_wage) AS max_wage
FROM labor_master
WHERE is_active = TRUE
GROUP BY job_title
ORDER BY employee_count DESC;

-- =============================================
-- 5. INSERT SAMPLE DATA (if needed)
-- =============================================

-- Update existing sample employees with shift data
DO $$
BEGIN
    -- Sample employee updates (only if records exist)
    IF EXISTS (SELECT 1 FROM labor_master WHERE employee_code = 'EMP-001') THEN
        UPDATE labor_master SET 
            shift_type = 'DAY',
            shift_wage = 185.00,
            approval_status = 'APPROVED'
        WHERE employee_code = 'EMP-001';
    END IF;
END $$;

-- =============================================
-- 6. ADD CONSTRAINTS
-- =============================================

-- Add check constraint for shift_type
ALTER TABLE labor_master DROP CONSTRAINT IF EXISTS chk_shift_type;
ALTER TABLE labor_master ADD CONSTRAINT chk_shift_type 
    CHECK (shift_type IN ('DAY', 'NIGHT', 'ROTATING'));

-- Add check constraint for approval_status
ALTER TABLE labor_master DROP CONSTRAINT IF EXISTS chk_approval_status;
ALTER TABLE labor_master ADD CONSTRAINT chk_approval_status 
    CHECK (approval_status IN ('DRAFT', 'PENDING_APPROVAL', 'APPROVED', 'REJECTED'));

-- Ensure shift_wage is positive  
ALTER TABLE labor_master DROP CONSTRAINT IF EXISTS chk_shift_wage_positive;
ALTER TABLE labor_master ADD CONSTRAINT chk_shift_wage_positive 
    CHECK (shift_wage IS NULL OR shift_wage >= 0);

COMMENT ON TABLE labor_master IS 'Master data for workforce management including shift types, wages, and approval workflow';
