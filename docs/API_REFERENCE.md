# API Reference Guide

Base URL: `http://localhost:8080`

## Table of Contents
1. [Customer Management](#customer-management)
2. [Product Management](#product-management)
3. [Order Management](#order-management)
4. [Inventory Management](#inventory-management)

---

## Customer Management

### Get All Customers
```http
GET /api/customers
```

**Response:**
```json
[
  {
    "id": 1,
    "customerCode": "CUST001",
    "companyName": "ABC Printing Co.",
    "contactPerson": "John Doe",
    "email": "john@abcprinting.com",
    "phone": "+1234567890",
    "mobile": "+1234567890",
    "addressLine1": "123 Main St",
    "city": "New York",
    "state": "NY",
    "country": "USA",
    "creditLimit": 10000.00,
    "isActive": true
  }
]
```

### Get Customer by ID
```http
GET /api/customers/{id}
```

### Create Customer
```http
POST /api/customers
Content-Type: application/json

{
  "customerCode": "CUST001",
  "companyName": "ABC Printing Co.",
  "contactPerson": "John Doe",
  "email": "john@abcprinting.com",
  "phone": "+1234567890",
  "mobile": "+1234567890",
  "addressLine1": "123 Main St",
  "addressLine2": "Suite 100",
  "city": "New York",
  "state": "NY",
  "postalCode": "10001",
  "country": "USA",
  "taxId": "12-3456789",
  "creditLimit": 10000.00,
  "paymentTerms": "Net 30",
  "isActive": true,
  "notes": "Preferred customer"
}
```

### Update Customer
```http
PUT /api/customers/{id}
Content-Type: application/json

{
  "companyName": "ABC Printing Co. Updated",
  "contactPerson": "Jane Doe",
  "isActive": true
}
```

### Delete Customer
```http
DELETE /api/customers/{id}
```

### Search Customers
```http
GET /api/customers/search?keyword=ABC
```

### Get Active Customers
```http
GET /api/customers/active
```

---

## Product Management

### Get All Products
```http
GET /api/products
```

**Response:**
```json
[
  {
    "id": 1,
    "productCode": "PROD001",
    "name": "Business Cards - Standard",
    "description": "Standard business cards printing",
    "categoryId": 1,
    "categoryName": "Business Cards",
    "unitOfMeasure": "PCS",
    "basePrice": 50.00,
    "costPrice": 30.00,
    "isActive": true
  }
]
```

### Create Product
```http
POST /api/products
Content-Type: application/json

{
  "productCode": "PROD001",
  "name": "Business Cards - Standard",
  "description": "Standard business cards with various finishing options",
  "categoryId": 1,
  "unitOfMeasure": "PCS",
  "basePrice": 50.00,
  "costPrice": 30.00,
  "isActive": true
}
```

### Update Product
```http
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Business Cards - Premium",
  "basePrice": 75.00,
  "costPrice": 45.00
}
```

### Get Product by Code
```http
GET /api/products/code/{productCode}
```

### Search Products
```http
GET /api/products/search?keyword=business
```

---

## Order Management

### Get All Orders
```http
GET /api/orders
```

**Response:**
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-2024-001",
    "customerId": 1,
    "customerName": "ABC Printing Co.",
    "orderDate": "2024-04-22",
    "requiredDate": "2024-04-30",
    "status": "PENDING",
    "priority": "NORMAL",
    "paymentStatus": "UNPAID",
    "subtotal": 500.00,
    "taxAmount": 50.00,
    "discountAmount": 0.00,
    "totalAmount": 550.00,
    "paidAmount": 0.00,
    "orderItems": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Business Cards",
        "quantity": 1000,
        "unitPrice": 50.00,
        "lineTotal": 550.00,
        "paperType": "Premium Matte",
        "paperSize": "3.5x2",
        "colorType": "Full Color",
        "printingSides": "Double",
        "finishing": "Rounded Corners"
      }
    ]
  }
]
```

### Create Order
```http
POST /api/orders
Content-Type: application/json

{
  "orderNumber": "ORD-2024-001",
  "customerId": 1,
  "orderDate": "2024-04-22",
  "requiredDate": "2024-04-30",
  "status": "PENDING",
  "priority": "NORMAL",
  "paymentStatus": "UNPAID",
  "paymentMethod": "Credit Card",
  "specialInstructions": "Handle with care",
  "deliveryAddress": "123 Main St, New York, NY 10001",
  "orderItems": [
    {
      "productId": 1,
      "description": "Business Cards - Premium Matte",
      "quantity": 1000,
      "unitPrice": 50.00,
      "discountPercent": 0,
      "taxPercent": 10,
      "paperType": "Premium Matte",
      "paperSize": "3.5x2 inches",
      "colorType": "Full Color",
      "printingSides": "Double Sided",
      "finishing": "Rounded Corners, Spot UV",
      "artworkFileUrl": "https://example.com/artwork/file.pdf",
      "notes": "Rush order"
    }
  ]
}
```

### Update Order
```http
PUT /api/orders/{id}
Content-Type: application/json

{
  "status": "CONFIRMED",
  "paymentStatus": "PARTIAL",
  "paidAmount": 200.00,
  "deliveryDate": "2024-04-28"
}
```

### Get Orders by Status
```http
GET /api/orders/status/PENDING
GET /api/orders/status/IN_PRODUCTION
GET /api/orders/status/DELIVERED
```

**Status Options:**
- `PENDING`
- `CONFIRMED`
- `IN_PRODUCTION`
- `READY`
- `DELIVERED`
- `CANCELLED`

### Get Orders by Customer
```http
GET /api/orders/customer/{customerId}
```

### Get Order by Number
```http
GET /api/orders/number/ORD-2024-001
```

### Search Orders
```http
GET /api/orders/search?keyword=ORD-2024
```

---

## Inventory Management

### Get All Inventory Items
```http
GET /api/inventory
```

**Response:**
```json
[
  {
    "id": 1,
    "itemCode": "INV001",
    "name": "Premium Matte Paper - A4",
    "description": "High quality matte paper for business cards",
    "categoryId": 1,
    "categoryName": "Paper Stock",
    "unitOfMeasure": "SHEETS",
    "currentQuantity": 5000.000,
    "minimumQuantity": 1000.000,
    "reorderPoint": 1500.000,
    "unitCost": 0.50,
    "location": "Warehouse A - Shelf 1",
    "isActive": true
  }
]
```

### Create Inventory Item
```http
POST /api/inventory
Content-Type: application/json

{
  "itemCode": "INV001",
  "name": "Premium Matte Paper - A4",
  "description": "High quality matte paper for business cards",
  "categoryId": 1,
  "unitOfMeasure": "SHEETS",
  "currentQuantity": 5000.000,
  "minimumQuantity": 1000.000,
  "maximumQuantity": 10000.000,
  "reorderPoint": 1500.000,
  "unitCost": 0.50,
  "location": "Warehouse A - Shelf 1",
  "isActive": true
}
```

### Update Inventory Item
```http
PUT /api/inventory/{id}
Content-Type: application/json

{
  "name": "Premium Matte Paper - A4 (Updated)",
  "unitCost": 0.55,
  "minimumQuantity": 1200.000
}
```

### Adjust Inventory
Manually adjust inventory quantity (for corrections, physical counts, etc.)

```http
POST /api/inventory/{id}/adjust
Content-Type: application/json

{
  "quantity": 5500.000,
  "reason": "Physical stock count adjustment"
}
```

### Add Stock
Add stock to inventory (typically from purchases)

```http
POST /api/inventory/{id}/add-stock
Content-Type: application/json

{
  "quantity": 2000.000,
  "referenceType": "PURCHASE_ORDER",
  "referenceId": 123
}
```

### Reduce Stock
Reduce stock from inventory (typically for production usage)

```http
POST /api/inventory/{id}/reduce-stock
Content-Type: application/json

{
  "quantity": 500.000,
  "referenceType": "ORDER",
  "referenceId": 456
}
```

### Get Low Stock Items
Retrieve items where current quantity ≤ minimum quantity

```http
GET /api/inventory/low-stock
```

### Get Active Inventory Items
```http
GET /api/inventory/active
```

### Get Inventory Item by Code
```http
GET /api/inventory/code/INV001
```

### Search Inventory
```http
GET /api/inventory/search?keyword=paper
```

---

## Common Response Codes

### Success Responses
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `204 No Content` - Resource deleted successfully

### Error Responses
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Error Response Format
```json
{
  "status": 400,
  "message": "Customer code already exists: CUST001",
  "timestamp": "2024-04-22T10:30:00"
}
```

### Validation Error Format
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "customerCode": "Customer code is required",
    "email": "Invalid email format"
  },
  "timestamp": "2024-04-22T10:30:00"
}
```

---

## Pagination (Future Enhancement)

Pagination will be added in future versions:

```http
GET /api/customers?page=0&size=20&sort=companyName,asc
```

---

## Authentication (Future Enhancement)

JWT Bearer token authentication will be implemented:

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400
}
```

Then use token in requests:
```http
GET /api/customers
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Testing with cURL

### Create Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerCode": "CUST001",
    "companyName": "ABC Printing Co.",
    "contactPerson": "John Doe",
    "email": "john@abcprinting.com",
    "phone": "+1234567890",
    "isActive": true
  }'
```

### Create Order
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
        "paperType": "Premium Matte"
      }
    ]
  }'
```

### Get All Orders
```bash
curl http://localhost:8080/api/orders
```

---

## Testing with Postman

1. Import the API into Postman
2. Set base URL: `http://localhost:8080`
3. Create a collection for each module
4. Test all endpoints sequentially

---

## WebSocket Support (Future)

Real-time updates for:
- Order status changes
- Low stock alerts
- Production job updates

```javascript
const socket = new WebSocket('ws://localhost:8080/ws');
socket.onmessage = (event) => {
  console.log('Update:', JSON.parse(event.data));
};
```
