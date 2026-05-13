# Quick Start Guide - ERP Backend

This guide will help you get the ERP system up and running in minutes.

## Prerequisites Check

```bash
# Check Java version (need 17+)
java -version

# Check Maven version
mvn -version

# Check PostgreSQL
psql --version
```

## Step 1: Database Setup (5 minutes)

### Option A: Using psql command line
```bash
# Login to PostgreSQL
psql -U postgres

# Run the setup script
\i scripts/setup-database.sql

# Or manually:
CREATE DATABASE erp_db;
CREATE USER erp_user WITH PASSWORD 'erp_password';
GRANT ALL PRIVILEGES ON DATABASE erp_db TO erp_user;
\q
```

### Option B: Using pgAdmin
1. Open pgAdmin
2. Create new database: `erp_db`
3. Create new user: `erp_user` with password `erp_password`
4. Grant all privileges on `erp_db` to `erp_user`

## Step 2: Configure Application (2 minutes)

Edit `src/main/resources/application.properties` if needed:

```properties
# Change these if your PostgreSQL setup is different
spring.datasource.url=jdbc:postgresql://localhost:5432/erp_db
spring.datasource.username=erp_user
spring.datasource.password=erp_password

# Change port if 8080 is already in use
server.port=8080
```

## Step 3: Build & Run (3 minutes)

```bash
# Clean and build
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

Or if you're on Windows:
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

## Step 4: Verify Installation (2 minutes)

### Check application is running
```bash
curl http://localhost:8080/api-docs
```

### Access Swagger UI
Open browser: http://localhost:8080/swagger-ui.html

### Test an endpoint
```bash
# Get all customers (should return empty array initially)
curl http://localhost:8080/api/customers
```

## Step 5: Create Test Data (5 minutes)

### Create a customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerCode": "CUST001",
    "companyName": "Test Company",
    "contactPerson": "John Doe",
    "email": "john@testcompany.com",
    "phone": "+1234567890",
    "city": "New York",
    "state": "NY",
    "country": "USA",
    "isActive": true
  }'
```

### Create a product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "PROD001",
    "name": "Business Cards",
    "description": "Standard business cards",
    "unitOfMeasure": "PCS",
    "basePrice": 50.00,
    "isActive": true
  }'
```

### Create an inventory item
```bash
curl -X POST http://localhost:8080/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "itemCode": "INV001",
    "name": "Paper - Premium Matte",
    "unitOfMeasure": "SHEETS",
    "currentQuantity": 5000,
    "minimumQuantity": 1000,
    "reorderPoint": 1500,
    "unitCost": 0.50,
    "isActive": true
  }'
```

### Create an order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "orderNumber": "ORD-2024-001",
    "customerId": 1,
    "orderDate": "2024-04-22",
    "status": "PENDING",
    "orderItems": [
      {
        "productId": 1,
        "quantity": 1000,
        "unitPrice": 50.00,
        "paperType": "Premium Matte",
        "paperSize": "3.5x2",
        "colorType": "Full Color"
      }
    ]
  }'
```

## Step 6: Explore the API (ongoing)

### Using Swagger UI (Recommended)
1. Go to: http://localhost:8080/swagger-ui.html
2. Explore all endpoints
3. Try out different operations
4. See request/response examples

### Using Postman
1. Import the OpenAPI spec from: http://localhost:8080/api-docs
2. Create collections for each module
3. Set base URL: `http://localhost:8080`

### Using cURL (for automation)
See [API_REFERENCE.md](docs/API_REFERENCE.md) for all endpoints

## Common Issues & Solutions

### Issue: "Port 8080 already in use"
**Solution:** Change port in application.properties
```properties
server.port=8081
```

### Issue: "Could not connect to database"
**Solution:** 
1. Check PostgreSQL is running: `pg_ctl status`
2. Verify credentials in application.properties
3. Check database exists: `psql -U postgres -c "\l"`

### Issue: "Flyway migration failed"
**Solution:**
```bash
# Drop and recreate database
psql -U postgres -c "DROP DATABASE IF EXISTS erp_db;"
psql -U postgres -c "CREATE DATABASE erp_db;"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE erp_db TO erp_user;"

# Restart application
./mvnw spring-boot:run
```

### Issue: "Maven build fails"
**Solution:**
```bash
# Clean Maven cache
./mvnw clean

# Update dependencies
./mvnw dependency:resolve

# Try again
./mvnw clean install
```

## Default Credentials

### Database
- **Database**: erp_db
- **Username**: erp_user
- **Password**: erp_password

### Application (for future authentication)
- **Username**: admin
- **Password**: admin123
- **Email**: admin@erp.com

## Next Steps

1. **Read the documentation**:
   - [README.md](README.md) - Full documentation
   - [DATABASE_SCHEMA.md](docs/DATABASE_SCHEMA.md) - Database structure
   - [API_REFERENCE.md](docs/API_REFERENCE.md) - API endpoints

2. **Customize the system**:
   - Add your company's products
   - Configure categories
   - Set up inventory items
   - Create customer records

3. **Develop the frontend**:
   - Use React/Vue/Angular
   - Connect to API at http://localhost:8080/api
   - CORS is already configured for localhost:3000 and localhost:5173

4. **Enable security (recommended)**:
   - Implement JWT authentication
   - Add role-based access control
   - Secure sensitive endpoints

## Useful Commands

### Development
```bash
# Run with live reload
./mvnw spring-boot:run

# Run tests
./mvnw test

# Skip tests
./mvnw clean install -DskipTests

# Package as JAR
./mvnw clean package
```

### Production
```bash
# Build production JAR
./mvnw clean package -DskipTests

# Run the JAR
java -jar target/erp-product-be-1.0.0.jar

# Run with custom config
java -jar target/erp-product-be-1.0.0.jar --spring.config.location=config/application.properties
```

### Database
```bash
# Backup database
pg_dump -U erp_user erp_db > backup.sql

# Restore database
psql -U erp_user erp_db < backup.sql

# Connect to database
psql -U erp_user -d erp_db
```

## Getting Help

### Check logs
```bash
# Application logs are in the console
# Or configure file logging in application.properties:
logging.file.name=logs/app.log
```

### Debug mode
```bash
# Run with debug logging
./mvnw spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.erp=DEBUG"
```

### API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Success! 🎉

You now have a fully functional ERP backend system running!

Try creating some test data and explore the Swagger UI to see all available operations.
