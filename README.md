# ERP System - Backend API

A comprehensive ERP system for printing businesses built with Spring Boot, PostgreSQL, and React.

## Features

### 📊 Dashboard & Analytics
- **Real-time KPI Metrics**: Total orders, revenue, costs, profit margins
- **Alert System**: Low inventory, negative profit orders, delayed deliveries
- **Recent Activity**: Last 7-30 days order tracking
- **Growth Indicators**: Revenue and cost growth percentages
- **Customer Tier Classification**: Automatic tier assignment based on order history

### 🔐 Authentication & Security
- **User Authentication**: BCrypt password encryption
- **Session Management**: Token-based authentication (JWT-ready)
- **Role-Based Access**: ADMIN, MANAGER, OPERATOR, VIEWER roles
- **Station Tracking**: Workstation and IP logging
- **Password Management**: Secure password change functionality

### Order Management
- Create and manage customer orders
- Track order status (Pending, In Production, Ready, Delivered)
- Order items with printing specifications (paper type, size, color, finishing)
- Payment tracking and status management

### Inventory Management
- Track inventory items (paper, ink, materials)
- Real-time stock levels
- Low stock alerts
- Inventory adjustments and transactions
- Stock movements tracking (Purchase, Usage, Adjustment)

### Customer Management
- Customer information and contacts
- Credit limit management
- Payment terms tracking
- Customer history

### Product Catalog
- Product categories and hierarchies
- Product specifications for printing services
- Pricing management

### Production Management
- Labor master (employee tracking)
- Production jobs and assignments
- Work hour tracking

### Reporting & Analytics
- Order reports
- Inventory reports
- Production analytics

## Technology Stack

- **Backend Framework**: Spring Boot 3.2.4
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Database Migration**: Flyway
- **Security**: Spring Security (JWT-ready)
- **API Documentation**: Swagger/OpenAPI
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Database Setup

1. Install PostgreSQL and create a database:

```sql
CREATE DATABASE erp_db;
CREATE USER erp_user WITH PASSWORD 'erp_password';
GRANT ALL PRIVILEGES ON DATABASE erp_db TO erp_user;
```

2. Update database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/erp_db
spring.datasource.username=erp_user
spring.datasource.password=erp_password
```

## Installation & Setup

1. **Clone the repository**

```bash
cd erp-product-be
```

2. **Build the project**

```bash
./mvnw clean install
```

3. **Run the application**

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Database Migration

The application uses Flyway for database migrations. The schema will be automatically created on first run.

Migration files are located in: `src/main/resources/db/migration/`

## API Documentation with Swagger

Once the application is running, access the **interactive API documentation**:

### Swagger UI (Interactive)
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification (JSON)
```
http://localhost:8080/v3/api-docs
```

### 🔐 Quick Start with Swagger

1. **Open Swagger UI**: http://localhost:8080/swagger-ui.html
2. **Authenticate**:
   - Expand `Authentication` section
   - Click `POST /api/auth/login`
   - Try it out with credentials: `{"username":"admin", "password":"admin123"}`
   - Copy the `token` from response
   - Click green **Authorize** 🔒 button (top right)
   - Enter: `Bearer <your-token>`
3. **Test APIs**: All endpoints are now authenticated!

**📖 Detailed Guide**: See [SWAGGER_DOCUMENTATION.md](SWAGGER_DOCUMENTATION.md) for complete Swagger usage guide.

## API Endpoints

### 🔑 Authentication APIs
- `POST /api/auth/login` - User login (returns session token)
- `POST /api/auth/logout` - Logout and invalidate token
- `GET /api/auth/validate` - Check token validity
- `POST /api/auth/change-password` - Update user password

**Default Credentials**: username=`admin`, password=`admin123`

### 📊 Dashboard APIs
- `GET /api/dashboard/summary` - Complete dashboard data (KPIs + alerts + recent orders)
- `GET /api/dashboard/kpi` - KPI metrics only
- `GET /api/dashboard/alerts/low-inventory` - Items with critical/low stock
- `GET /api/dashboard/alerts/negative-profit` - Orders with profit loss
- `GET /api/dashboard/alerts/delayed` - Overdue orders
- `GET /api/dashboard/recent-orders?days=7` - Recent orders (default: 7 days)

### Customer Management
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer
- `GET /api/customers/search?keyword=` - Search customers

### Product Management
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/search?keyword=` - Search products

### Order Management
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/number/{orderNumber}` - Get order by number
- `GET /api/orders/status/{status}` - Get orders by status
- `GET /api/orders/customer/{customerId}` - Get orders by customer
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Delete order
- `GET /api/orders/search?keyword=` - Search orders

### Inventory Management
- `GET /api/inventory` - Get all inventory items
- `GET /api/inventory/{id}` - Get inventory item by ID
- `GET /api/inventory/low-stock` - Get low stock items
- `POST /api/inventory` - Create new inventory item
- `PUT /api/inventory/{id}` - Update inventory item
- `DELETE /api/inventory/{id}` - Delete inventory item
- `POST /api/inventory/{id}/adjust` - Adjust inventory quantity
- `POST /api/inventory/{id}/add-stock` - Add stock
- `POST /api/inventory/{id}/reduce-stock` - Reduce stock
- `GET /api/inventory/search?keyword=` - Search inventory

## Example API Requests

### 🔐 Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "userId": 1,
  "username": "admin",
  "email": "admin@printflow.com",
  "firstName": "System",
  "lastName": "Administrator",
  "roleName": "ADMIN",
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "loginTime": "2026-04-22T21:40:00",
  "message": "Login successful"
}
```

### 📊 Get Dashboard Summary
```bash
GET /api/dashboard/summary
Authorization: Bearer <your-token>
```

**Response:**
```json
{
  "kpiMetrics": {
    "totalOrders": 1284,
    "activeOrders": 42,
    "totalCost": 42902.50,
    "totalRevenue": 158400.00,
    "netProfit": 115497.50,
    "profitMarginPercentage": 72.89
  },
  "lowInventoryAlerts": [...],
  "negativeProfitAlerts": [...],
  "delayedOrderAlerts": [...],
  "lowInventoryCount": 3,
  "negativeProfitCount": 2,
  "delayedOrdersCount": 5
}
```

### Create Customer
```json
POST /api/customers
{
  "customerCode": "CUST001",
  "companyName": "ABC Printing Co.",
  "contactPerson": "John Doe",
  "email": "john@abcprinting.com",
  "phone": "+1234567890",
  "addressLine1": "123 Main St",
  "city": "New York",
  "state": "NY",
  "country": "USA",
  "isActive": true
}
```

### Create Order
```json
POST /api/orders
{
  "orderNumber": "ORD-2024-001",
  "customerId": 1,
  "orderDate": "2024-04-22",
  "status": "PENDING",
  "priority": "NORMAL",
  "paymentStatus": "UNPAID",
  "orderItems": [
    {
      "productId": 1,
      "description": "Business Cards",
      "quantity": 1000,
      "unitPrice": 50.00,
      "paperType": "Premium Matte",
      "paperSize": "3.5x2",
      "colorType": "Full Color",
      "printingSides": "Double",
      "finishing": "Rounded Corners"
    }
  ]
}
```

### Adjust Inventory
```json
POST /api/inventory/1/adjust
{
  "quantity": 500.00,
  "reason": "Stock take adjustment"
}
```

## Default Data

The application comes with:
- **Default Admin User**
  - Username: `admin`
  - Password: `admin123`
  - Email: `admin@erp.com`

- **Default Roles**: ADMIN, MANAGER, SALES, PRODUCTION, WAREHOUSE

- **Sample Categories**: Business Cards, Brochures, Banners, Custom Printing

## Project Structure

```
src/
├── main/
│   ├── java/com/erp/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST API controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA entities
│   │   ├── exception/       # Exception handling
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic
│   │   └── ErpApplication.java
│   └── resources/
│       ├── db/migration/    # Flyway migration scripts
│       └── application.properties
└── test/                    # Test cases
```

## Development

### Running Tests
```bash
./mvnw test
```

### Build without tests
```bash
./mvnw clean package -DskipTests
```

### Hot Reload
The application includes Spring Boot DevTools for automatic restart during development.

## Security

- Spring Security is configured with basic authentication
- JWT token support is prepared (can be enabled)
- CORS is configured for React frontend (localhost:3000, localhost:5173)
- Passwords are encrypted using BCrypt

## Next Steps

1. **Frontend Development**: Build React frontend using the API
2. **Authentication**: Implement JWT authentication properly
3. **Reports**: Add report generation (PDF/Excel)
4. **Notifications**: Email notifications for low stock, order status
5. **File Upload**: Implement artwork file upload for orders
6. **Dashboard**: Analytics and dashboard APIs
7. **Audit Logging**: Track all system changes

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials in application.properties
- Ensure database `erp_db` exists

### Port Already in Use
- Change port in application.properties: `server.port=8081`

### Migration Errors
- Drop and recreate database for clean start
- Check Flyway migration scripts for syntax errors

## Support

For issues and questions, please contact the development team.

## License

Copyright © 2026 ERP Solutions
