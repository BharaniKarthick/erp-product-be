# Swagger/OpenAPI Documentation - PrintFlow ERP

## 🚀 Quick Start

### Access Swagger UI
Once the application is running, access the interactive API documentation at:

**Swagger UI**: http://localhost:8080/swagger-ui.html

**OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Starting the Application
```bash
mvn spring-boot:run
```

The application starts on port **8080** by default.

---

## 📖 Using Swagger UI

### 1. **Authentication Setup**

Before testing protected endpoints, you need to authenticate:

1. **Expand** the `Authentication` section
2. **Click** on `POST /api/auth/login`
3. **Click** "Try it out"
4. **Enter** credentials in the request body:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
5. **Click** "Execute"
6. **Copy** the `token` value from the response
7. **Click** the green "Authorize" 🔒 button at the top right
8. **Enter**: `Bearer <your-token>` (replace `<your-token>` with the actual token)
9. **Click** "Authorize"
10. **Click** "Close"

Now all subsequent API calls will include your authentication token!

### 2. **Testing Dashboard APIs**

#### Get Complete Dashboard Summary
```
GET /api/dashboard/summary
```
Returns KPIs, all alerts (low inventory, negative profit, delayed orders), and recent orders.

#### Get Only KPI Metrics
```
GET /api/dashboard/kpi
```
Returns financial metrics: total orders, revenue, costs, profit, margins.

#### Get Low Inventory Alerts
```
GET /api/dashboard/alerts/low-inventory
```
Returns items with stock at or below reorder point.

#### Get Negative Profit Alerts
```
GET /api/dashboard/alerts/negative-profit
```
Returns orders with negative profit (losses).

#### Get Delayed Orders
```
GET /api/dashboard/alerts/delayed
```
Returns orders past their required delivery date.

#### Get Recent Orders
```
GET /api/dashboard/recent-orders?days=7
```
Returns recent orders from last N days (default: 7).

### 3. **Testing Other Modules**

All modules are documented in Swagger:

- **Dashboard**: Real-time metrics and alerts
- **Authentication**: Login, logout, token validation
- **Orders**: Order management (if implemented)
- **Inventory**: Stock management (if implemented)
- **Reports**: Business analytics (if implemented)
- **Settings - Labor**: Employee management (if implemented)

---

## 🔐 Security Scheme

The API uses **Bearer Token** authentication:

```
Authorization: Bearer <your-token>
```

### Token Lifecycle
1. **Obtain Token**: `POST /api/auth/login`
2. **Use Token**: Include in `Authorization` header for all requests
3. **Validate Token**: `GET /api/auth/validate`
4. **Invalidate Token**: `POST /api/auth/logout`

### Default Test Users

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin123` | ADMIN | Full system access |

**Note**: Change default passwords in production!

---

## 📊 Dashboard API Examples

### Example 1: Get Dashboard Summary

**Request:**
```http
GET http://localhost:8080/api/dashboard/summary
Authorization: Bearer 550e8400-e29b-41d4-a716-446655440000
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
    "profitMarginPercentage": 72.89,
    "revenueGrowth": "+18.3%",
    "costGrowth": "+4.2%",
    "projectedRevenue": 174240.00
  },
  "lowInventoryAlerts": [
    {
      "itemCode": "INV-001",
      "itemName": "A4 Paper",
      "currentQuantity": 0,
      "reorderPoint": 500,
      "status": "Critical"
    }
  ],
  "negativeProfitAlerts": [
    {
      "orderNumber": "ORD-2024-087",
      "customerName": "ABC Corp",
      "totalCost": 5220.40,
      "revenue": 5100.00,
      "profitLoss": -120.40
    }
  ],
  "delayedOrderAlerts": [
    {
      "orderNumber": "ORD-2024-045",
      "customerName": "XYZ Industries",
      "daysLate": 5,
      "status": "IN_PRODUCTION"
    }
  ],
  "lowInventoryCount": 3,
  "negativeProfitCount": 2,
  "delayedOrdersCount": 5
}
```

### Example 2: Login and Get Token

**Request:**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Success Response (200 OK):**
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

**Error Response (401 Unauthorized):**
```json
{
  "message": "Invalid username or password"
}
```

---

## 🎯 API Modules Overview

### 1. Dashboard APIs (`/api/dashboard`)
- **Purpose**: Executive overview with real-time metrics
- **Endpoints**: 6
- **Key Features**: KPIs, alerts (inventory, profit, delays), recent activity

### 2. Authentication APIs (`/api/auth`)
- **Purpose**: User authentication and session management
- **Endpoints**: 4
- **Key Features**: Login, logout, token validation, password change

### 3. Order APIs (`/api/orders`)
- **Purpose**: Order management and tracking
- **Key Features**: Create, update, delete orders, cost calculation

### 4. Inventory APIs (`/api/inventory`)
- **Purpose**: Stock management and adjustments
- **Key Features**: Add items, adjust quantities, track usage

### 5. Reports APIs (`/api/reports`)
- **Purpose**: Business analytics and insights
- **Key Features**: Profit breakdown, inventory trends, revenue charts

### 6. Labor Settings APIs (`/api/settings/labor`)
- **Purpose**: Employee management
- **Key Features**: CRUD operations, approval workflow, shift management

---

## 🛠️ Configuration

### Swagger Configuration Location
File: `src/main/java/com/erp/config/OpenApiConfig.java`

### Key Features
- **Bearer Token Authentication**: Configured for JWT/session tokens
- **Multiple Servers**: Local development, testing, production
- **Detailed Descriptions**: Comprehensive API documentation
- **Security Schemes**: Pre-configured authentication

### CORS Configuration
Enabled for:
- `http://localhost:3000` (React/Vue frontend)
- `http://localhost:5173` (Vite dev server)

---

## 📝 API Response Formats

### Success Response
```json
{
  "field1": "value1",
  "field2": "value2"
}
```

### Error Response
```json
{
  "message": "Error description"
}
```

### List Response
```json
[
  { "id": 1, "name": "Item 1" },
  { "id": 2, "name": "Item 2" }
]
```

---

## 🔍 Filtering and Pagination

### Query Parameters
- `days` - Number of days for time-based queries (e.g., recent orders)
- `status` - Filter by status
- `page` - Page number (if pagination implemented)
- `size` - Items per page (if pagination implemented)

### Examples
```
GET /api/dashboard/recent-orders?days=30
GET /api/orders?status=PENDING&page=0&size=20
```

---

## 📋 Common HTTP Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| 200 | OK | Successful GET, PUT, POST |
| 201 | Created | Resource created successfully |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input, validation error |
| 401 | Unauthorized | Invalid credentials, missing token |
| 403 | Forbidden | Valid token but insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 500 | Internal Server Error | Server-side error |

---

## 🧪 Testing with Swagger UI

### Step-by-Step Testing Guide

1. **Start Application**
   ```bash
   mvn spring-boot:run
   ```

2. **Open Swagger UI**
   - Visit: http://localhost:8080/swagger-ui.html

3. **Authenticate**
   - Use login endpoint to get token
   - Click "Authorize" button
   - Enter token with "Bearer " prefix

4. **Test Dashboard**
   - Try `GET /api/dashboard/summary`
   - Check all KPIs and alerts
   - Verify response structure

5. **Test Alerts**
   - Try each alert endpoint individually
   - Verify data accuracy

6. **Test Recent Orders**
   - Try different `days` parameter values
   - Check customer tier classification

---

## 🚨 Troubleshooting

### Swagger UI Not Loading
- **Check**: Application is running on port 8080
- **URL**: Make sure you're using `/swagger-ui.html` (with hyphen)
- **Browser**: Try clearing cache or use incognito mode

### 401 Unauthorized Errors
- **Check**: Token is valid (use `/api/auth/validate`)
- **Format**: Token must have "Bearer " prefix
- **Expiry**: Token may have expired, get a new one

### CORS Errors
- **Check**: Frontend is running on `localhost:3000` or `localhost:5173`
- **Headers**: Make sure `Content-Type: application/json` is set
- **Browser**: Check browser console for CORS error details

### 404 Not Found
- **Check**: Endpoint path is correct
- **Method**: Verify HTTP method (GET, POST, etc.)
- **Server**: Application is running and healthy

---

## 📚 Additional Resources

### OpenAPI Specification
- **OpenAPI 3.0**: https://swagger.io/specification/
- **Springdoc**: https://springdoc.org/

### API Documentation Best Practices
- Use clear, descriptive endpoint names
- Provide example request/response bodies
- Document all error scenarios
- Include authentication requirements

### Frontend Integration
```javascript
// Example: Fetch dashboard data
const token = localStorage.getItem('authToken');

fetch('http://localhost:8080/api/dashboard/summary', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log(data));
```

---

## 🎓 Learning Swagger UI

### Key UI Elements
- **Authorize Button** 🔒: Set authentication token
- **Try it out**: Enable interactive testing
- **Execute**: Send the request
- **Expand/Collapse**: Show/hide endpoint details
- **Schemas**: View data model definitions

### Keyboard Shortcuts
- `Ctrl/Cmd + K`: Focus search
- `Ctrl/Cmd + /`: Toggle documentation
- `Escape`: Close modals

---

## ✅ Checklist

Before deploying to production:

- [ ] Change default admin password
- [ ] Configure production server URL
- [ ] Enable HTTPS for API endpoints
- [ ] Implement JWT token expiration
- [ ] Add rate limiting
- [ ] Enable API versioning
- [ ] Set up monitoring and logging
- [ ] Document all error codes
- [ ] Add API usage examples
- [ ] Configure proper CORS for production domains

---

## 📞 Support

For issues or questions:
- **Email**: support@printflow.com
- **Documentation**: Check inline API descriptions in Swagger UI
- **GitHub**: [Repository URL if applicable]

---

**Happy API Testing! 🎉**
