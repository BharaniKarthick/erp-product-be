# Database Schema Documentation

## Overview
This document describes the complete database schema for the ERP system for printing businesses.

---

## Entity Relationship Summary

### Core Entities
1. **Users & Roles** - Authentication and authorization
2. **Customers** - Customer information
3. **Products & Categories** - Product catalog
4. **Orders & Order Items** - Order management with printing specifications

5. **Inventory** - Inventory items, categories, and transactions
6. **Production** - Labor master, production jobs, assignments

---

## Table Schemas

### 1. roles
Stores user roles for access control.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| name | VARCHAR(50) | Role name (ADMIN, MANAGER, SALES, etc.) |
| description | VARCHAR(255) | Role description |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `name` (unique)

---

### 2. users
System users with authentication details.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| username | VARCHAR(100) | Unique username |
| email | VARCHAR(255) | User email (unique) |
| password_hash | VARCHAR(255) | Encrypted password |
| first_name | VARCHAR(100) | First name |
| last_name | VARCHAR(100) | Last name |
| phone | VARCHAR(20) | Phone number |
| role_id | BIGINT | Foreign key to roles |
| is_active | BOOLEAN | Active status |
| last_login | TIMESTAMP | Last login time |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `username`, `email`, `role_id`

---

### 3. customers
Customer/client information.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| customer_code | VARCHAR(50) | Unique customer code |
| company_name | VARCHAR(255) | Company name |
| contact_person | VARCHAR(100) | Contact person name |
| email | VARCHAR(255) | Email address |
| phone | VARCHAR(20) | Phone number |
| mobile | VARCHAR(20) | Mobile number |
| address_line1 | VARCHAR(255) | Address line 1 |
| address_line2 | VARCHAR(255) | Address line 2 |
| city | VARCHAR(100) | City |
| state | VARCHAR(100) | State/Province |
| postal_code | VARCHAR(20) | Postal/ZIP code |
| country | VARCHAR(100) | Country |
| tax_id | VARCHAR(50) | Tax identification number |
| credit_limit | DECIMAL(15,2) | Credit limit |
| payment_terms | VARCHAR(50) | Payment terms |
| is_active | BOOLEAN | Active status |
| notes | TEXT | Additional notes |
| created_by | BIGINT | Foreign key to users |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `customer_code`, `email`, `is_active`

---

### 4. product_categories
Product category hierarchy.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| name | VARCHAR(100) | Category name |
| description | TEXT | Description |
| parent_category_id | BIGINT | Parent category (self-reference) |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Examples**: Business Cards, Brochures, Banners, Custom Printing

---

### 5. products
Product/service catalog.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| product_code | VARCHAR(50) | Unique product code |
| name | VARCHAR(255) | Product name |
| description | TEXT | Product description |
| category_id | BIGINT | Foreign key to product_categories |
| unit_of_measure | VARCHAR(20) | Unit (PCS, SET, etc.) |
| base_price | DECIMAL(15,2) | Base selling price |
| cost_price | DECIMAL(15,2) | Cost price |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `product_code`, `category_id`, `is_active`

---

### 6. printing_specifications
Optional printing specifications for products.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| product_id | BIGINT | Foreign key to products |
| paper_type | VARCHAR(100) | Paper type (Matte, Glossy, etc.) |
| paper_weight | VARCHAR(50) | Paper weight |
| paper_size | VARCHAR(50) | Size (A4, Letter, Custom) |
| color_type | VARCHAR(50) | Full Color, B&W, Spot |
| printing_sides | VARCHAR(20) | Single, Double |
| finishing | VARCHAR(100) | Lamination, Binding, etc. |
| minimum_quantity | INT | Minimum order quantity |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

---

### 7. orders
Customer orders.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| order_number | VARCHAR(50) | Unique order number |
| customer_id | BIGINT | Foreign key to customers |
| order_date | DATE | Order date |
| required_date | DATE | Required/due date |
| delivery_date | DATE | Actual delivery date |
| status | VARCHAR(50) | Order status |
| priority | VARCHAR(20) | Priority (LOW, NORMAL, HIGH, URGENT) |
| payment_status | VARCHAR(50) | UNPAID, PARTIAL, PAID |
| payment_method | VARCHAR(50) | Payment method |
| subtotal | DECIMAL(15,2) | Subtotal amount |
| tax_amount | DECIMAL(15,2) | Tax amount |
| discount_amount | DECIMAL(15,2) | Discount amount |
| total_amount | DECIMAL(15,2) | Total amount |
| paid_amount | DECIMAL(15,2) | Amount paid |
| special_instructions | TEXT | Special instructions |
| delivery_address | TEXT | Delivery address |
| created_by | BIGINT | Foreign key to users |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Status Values**: PENDING, CONFIRMED, IN_PRODUCTION, READY, DELIVERED, CANCELLED

**Indexes**: `order_number`, `customer_id`, `order_date`, `status`, `payment_status`

---

### 8. order_items
Line items in orders with printing specifications.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| order_id | BIGINT | Foreign key to orders |
| product_id | BIGINT | Foreign key to products |
| description | TEXT | Item description |
| quantity | INT | Quantity |
| unit_price | DECIMAL(15,2) | Unit price |
| discount_percent | DECIMAL(5,2) | Discount percentage |
| tax_percent | DECIMAL(5,2) | Tax percentage |
| line_total | DECIMAL(15,2) | Line total |
| paper_type | VARCHAR(100) | Paper type |
| paper_size | VARCHAR(50) | Paper size |
| color_type | VARCHAR(50) | Color type |
| printing_sides | VARCHAR(20) | Printing sides |
| finishing | VARCHAR(100) | Finishing options |
| artwork_file_url | VARCHAR(500) | Artwork file URL |
| notes | TEXT | Additional notes |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `order_id`, `product_id`

---

### 9. order_status_history
Audit trail for order status changes.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| order_id | BIGINT | Foreign key to orders |
| old_status | VARCHAR(50) | Previous status |
| new_status | VARCHAR(50) | New status |
| changed_by | BIGINT | Foreign key to users |
| notes | TEXT | Change notes |
| created_at | TIMESTAMP | Change timestamp |

---

### 10. inventory_categories
Categories for inventory items.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| name | VARCHAR(100) | Category name |
| description | TEXT | Description |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Examples**: Paper Stock, Ink, Finishing Materials, Packaging

---

### 11. inventory_items
Raw materials and supplies inventory.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| item_code | VARCHAR(50) | Unique item code |
| name | VARCHAR(255) | Item name |
| description | TEXT | Description |
| category_id | BIGINT | Foreign key to inventory_categories |
| unit_of_measure | VARCHAR(20) | Unit (KG, LITER, ROLL, etc.) |
| current_quantity | DECIMAL(15,3) | Current stock quantity |
| minimum_quantity | DECIMAL(15,3) | Minimum stock level |
| maximum_quantity | DECIMAL(15,3) | Maximum stock level |
| reorder_point | DECIMAL(15,3) | Reorder point |
| unit_cost | DECIMAL(15,2) | Unit cost |
| location | VARCHAR(100) | Storage location |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `item_code`, `category_id`

---

### 12. inventory_transactions
All inventory movements (purchases, usage, adjustments).

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| transaction_type | VARCHAR(50) | PURCHASE, USAGE, ADJUSTMENT, RETURN, TRANSFER |
| inventory_item_id | BIGINT | Foreign key to inventory_items |
| reference_number | VARCHAR(100) | Reference document number |
| reference_type | VARCHAR(50) | Reference type (ORDER, PURCHASE_ORDER, etc.) |
| reference_id | BIGINT | Reference ID |
| quantity | DECIMAL(15,3) | Quantity (positive or negative) |
| unit_cost | DECIMAL(15,2) | Unit cost |
| total_cost | DECIMAL(15,2) | Total cost |
| balance_after | DECIMAL(15,3) | Balance after transaction |
| transaction_date | DATE | Transaction date |
| notes | TEXT | Transaction notes |
| created_by | BIGINT | Foreign key to users |
| created_at | TIMESTAMP | Creation timestamp |

**Indexes**: `inventory_item_id`, `transaction_date`

---

### 13. labor_master
Employee/labor information.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| employee_code | VARCHAR(50) | Unique employee code |
| first_name | VARCHAR(100) | First name |
| last_name | VARCHAR(100) | Last name |
| email | VARCHAR(255) | Email address |
| phone | VARCHAR(20) | Phone number |
| job_title | VARCHAR(100) | Job title |
| department | VARCHAR(100) | Department |
| hourly_rate | DECIMAL(10,2) | Hourly rate |
| daily_rate | DECIMAL(10,2) | Daily rate |
| skill_level | VARCHAR(50) | TRAINEE, JUNIOR, SENIOR, EXPERT |
| is_active | BOOLEAN | Active status |
| hire_date | DATE | Date of hire |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `employee_code`

---

### 16. production_jobs
Production jobs linked to orders.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| job_number | VARCHAR(50) | Unique job number |
| order_id | BIGINT | Foreign key to orders |
| order_item_id | BIGINT | Foreign key to order_items |
| job_name | VARCHAR(255) | Job name |
| status | VARCHAR(50) | PENDING, IN_PROGRESS, COMPLETED, ON_HOLD, CANCELLED |
| priority | VARCHAR(20) | Priority level |
| scheduled_start_date | DATE | Scheduled start |
| scheduled_end_date | DATE | Scheduled end |
| actual_start_date | DATE | Actual start |
| actual_end_date | DATE | Actual end |
| estimated_hours | DECIMAL(10,2) | Estimated hours |
| actual_hours | DECIMAL(10,2) | Actual hours |
| notes | TEXT | Job notes |
| created_by | BIGINT | Foreign key to users |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes**: `order_id`, `status`

---

### 17. production_assignments
Assign workers to production jobs.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| production_job_id | BIGINT | Foreign key to production_jobs |
| labor_id | BIGINT | Foreign key to labor_master |
| assigned_date | DATE | Assignment date |
| hours_worked | DECIMAL(10,2) | Hours worked |
| status | VARCHAR(50) | ASSIGNED, WORKING, COMPLETED |
| notes | TEXT | Assignment notes |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

---

### 18. system_settings
System configuration settings.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| setting_key | VARCHAR(100) | Setting key (unique) |
| setting_value | TEXT | Setting value |
| setting_type | VARCHAR(50) | STRING, NUMBER, BOOLEAN, JSON |
| description | TEXT | Setting description |
| is_public | BOOLEAN | Publicly accessible |
| updated_by | BIGINT | Foreign key to users |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

---

### 19. audit_log
System audit trail.

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| user_id | BIGINT | Foreign key to users |
| action | VARCHAR(100) | Action performed |
| entity_type | VARCHAR(100) | Entity type |
| entity_id | BIGINT | Entity ID |
| old_values | JSONB | Old values (JSON) |
| new_values | JSONB | New values (JSON) |
| ip_address | VARCHAR(50) | IP address |
| user_agent | TEXT | User agent string |
| created_at | TIMESTAMP | Action timestamp |

---

## Relationships

### One-to-Many
- `roles` → `users`
- `customers` → `orders`
- `orders` → `order_items`
- `orders` → `production_jobs`
- `product_categories` → `products`
- `products` → `order_items`
- `inventory_categories` → `inventory_items`
- `inventory_items` → `inventory_transactions`

- `production_jobs` → `production_assignments`
- `labor_master` → `production_assignments`

### Self-Referencing
- `product_categories` → `product_categories` (parent category)

---

## Initial Data

### Roles
- ADMIN
- MANAGER
- SALES
- PRODUCTION
- WAREHOUSE

### Default Admin User
- Username: `admin`
- Password: `admin123` (BCrypt hashed)
- Email: `admin@erp.com`

### Product Categories
- Business Cards
- Brochures
- Banners
- Custom Printing

### Inventory Categories
- Paper Stock
- Ink
- Finishing Materials
- Packaging

---

## Indexes Summary

All tables have:
- Primary key index on `id`
- Timestamp indexes on `created_at` where applicable

Additional indexes created for:
- Unique constraints (codes, emails, usernames)
- Foreign keys (for join performance)
- Search fields (status, dates, active flags)
- Full-text search candidates (names, descriptions)
