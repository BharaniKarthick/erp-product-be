# 🚀 Swagger Quick Reference Card

## Access Points

| Resource | URL |
|----------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |
| **Application** | http://localhost:8080 |

---

## ⚡ 30-Second Quick Start

### 1. Start Application
```bash
mvn spring-boot:run
```

### 2. Open Swagger
```
http://localhost:8080/swagger-ui.html
```

### 3. Authenticate
1. Click `POST /api/auth/login`
2. **Try it out**
3. Use credentials:
   ```json
   {"username": "admin", "password": "admin123"}
   ```
4. **Execute**
5. Copy the `token` value
6. Click 🔒 **Authorize** (top right)
7. Enter: `Bearer <paste-token-here>`
8. **Authorize** → **Close**

### 4. Test Any API
All requests are now authenticated! ✅

---

## 📋 Default Credentials

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN |

⚠️ **Change in production!**

---

## 🎯 Key Endpoints

### Dashboard (No Parameters Needed)
```
GET /api/dashboard/summary
```
Returns everything: KPIs, alerts, recent orders

### Get KPIs Only
```
GET /api/dashboard/kpi
```

### Recent Orders
```
GET /api/dashboard/recent-orders?days=7
```

### Low Inventory Alerts
```
GET /api/dashboard/alerts/low-inventory
```

### Negative Profit Orders
```
GET /api/dashboard/alerts/negative-profit
```

### Delayed Orders
```
GET /api/dashboard/alerts/delayed
```

---

## 🔐 Authentication Flow

```
1. POST /api/auth/login
   ↓
2. Get token in response
   ↓
3. Click Authorize 🔒
   ↓
4. Enter: Bearer <token>
   ↓
5. Test protected endpoints ✅
```

---

## 📊 Expected Dashboard Response Structure

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
      "profitLoss": -120.40
    }
  ],
  "delayedOrderAlerts": [
    {
      "orderNumber": "ORD-2024-045",
      "daysLate": 5
    }
  ],
  "lowInventoryCount": 3,
  "negativeProfitCount": 2,
  "delayedOrdersCount": 5
}
```

---

## 🧪 Testing Checklist

- [ ] Application running on port 8080
- [ ] Swagger UI loads successfully
- [ ] Login returns token
- [ ] Authorize button accepts token
- [ ] Dashboard summary returns data
- [ ] All alerts display correctly
- [ ] Recent orders show customer data

---

## 🐛 Troubleshooting

### Swagger UI Not Loading
```bash
# Check if app is running
curl http://localhost:8080/actuator/health

# If not running, start it:
mvn spring-boot:run
```

### 401 Unauthorized
- **Fix**: Get new token from `/api/auth/login`
- **Format**: Must include `Bearer ` prefix
- **Example**: `Bearer 550e8400-e29b-41d4-a716-446655440000`

### CORS Error
- **Frontend must be on**: `localhost:3000` or `localhost:5173`
- **Check**: Browser console for details

---

## 📞 More Help

See full documentation: **SWAGGER_DOCUMENTATION.md**

---

**PrintFlow ERP - API v1.0.0**
