# Frontend API Reference - Quick Guide

## 🔗 Base URL
```
http://localhost:8080
```

---

## 🔐 Authentication

### Login
```javascript
// POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}

// Response
{
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "userId": 1,
  "username": "admin",
  "roleName": "ADMIN"
}
```

### Using Token
```javascript
// Add to all API requests
headers: {
  'Authorization': 'Bearer YOUR_TOKEN_HERE',
  'Content-Type': 'application/json'
}
```

---

## 📊 Dashboard API

### Get Everything
```javascript
// GET /api/dashboard/summary
// Returns: KPIs + all alerts + recent orders
{
  kpiMetrics: { totalOrders, activeOrders, totalCost, totalRevenue, netProfit, profitMarginPercentage },
  lowInventoryAlerts: [...],
  negativeProfitAlerts: [...],
  delayedOrderAlerts: [...],
  recentOrders: [...],
  lowInventoryCount: 3,
  negativeProfitCount: 2,
  delayedOrdersCount: 5
}
```

### Get KPIs Only
```javascript
// GET /api/dashboard/kpi
{
  totalOrders: 1284,
  activeOrders: 42,
  totalCost: 42902.50,
  totalRevenue: 158400.00,
  netProfit: 115497.50,
  profitMarginPercentage: 72.89,
  revenueGrowth: "+18.3%",
  costGrowth: "+4.2%",
  projectedRevenue: 174240.00
}
```

### Get Alerts
```javascript
// GET /api/dashboard/alerts/low-inventory
[
  {
    itemCode: "INV-001",
    itemName: "A4 Paper",
    currentQuantity: 0,
    reorderPoint: 500,
    status: "Critical"
  }
]

// GET /api/dashboard/alerts/negative-profit
[
  {
    orderNumber: "ORD-2024-087",
    customerName: "ABC Corp",
    profitLoss: -120.40
  }
]

// GET /api/dashboard/alerts/delayed
[
  {
    orderNumber: "ORD-2024-045",
    customerName: "XYZ Company",
    daysLate: 5
  }
]
```

### Get Recent Orders
```javascript
// GET /api/dashboard/recent-orders?days=7
[
  {
    orderNumber: "ORD-2024-1250",
    customerName: "North Avenue Textiles",
    customerInitials: "NA",
    customerTier: "Priority Client",
    productType: "Business Cards",
    profitLoss: 2450.50,
    isProfitable: true,
    orderDate: "2026-04-20"
  }
]
```

---

## 📦 Sample React Code

### Axios Setup
```javascript
// src/api/axios.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### Login Function
```javascript
// src/api/authApi.js
import api from './axios';

export const login = async (username, password) => {
  const response = await api.post('/api/auth/login', {
    username,
    password
  });
  
  // Store token
  localStorage.setItem('token', response.data.token);
  localStorage.setItem('user', JSON.stringify(response.data));
  
  return response.data;
};

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};
```

### Dashboard Function
```javascript
// src/api/dashboardApi.js
import api from './axios';

export const getDashboardSummary = async () => {
  const response = await api.get('/api/dashboard/summary');
  return response.data;
};

export const getKPIMetrics = async () => {
  const response = await api.get('/api/dashboard/kpi');
  return response.data;
};
```

### Login Component Example
```javascript
// src/pages/LoginPage.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/authApi';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(username, password);
      navigate('/dashboard');
    } catch (err) {
      setError('Invalid credentials');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        placeholder="Username"
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
      />
      <button type="submit">Login</button>
      {error && <p>{error}</p>}
    </form>
  );
}
```

### Dashboard Component Example
```javascript
// src/pages/DashboardPage.jsx
import { useState, useEffect } from 'react';
import { getDashboardSummary } from '../api/dashboardApi';

function DashboardPage() {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const data = await getDashboardSummary();
        setDashboard(data);
      } catch (error) {
        console.error('Error fetching dashboard:', error);
      } finally {
        setLoading(false);
      }
    };
    
    fetchDashboard();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1>Dashboard</h1>
      
      {/* KPI Cards */}
      <div className="kpi-cards">
        <div>Total Orders: {dashboard.kpiMetrics.totalOrders}</div>
        <div>Revenue: ${dashboard.kpiMetrics.totalRevenue}</div>
        <div>Profit: ${dashboard.kpiMetrics.netProfit}</div>
      </div>

      {/* Alerts */}
      <div className="alerts">
        <h2>Alerts ({dashboard.lowInventoryCount + dashboard.negativeProfitCount + dashboard.delayedOrdersCount})</h2>
        
        <h3>Low Inventory ({dashboard.lowInventoryCount})</h3>
        {dashboard.lowInventoryAlerts.map(alert => (
          <div key={alert.inventoryItemId}>
            {alert.itemName}: {alert.currentQuantity} units ({alert.status})
          </div>
        ))}
      </div>

      {/* Recent Orders */}
      <div className="recent-orders">
        <h2>Recent Orders</h2>
        <table>
          <tbody>
            {dashboard.recentOrders.map(order => (
              <tr key={order.orderId}>
                <td>{order.orderNumber}</td>
                <td>{order.customerName}</td>
                <td>{order.isProfitable ? '✅' : '❌'}</td>
                <td>${order.profitLoss}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
```

---

## 🚀 Quick Start Commands

```bash
# 1. Create React app
npx create-react-app erp-product-fe
cd erp-product-fe

# 2. Install packages
npm install axios react-router-dom

# 3. Start development
npm start

# Backend should be running on port 8080
# Frontend will run on port 3000
```

---

## ✅ Testing Checklist

1. ✅ Backend running: http://localhost:8080
2. ✅ Swagger accessible: http://localhost:8080/swagger-ui.html
3. ✅ Can login via Swagger (admin/admin123)
4. ✅ Frontend running: http://localhost:3000
5. ✅ Can login via React app
6. ✅ Dashboard loads with data

---

## 🔧 Troubleshooting

### CORS Error?
Backend CORS is configured for `localhost:3000` and `localhost:5173` ✅

### 401 Unauthorized?
Check token is in localStorage and added to headers

### Network Error?
Verify backend is running on port 8080

---

**Need complete starter code? Just ask!**
