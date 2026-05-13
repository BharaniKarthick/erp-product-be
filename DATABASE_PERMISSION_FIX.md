# Database Permission Error Fix

## Error
```
SQL State  : 42501
Error Code : 0
Message    : ERROR: permission denied for schema public
```

## Root Cause
The database user `erp_user` doesn't have sufficient permissions on the PostgreSQL `public` schema to create tables (required by Flyway).

---

## ✅ Quick Fix (Option 1 - Recommended)

### Step 1: Connect to PostgreSQL as superuser
```bash
psql -U postgres
```

### Step 2: Run these commands
```sql
-- Connect to the database
\c erp_db

-- Grant schema permissions
GRANT ALL ON SCHEMA public TO erp_user;
GRANT CREATE ON SCHEMA public TO erp_user;
GRANT USAGE ON SCHEMA public TO erp_user;

-- Grant permissions on future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO erp_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO erp_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON FUNCTIONS TO erp_user;

-- Exit
\q
```

### Step 3: Restart Spring Boot application
```bash
mvn spring-boot:run
```

✅ **The error should be fixed!**

---

## 📜 Alternative Fix (Option 2 - Using Script)

### Run the fix script:
```bash
psql -U postgres -d erp_db -f scripts/fix-database-permissions.sql
```

---

## 🔧 Complete Fresh Setup (Option 3)

If you want to start fresh with proper permissions:

### Step 1: Drop existing database (if needed)
```bash
psql -U postgres
```
```sql
DROP DATABASE IF EXISTS erp_db;
DROP USER IF EXISTS erp_user;
\q
```

### Step 2: Run complete setup script
```bash
psql -U postgres -f scripts/setup-database.sql
```

### Step 3: Start application
```bash
mvn spring-boot:run
```

---

## 🔍 Verify Permissions

Connect as erp_user and check:
```bash
psql -U erp_user -d erp_db
```

```sql
-- Check current user
SELECT current_user;

-- Check schema permissions
SELECT 
    nspname as schema,
    has_schema_privilege('erp_user', nspname, 'CREATE') as has_create,
    has_schema_privilege('erp_user', nspname, 'USAGE') as has_usage
FROM pg_namespace
WHERE nspname = 'public';

-- Expected result:
--  schema  | has_create | has_usage 
-- ---------+------------+-----------
--  public  | t          | t
```

---

## 📝 What These Permissions Do

| Permission | Purpose |
|------------|---------|
| `GRANT ALL ON SCHEMA public` | Full access to the public schema |
| `GRANT CREATE ON SCHEMA public` | **Required for Flyway** - allows creating tables |
| `GRANT USAGE ON SCHEMA public` | Allows accessing schema objects |
| `ALTER DEFAULT PRIVILEGES` | Ensures future objects are accessible |

---

## 🎯 Why This Happens

In PostgreSQL 15+, the `public` schema no longer grants `CREATE` permission to regular users by default for security reasons. You must explicitly grant it.

---

## 🚀 After Fix

Once permissions are granted:
1. Flyway will create `flyway_schema_history` table ✅
2. All migration scripts (V1, V2, V3, V4, V5) will run ✅
3. Application will start successfully ✅

---

## 🔐 Production Recommendation

For production, consider:
1. Using a dedicated schema instead of `public`
2. Minimal required permissions only
3. Separate migration user with elevated privileges
4. Application user with limited runtime permissions

Example:
```sql
-- Create dedicated schema
CREATE SCHEMA erp_schema;

-- Update application.properties:
-- spring.jpa.properties.hibernate.default_schema=erp_schema
-- spring.flyway.schemas=erp_schema
```

---

## Need Help?

If the error persists:
1. Check PostgreSQL version: `psql --version`
2. Verify connection: `psql -U erp_user -d erp_db`
3. Check logs: `tail -f /var/log/postgresql/postgresql-*.log`
4. Verify application.properties password matches database password

---

**Current Password in application.properties**: `Cotton@100`

Make sure this matches your database user password!
