# Order Management API - Quick Reference Guide

## Base URL
```
http://localhost:8080/api
```

## Authentication
All endpoints require authentication (to be implemented with JWT).

---

## Order Detail Endpoints

### 1. Get Detailed Order
**Endpoint:** `GET /orders/{id}/detail`

**Description:** Retrieves comprehensive order details including all materials, labor, machines, transactions, and alerts.

**Response Example:**
```json
{
  "id": 1,
  "orderNumber": "ORD-8842",
  "customerName": "Aether Corp International",
  "customerCode": "CUST-001",
  "status": "RUNNING",
  "priority": "STANDARD_NORMAL",
  "warehouseOrigin": "WH-North-04",
  "completionProgress": 84.0,
  "currentStage": "Finishing",
  "quotedPrice": 14500.00,
  "estimatedLaborCost": 1240.00,
  "estimatedMaterialCost": 5662.50,
  "estimatedMachineCost": 1440.00,
  "totalEstimatedCost": 8342.50,
  "actualLaborCost": 1088.50,
  "actualMaterialCost": 5662.50,
  "actualMachineCost": 1440.00,
  "totalActualCost": 8191.00,
  "profitLoss": 6309.00,
  "marginPercentage": 43.51,
  "orderMaterials": [...],
  "orderLabor": [...],
  "orderMachines": [...],
  "orderTransactions": [...],
  "orderAlerts": [...]
}
```

**Status Codes:**
- `200 OK` - Success
- `404 Not Found` - Order not found

---

## Material Management

### 2. Add Material to Order
**Endpoint:** `POST /orders/{orderId}/materials`

**Request Body:**
```json
{
  "inventoryItemId": 123,
  "materialName": "Premium Raw Cotton Fabric",
  "description": "Unbleached cotton fabric",
  "quantity": 1200,
  "unitOfMeasure": "m",
  "unitCost": 4.25,
  "stockStatus": "IN_STOCK",
  "notes": "For tote bag production"
}
```

**Response:** Returns created `OrderMaterialDTO`

**Status Codes:**
- `201 Created` - Material added successfully
- `400 Bad Request` - Validation error
- `404 Not Found` - Order not found

**Notes:**
- `inventoryItemId` is optional (can create custom material without linking to inventory)
- `totalCost` is calculated automatically (quantity * unitCost)
- Adding material updates order's `actualMaterialCost`
- Transaction log entry is created automatically

---

### 3. Remove Material from Order
**Endpoint:** `DELETE /orders/materials/{materialId}`

**Status Codes:**
- `204 No Content` - Material removed successfully
- `404 Not Found` - Material not found

**Notes:**
- Updates order's `actualMaterialCost`
- Cannot be undone (transaction log remains)

---

## Labor Management

### 4. Add Labor to Order
**Endpoint:** `POST /orders/{orderId}/labor`

**Request Body:**
```json
{
  "laborId": 56,
  "operatorCode": "EMP-902",
  "operatorName": "John Smith",
  "shiftRole": "Lead Screen Printer",
  "durationHours": 18.5,
  "hourlyRate": 45.00,
  "shiftDate": "2024-04-22",
  "notes": "Day shift - main production"
}
```

**Response:** Returns created `OrderLaborDTO`

**Status Codes:**
- `201 Created` - Labor added successfully
- `400 Bad Request` - Validation error
- `404 Not Found` - Order not found

**Notes:**
- `laborId` is optional (can create custom labor entry)
- `totalCost` is calculated automatically (durationHours * hourlyRate)
- Adding labor updates order's `actualLaborCost`

---

### 5. Remove Labor from Order
**Endpoint:** `DELETE /orders/labor/{laborId}`

**Status Codes:**
- `204 No Content` - Labor removed successfully
- `404 Not Found` - Labor not found

---

## Machine Management

### 6. Add Machine to Order
**Endpoint:** `POST /orders/{orderId}/machines`

**Request Body:**
```json
{
  "machineId": 8,
  "machineCode": "PRN-HEIDEL-X",
  "machineName": "Heidelberg Multi-Color Press",
  "processDescription": "Multi-Color Silk Screen",
  "uptimeHours": 12.0,
  "hourlyCost": 120.00,
  "startTime": "2024-04-22T08:00:00",
  "endTime": "2024-04-22T20:00:00",
  "notes": "Production run for order ORD-8842"
}
```

**Response:** Returns created `OrderMachineDTO`

**Status Codes:**
- `201 Created` - Machine added successfully
- `400 Bad Request` - Validation error
- `404 Not Found` - Order not found

**Notes:**
- `machineId` is optional
- `totalCost` is calculated automatically (uptimeHours * hourlyCost)
- Adding machine updates order's `actualMachineCost`

---

### 7. Remove Machine from Order
**Endpoint:** `DELETE /orders/machines/{machineId}`

**Status Codes:**
- `204 No Content` - Machine removed successfully
- `404 Not Found` - Machine not found

---

## Transaction History

### 8. Get Order Transactions
**Endpoint:** `GET /orders/{orderId}/transactions`

**Description:** Retrieves all transactions for an order, sorted by date (most recent first).

**Response Example:**
```json
[
  {
    "id": 145,
    "orderId": 1,
    "transactionDate": "2024-04-22T14:30:00",
    "transactionType": "MATERIAL_DEDUCTION",
    "actionDescription": "Material Deduction: Cyanine Blue Dye",
    "quantityOrDuration": "120 kg",
    "userName": "Admin",
    "costImpact": 1488.00,
    "notes": null
  },
  {
    "id": 144,
    "orderId": 1,
    "transactionDate": "2024-04-22T12:15:00",
    "transactionType": "LABOR_ENTRY",
    "actionDescription": "Labor Entry: Sarah Chen - Day Shift",
    "quantityOrDuration": "1 Shift",
    "userName": "Supervisor John",
    "costImpact": 48.00
  }
]
```

**Transaction Types:**
- `MATERIAL_DEDUCTION`
- `LABOR_ENTRY`
- `MACHINE_ALLOCATION`
- `STOCK_ADJUSTMENT`
- `STATUS_CHANGE`

**Status Codes:**
- `200 OK` - Success
- `404 Not Found` - Order not found

---

## Progress Update

### 9. Update Order Progress
**Endpoint:** `PATCH /orders/{orderId}/progress?progress={value}&currentStage={stage}`

**Query Parameters:**
- `progress` (required) - Completion percentage (0-100)
- `currentStage` (required) - Current stage name (e.g., "Printing", "Finishing")

**Example:**
```
PATCH /orders/1/progress?progress=84&currentStage=Finishing
```

**Status Codes:**
- `200 OK` - Progress updated successfully
- `404 Not Found` - Order not found

---

## Field Reference

### Order Status Values
- `PENDING` - Order created, not started
- `RUNNING` - Currently in production
- `COMPLETED` - Production finished
- `ON_HOLD` - Temporarily paused
- `CANCELLED` - Order cancelled
- `READY_TO_SHIP` - Ready for delivery
- `SHIPPED` - Shipped to customer

### Priority Levels
- `CRITICAL_HIGH` - Urgent orders
- `STANDARD_NORMAL` - Normal priority
- `LOW_DEFERRED` - Can be delayed

### Stock Status
- `IN_STOCK` - Available in inventory
- `LOW_STOCK` - Below reorder point
- `OUT_OF_STOCK` - Not available
- `REQUIRED_PO` - Purchase order needed

### Alert Types
- `DELIVERY_PROXIMITY` - Delivery date approaching
- `BUDGET_OVERRUN` - Costs exceeding estimates
- `LOW_INVENTORY` - Materials running low
- `DELAY` - Production delays

### Alert Status
- `ACTIVE` - Alert enabled and monitoring
- `TRIGGERED` - Alert condition met
- `RESOLVED` - Issue resolved
- `DISMISSED` - Alert dismissed by user

---

## Data Flow Examples

### Creating a New Order with Full Details

**Step 1:** Create the order
```
POST /orders
```

**Step 2:** Add materials
```
POST /orders/{orderId}/materials
```

**Step 3:** Add labor assignments
```
POST /orders/{orderId}/labor
```

**Step 4:** Add machine allocations
```
POST /orders/{orderId}/machines
```

**Step 5:** View complete details
```
GET /orders/{orderId}/detail
```

**Result:**
- Order's `actualMaterialCost`, `actualLaborCost`, and `actualMachineCost` are automatically calculated
- `profitLoss` and `marginPercentage` are automatically calculated
- Transaction log entries created for each addition

---

### Monitoring Order Progress

**Step 1:** Get current order detail
```
GET /orders/{orderId}/detail
```

**Step 2:** Check completion progress and current stage
```json
{
  "completionProgress": 84.0,
  "currentStage": "Finishing"
}
```

**Step 3:** Update progress as work proceeds
```
PATCH /orders/{orderId}/progress?progress=95&currentStage=Quality%20Check
```

---

### Reviewing Order Profitability

**Get order detail to see:**
- `quotedPrice` - What customer is paying
- `totalEstimatedCost` - Initial cost estimate
- `totalActualCost` - Real costs incurred
- `profitLoss` - Difference (quotedPrice - totalActualCost)
- `marginPercentage` - (profitLoss / quotedPrice) * 100

**Color coding for UI:**
- Green: profitLoss > 0 (profit)
- Red: profitLoss < 0 (loss)

---

## Error Handling

All endpoints return standard error responses:

```json
{
  "timestamp": "2024-04-22T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed: quantity must be positive",
  "path": "/api/orders/1/materials"
}
```

**Common HTTP Status Codes:**
- `200 OK` - Successful GET/PATCH
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation error
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Frontend Integration Tips

### 1. Order Management Home Page
```javascript
// Get all orders
const response = await fetch('/api/orders');
const orders = await response.json();

// Show in table with:
// - orderNumber
// - customerName  
// - quantity (sum of order items)
// - status (with colored badges)
// - profitLoss (with +/- indicator)
```

### 2. Detailed Order Page
```javascript
// Get full order details
const response = await fetch(`/api/orders/${orderId}/detail`);
const order = await response.json();

// Display sections:
// - Order info (top header)
// - Financial summary (profit/loss calc)
// - Materials table (order.orderMaterials)
// - Labor table (order.orderLabor)
// - Machines table (order.orderMachines)
// - Transaction history (order.orderTransactions)
```

### 3. Create Order Page
```javascript
// Step 1: Create order
const orderResponse = await fetch('/api/orders', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(orderData)
});
const order = await orderResponse.json();

// Step 2: Add materials
for (const material of materials) {
  await fetch(`/api/orders/${order.id}/materials`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(material)
  });
}

// Step 3: Add labor and machines similarly
```

### 4. Real-time Cost Calculation
```javascript
// After adding any material/labor/machine
const updatedOrder = await fetch(`/api/orders/${orderId}/detail`);
const order = await updatedOrder.json();

// Update UI with:
// - order.totalActualCost
// - order.profitLoss
// - order.marginPercentage
```

---

## Testing with Swagger UI

Access the interactive API documentation:
```
http://localhost:8080/swagger-ui.html
```

Navigate to "Order Detail Management" section to:
- View all endpoint documentation
- Try out endpoints with sample data
- See request/response schemas
- Test error scenarios

---

## Support

For issues or questions, contact the backend development team or create a ticket in JIRA with tag `order-management`.
