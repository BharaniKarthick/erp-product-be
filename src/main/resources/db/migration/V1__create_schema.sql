-- ERP Database Schema for Printing Business
-- PostgreSQL Database

-- =============================================
-- 1. AUTHENTICATION & USER MANAGEMENT
-- =============================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    role_id BIGINT REFERENCES roles(id),
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 2. CUSTOMER MANAGEMENT
-- =============================================

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    customer_code VARCHAR(50) UNIQUE NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(255),
    phone VARCHAR(20),
    mobile VARCHAR(20),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'USA',
    tax_id VARCHAR(50),
    credit_limit DECIMAL(15, 2) DEFAULT 0,
    payment_terms VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 3. PRODUCT/SERVICE CATALOG
-- =============================================

CREATE TABLE product_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    parent_category_id BIGINT REFERENCES product_categories(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT REFERENCES product_categories(id),
    unit_of_measure VARCHAR(20) DEFAULT 'PCS',
    base_price DECIMAL(15, 2) NOT NULL DEFAULT 0,
    cost_price DECIMAL(15, 2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Printing specifications (paper type, size, color, finishing)
CREATE TABLE printing_specifications (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    paper_type VARCHAR(100),
    paper_weight VARCHAR(50),
    paper_size VARCHAR(50),
    color_type VARCHAR(50), -- Full Color, Black & White, Spot Color
    printing_sides VARCHAR(20), -- Single, Double
    finishing VARCHAR(100), -- Lamination, Binding, Cutting, etc.
    minimum_quantity INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 4. ORDER MANAGEMENT
-- =============================================

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT REFERENCES customers(id) NOT NULL,
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    required_date DATE,
    delivery_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, CONFIRMED, IN_PRODUCTION, READY, DELIVERED, CANCELLED
    priority VARCHAR(20) DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    payment_status VARCHAR(50) DEFAULT 'UNPAID', -- UNPAID, PARTIAL, PAID
    payment_method VARCHAR(50),
    subtotal DECIMAL(15, 2) DEFAULT 0,
    tax_amount DECIMAL(15, 2) DEFAULT 0,
    discount_amount DECIMAL(15, 2) DEFAULT 0,
    total_amount DECIMAL(15, 2) DEFAULT 0,
    paid_amount DECIMAL(15, 2) DEFAULT 0,
    special_instructions TEXT,
    delivery_address TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id),
    description TEXT,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0,
    tax_percent DECIMAL(5, 2) DEFAULT 0,
    line_total DECIMAL(15, 2) NOT NULL,
    -- Printing specific fields
    paper_type VARCHAR(100),
    paper_size VARCHAR(50),
    color_type VARCHAR(50),
    printing_sides VARCHAR(20),
    finishing VARCHAR(100),
    artwork_file_url VARCHAR(500),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_status_history (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    changed_by BIGINT REFERENCES users(id),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 5. INVENTORY MANAGEMENT
-- =============================================

CREATE TABLE inventory_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory_items (
    id BIGSERIAL PRIMARY KEY,
    item_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT REFERENCES inventory_categories(id),
    unit_of_measure VARCHAR(20) DEFAULT 'KG',
    current_quantity DECIMAL(15, 3) DEFAULT 0,
    minimum_quantity DECIMAL(15, 3) DEFAULT 0,
    maximum_quantity DECIMAL(15, 3),
    reorder_point DECIMAL(15, 3),
    unit_cost DECIMAL(15, 2) DEFAULT 0,
    location VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory_transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(50) NOT NULL, -- PURCHASE, USAGE, ADJUSTMENT, RETURN, TRANSFER
    inventory_item_id BIGINT REFERENCES inventory_items(id),
    reference_number VARCHAR(100),
    reference_type VARCHAR(50), -- ORDER, PURCHASE_ORDER, ADJUSTMENT, etc.
    reference_id BIGINT,
    quantity DECIMAL(15, 3) NOT NULL,
    unit_cost DECIMAL(15, 2),
    total_cost DECIMAL(15, 2),
    balance_after DECIMAL(15, 3),
    transaction_date DATE NOT NULL DEFAULT CURRENT_DATE,
    notes TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory_adjustments (
    id BIGSERIAL PRIMARY KEY,
    adjustment_number VARCHAR(50) UNIQUE NOT NULL,
    adjustment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    adjustment_type VARCHAR(50) NOT NULL, -- INCREASE, DECREASE, CORRECTION
    reason VARCHAR(255),
    status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    approved_by BIGINT REFERENCES users(id),
    approved_at TIMESTAMP,
    notes TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory_adjustment_items (
    id BIGSERIAL PRIMARY KEY,
    adjustment_id BIGINT REFERENCES inventory_adjustments(id) ON DELETE CASCADE,
    inventory_item_id BIGINT REFERENCES inventory_items(id),
    old_quantity DECIMAL(15, 3),
    new_quantity DECIMAL(15, 3),
    adjusted_quantity DECIMAL(15, 3) NOT NULL,
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 6. PRODUCTION MANAGEMENT
-- =============================================

CREATE TABLE labor_master (
    id BIGSERIAL PRIMARY KEY,
    employee_code VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    job_title VARCHAR(100),
    department VARCHAR(100),
    hourly_rate DECIMAL(10, 2),
    daily_rate DECIMAL(10, 2),
    skill_level VARCHAR(50), -- TRAINEE, JUNIOR, SENIOR, EXPERT
    is_active BOOLEAN DEFAULT TRUE,
    hire_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production_jobs (
    id BIGSERIAL PRIMARY KEY,
    job_number VARCHAR(50) UNIQUE NOT NULL,
    order_id BIGINT REFERENCES orders(id),
    order_item_id BIGINT REFERENCES order_items(id),
    job_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, IN_PROGRESS, COMPLETED, ON_HOLD, CANCELLED
    priority VARCHAR(20) DEFAULT 'NORMAL',
    scheduled_start_date DATE,
    scheduled_end_date DATE,
    actual_start_date DATE,
    actual_end_date DATE,
    estimated_hours DECIMAL(10, 2),
    actual_hours DECIMAL(10, 2),
    notes TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production_assignments (
    id BIGSERIAL PRIMARY KEY,
    production_job_id BIGINT REFERENCES production_jobs(id) ON DELETE CASCADE,
    labor_id BIGINT REFERENCES labor_master(id),
    assigned_date DATE NOT NULL DEFAULT CURRENT_DATE,
    hours_worked DECIMAL(10, 2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'ASSIGNED', -- ASSIGNED, WORKING, COMPLETED
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 7. SYSTEM SETTINGS & CONFIGURATION
-- =============================================

CREATE TABLE system_settings (
    id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_type VARCHAR(50), -- STRING, NUMBER, BOOLEAN, JSON
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    updated_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 8. AUDIT LOG
-- =============================================

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    old_values JSONB,
    new_values JSONB,
    ip_address VARCHAR(50),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 9. INDEXES FOR PERFORMANCE
-- =============================================

-- Users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role_id);

-- Customers
CREATE INDEX idx_customers_code ON customers(customer_code);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_active ON customers(is_active);

-- Products
CREATE INDEX idx_products_code ON products(product_code);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_active ON products(is_active);

-- Orders
CREATE INDEX idx_orders_number ON orders(order_number);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);

-- Order Items
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);

-- Inventory
CREATE INDEX idx_inventory_code ON inventory_items(item_code);
CREATE INDEX idx_inventory_category ON inventory_items(category_id);
CREATE INDEX idx_inventory_transactions_item ON inventory_transactions(inventory_item_id);
CREATE INDEX idx_inventory_transactions_date ON inventory_transactions(transaction_date);

-- Production
CREATE INDEX idx_production_jobs_order ON production_jobs(order_id);
CREATE INDEX idx_production_jobs_status ON production_jobs(status);
CREATE INDEX idx_labor_code ON labor_master(employee_code);

-- =============================================
-- 10. INITIAL DATA
-- =============================================

-- Insert default roles
INSERT INTO roles (name, description) VALUES
    ('ADMIN', 'System Administrator'),
    ('MANAGER', 'Manager'),
    ('SALES', 'Sales Representative'),
    ('PRODUCTION', 'Production Staff'),
    ('WAREHOUSE', 'Warehouse Staff');

-- Insert default admin user (password: admin123)
INSERT INTO users (username, email, password_hash, first_name, last_name, role_id) VALUES
    ('admin', 'admin@erp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1JED.QgFy5p5SfC4BuWzXfH6zX.7GfW', 'System', 'Admin', 
     (SELECT id FROM roles WHERE name = 'ADMIN'));

-- Insert default inventory categories
INSERT INTO inventory_categories (name, description) VALUES
    ('Paper Stock', 'All types of paper materials'),
    ('Ink', 'Printing inks and toners'),
    ('Finishing Materials', 'Lamination, binding materials'),
    ('Packaging', 'Boxes, wrapping materials');

-- Insert default product categories
INSERT INTO product_categories (name, description) VALUES
    ('Business Cards', 'Business card printing services'),
    ('Brochures', 'Brochure and flyer printing'),
    ('Banners', 'Large format banner printing'),
    ('Custom Printing', 'Custom printing services');

-- Insert system settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description, is_public) VALUES
    ('company_name', 'PrintERP Solutions', 'STRING', 'Company name', true),
    ('default_tax_rate', '10', 'NUMBER', 'Default tax rate percentage', true),
    ('currency_symbol', '$', 'STRING', 'Currency symbol', true),
    ('low_stock_alert_enabled', 'true', 'BOOLEAN', 'Enable low stock alerts', false);
