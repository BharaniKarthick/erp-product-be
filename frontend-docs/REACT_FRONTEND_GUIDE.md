# React Frontend Setup Guide - PrintFlow ERP

## 📋 Backend Context

### Base API URL
```
http://localhost:8080
```

### Authentication
- **Endpoint**: `POST /api/auth/login`
- **Credentials**: `username: "admin"`, `password: "admin123"`
- **Response**: Returns `token` (use as `Bearer <token>` in headers)
- **Token Header**: `Authorization: Bearer <your-token>`

### CORS Enabled For
- `http://localhost:3000` (React default)
- `http://localhost:5173` (Vite default)

---

## 🚀 Quick Start - React Frontend

### Option 1: Create React App (Recommended for Beginners)

```bash
# Navigate to parent directory
cd /Users/b0k03wc/Documents/3-8/ERP

# Create React app
npx create-react-app erp-product-fe

# Navigate to frontend
cd erp-product-fe

# Install required packages
npm install axios react-router-dom @mui/material @mui/icons-material @emotion/react @emotion/styled

# Start development server
npm start
```

### Option 2: Vite (Faster, Modern)

```bash
# Navigate to parent directory
cd /Users/b0k03wc/Documents/3-8/ERP

# Create Vite React app
npm create vite@latest erp-product-fe -- --template react

# Navigate to frontend
cd erp-product-fe

# Install dependencies
npm install

# Install additional packages
npm install axios react-router-dom @mui/material @mui/icons-material @emotion/react @emotion/styled

# Start development server
npm run dev
```

---

## 📁 Recommended Project Structure

```
erp-product-fe/
├── public/
├── src/
│   ├── api/
│   │   ├── axios.js           # Axios instance with interceptors
│   │   ├── authApi.js         # Authentication API calls
│   │   ├── dashboardApi.js    # Dashboard API calls
│   │   ├── ordersApi.js       # Orders API calls
│   │   └── inventoryApi.js    # Inventory API calls
│   ├── components/
│   │   ├── common/
│   │   │   ├── Navbar.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── LoadingSpinner.jsx
│   │   ├── dashboard/
│   │   │   ├── KPICards.jsx
│   │   │   ├── AlertsSection.jsx
│   │   │   └── RecentOrdersTable.jsx
│   │   └── auth/
│   │       └── LoginForm.jsx
│   ├── pages/
│   │   ├── LoginPage.jsx
│   │   ├── DashboardPage.jsx
│   │   ├── OrdersPage.jsx
│   │   └── InventoryPage.jsx
│   ├── context/
│   │   └── AuthContext.jsx    # Authentication state management
│   ├── utils/
│   │   ├── auth.js            # Token management
│   │   └── constants.js       # API URLs, constants
│   ├── App.jsx
│   └── main.jsx (or index.js)
└── package.json
```

---

## 🔑 Backend API Endpoints Reference

### Authentication APIs
```javascript
POST   /api/auth/login                    // Login
POST   /api/auth/logout                   // Logout
GET    /api/auth/validate                 // Validate token
POST   /api/auth/change-password          // Change password
```

### Dashboard APIs
```javascript
GET    /api/dashboard/summary             // Complete dashboard data
GET    /api/dashboard/kpi                 // KPI metrics only
GET    /api/dashboard/alerts/low-inventory      // Low stock alerts
GET    /api/dashboard/alerts/negative-profit    // Negative profit orders
GET    /api/dashboard/alerts/delayed             // Delayed orders
GET    /api/dashboard/recent-orders?days=7      // Recent orders
```

### Orders APIs (if implemented)
```javascript
GET    /api/orders                        // Get all orders
GET    /api/orders/{id}                   // Get order by ID
POST   /api/orders                        // Create order
PUT    /api/orders/{id}                   // Update order
DELETE /api/orders/{id}                   // Delete order
```

### Inventory APIs (if implemented)
```javascript
GET    /api/inventory                     // Get all items
GET    /api/inventory/{id}                // Get item by ID
POST   /api/inventory                     // Create item
PUT    /api/inventory/{id}                // Update item
DELETE /api/inventory/{id}                // Delete item
```

---

## 📊 Data Models (DTOs)

### LoginRequest
```typescript
{
  username: string,
  password: string
}
```

### LoginResponse
```typescript
{
  userId: number,
  username: string,
  email: string,
  firstName: string,
  lastName: string,
  roleName: string,
  token: string,              // Use this for Authorization header
  loginTime: string,
  message: string
}
```

### DashboardSummary
```typescript
{
  kpiMetrics: {
    totalOrders: number,
    activeOrders: number,
    totalCost: number,
    totalRevenue: number,
    netProfit: number,
    profitMarginPercentage: number,
    revenueGrowth: string,    // e.g., "+18.3%"
    costGrowth: string,       // e.g., "+4.2%"
    projectedRevenue: number
  },
  lowInventoryAlerts: [
    {
      inventoryItemId: number,
      itemCode: string,
      itemName: string,
      currentQuantity: number,
      reorderPoint: number,
      unit: string,
      status: string            // "Critical" | "Low"
    }
  ],
  negativeProfitAlerts: [
    {
      orderId: number,
      orderNumber: string,
      customerName: string,
      totalCost: number,
      revenue: number,
      profitLoss: number,      // negative value
      status: string
    }
  ],
  delayedOrderAlerts: [
    {
      orderId: number,
      orderNumber: string,
      customerName: string,
      requiredDate: string,
      currentDate: string,
      daysLate: number,
      status: string
    }
  ],
  recentOrders: [
    {
      orderId: number,
      orderNumber: string,
      customerName: string,
      customerInitials: string,
      customerTier: string,    // "Priority Client", "Bulk Contract", etc.
      productType: string,
      status: string,
      profitLoss: number,
      isProfitable: boolean,
      orderDate: string
    }
  ],
  lowInventoryCount: number,
  negativeProfitCount: number,
  delayedOrdersCount: number
}
```

---

## 🛠️ Starter Code Files

I'll create these files in the next prompt:
1. `axios.js` - Configured Axios instance
2. `authApi.js` - Authentication API functions
3. `dashboardApi.js` - Dashboard API functions
4. `AuthContext.jsx` - Authentication state management
5. `LoginPage.jsx` - Login page component
6. `DashboardPage.jsx` - Dashboard page component
7. `.env` - Environment variables

---

## 🎨 Design System

The frontend follows a comprehensive design system focused on **Precision & Clarity**. See [DESIGN_SYSTEM.md](DESIGN_SYSTEM.md) for complete specifications.

### Quick Overview
- **Colors**: Primary #1275e2 (Blue), Secondary #5f78a3 (Slate), Tertiary #c55b00 (Amber)
- **Typography**: Inter font family (all weights)
- **Spacing**: Level 2 (4px, 8px, 16px, 24px, 32px)
- **Border Radius**: Level 2 (4px, 8px, 12px, 16px)
- **Mode**: Light mode optimized for clarity

### Pre-built Theme Files (copy to your project)
1. **Material-UI**: `templates/theme.js` - Complete MUI theme
2. **CSS Variables**: `templates/design-system.css` - Pure CSS implementation

---

## 🎨 UI Library Recommendations

### Material-UI (MUI) - Recommended
```bash
npm install @mui/material @mui/icons-material @emotion/react @emotion/styled
```
- Modern, professional design
- Rich component library
- Great documentation
- Data tables, cards, charts built-in

### Alternative: Tailwind CSS
```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```
- Utility-first CSS
- Highly customizable
- Smaller bundle size
- More control over design

---

## 🔐 Authentication Flow

```
1. User enters credentials on Login page
   ↓
2. POST /api/auth/login
   ↓
3. Store token in localStorage
   ↓
4. Set token in Axios default headers
   ↓
5. Redirect to Dashboard
   ↓
6. All API calls include Authorization header
   ↓
7. On 401 error → redirect to Login
```

---

## 📦 Required npm Packages

### Essential
```json
{
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.20.0",
    "axios": "^1.6.0"
  }
}
```

### UI Framework (Choose one)
```json
{
  "dependencies": {
    "@mui/material": "^5.15.0",
    "@mui/icons-material": "^5.15.0",
    "@emotion/react": "^11.11.0",
    "@emotion/styled": "^11.11.0"
  }
}
```

### Optional (Recommended)
```json
{
  "dependencies": {
    "recharts": "^2.10.0",           // Charts for dashboard
    "date-fns": "^2.30.0",           // Date formatting
    "react-hot-toast": "^2.4.1"      // Toast notifications
  }
}
```

---

## 🚦 Next Steps

1. **Create React app** (Option 1 or 2 above)
2. **Install dependencies**
3. **I'll provide starter code files** - Just say "create starter files" and I'll generate:
   - API configuration
   - Authentication setup
   - Login page
   - Dashboard page
   - All necessary utilities

4. **Start building**:
   - Login page → Dashboard → Other pages
   - Follow the HTML designs you provided earlier

---

## 🎯 Development Workflow

```bash
# Terminal 1: Backend
cd erp-product-be
mvn spring-boot:run          # Runs on http://localhost:8080

# Terminal 2: Frontend
cd erp-product-fe
npm start                    # Runs on http://localhost:3000
```

Both will run simultaneously!

---

## 📝 Environment Variables (.env)

```env
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_API_TIMEOUT=30000
```

---

**Ready to start?**

1. Choose Create React App or Vite
2. Run the setup commands above
3. Say "create starter files" and I'll generate all the boilerplate code!
