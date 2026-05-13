# Inventory Management System - Implementation Summary

## Overview
Comprehensive inventory management system for materials, chemicals, dyes, solvents, and textiles based on the provided HTML screens for the PrintFlow ERP application.

## Database Schema Changes (V3__enhance_inventory_management.sql)

### 1. New Columns Added to `inventory_items` Table

**Specifications:**
- `weight` - Material weight specification (e.g., "180 GSM")
- `width` - Material width specification (e.g., "150 CM")
- `supplier` - Supplier or manufacturer name

**Alert Configuration:**
- `low_stock_alerts_enabled` - Enable/disable low stock alerts

### 2. New Categories Added

- **Solvents** - Industrial solvents and cleaning agents
- **Auxiliaries** - Auxiliary chemicals and additives
- **Raw Materials** - Raw materials for production
- **Textiles** - Fabric and textile materials

### 3. Sample Inventory Items

**Dye:**
- SKU: DYE-CYA-042 - Cyanine Blue Dye XL (450 kg)

**Solvent:**
- SKU: SLV-IPA-109 - Isopropyl Alcohol 99% (12.5 L) - Low Stock

**Chemical:**
- SKU: CHM-SOH-882 - Sodium Hydroxide Pellets (1,200 kg)

**Textile:**
- SKU: TX-COT-882 - Premium Organic Cotton (1,240 m)
  - Specifications: 180 GSM, 150 CM width
  - Supplier: Ethical Fibers Co.
  - Location: Aisle 4, Shelf B

## Java Implementation

### New DTOs (5)

1. **InventoryItemCreateDTO** - Create new inventory item
   - Item name, type, unit, cost per unit
   - Opening stock
   - Low stock threshold and alerts configuration
   - Optional: description, location, weight, width, supplier

2. **InventoryItemDetailDTO** - Detailed inventory view
   - Basic info (ID, code, name, category)
   - Stock info (current, min, reorder point, unit cost)
   - Stock status (Healthy, Low Stock, Out of Stock)
   - Specifications (weight, width, supplier)
   - Total value calculation
   - Recent transactions list

3. **StockAdjustmentDTO** - Stock adjustment operations
   - Movement type (STOCK_IN, STOCK_OUT)
   - Quantity and reason
   - Effective date and notes
   - Optional: linked order/reference

4. **InventorySummaryDTO** - Dashboard statistics
   - Total solvents on hand
   - Low stock items count
   - Total inventory value
   - Category breakdown
   - Storage capacity used

5. **InventoryTransactionDTO** - Transaction history
   - Transaction type, date, quantity
   - Balance after transaction
   - Linked order/reference
   - Created by information

### Enhanced Entity: InventoryItem

**Added Fields:**
- `weight` - Material weight specification
- `width` - Material width specification
- `supplier` - Supplier name
- `lowStockAlertsEnabled` - Alert toggle

**Helper Methods:**
- `getStockStatus()` - Returns "Healthy", "Low Stock", or "Out of Stock"
- `getTotalValue()` - Calculates currentQuantity × unitCost

### New Service: InventoryManagementService

**Dashboard Methods:**
- `getInventorySummary()` - Calculate dashboard statistics

**Inventory Item Methods:**
- `getAllInventoryItems()` - Get all items with details
- `getInventoryItemsByCategory(categoryName)` - Filter by category
- `getLowStockItems()` - Get items with low stock
- `getInventoryItemDetail(id)` - Get single item with transactions
- `createInventoryItem(createDTO)` - Create new item with auto-generated SKU
- `updateInventoryItem(id, updateDTO)` - Update item details
- `deleteInventoryItem(id)` - Soft delete (deactivate)

**Stock Adjustment Methods:**
- `adjustStock(adjustmentDTO)` - Stock in/out with transaction logging

**Transaction Methods:**
- `getInventoryTransactions(id)` - Get transactions for item
- `getAllRecentTransactions()` - Get recent transactions across all items

**Helper Methods:**
- `generateItemCode(type)` - Auto-generate SKU (e.g., DYE-ABC-123)
- `mapReasonToTransactionType(reason)` - Map adjustment reason to transaction type
- `createTransaction()` - Log inventory transaction

### Updated Repository: InventoryTransactionRepository

**New Query Methods:**
- `findTop10ByInventoryItemIdOrderByTransactionDateDesc()` - Recent 10 transactions for item
- `findTop50ByOrderByTransactionDateDesc()` - Recent 50 transactions overall
- `findByInventoryItemIdOrderByTransactionDateDesc()` - All transactions for item

### New Controller: InventoryManagementController

**Dashboard Endpoints:**
```
GET /api/inventory/summary - Get inventory dashboard statistics
```

**Inventory Items Endpoints:**
```
GET /api/inventory - Get all inventory items
GET /api/inventory/category/{categoryName} - Filter by category
GET /api/inventory/low-stock - Get low stock items
GET /api/inventory/{id} - Get item detail with transactions
POST /api/inventory - Create new inventory item
PUT /api/inventory/{id} - Update inventory item
DELETE /api/inventory/{id} - Delete (deactivate) item
```

**Stock Adjustment Endpoints:**
```
POST /api/inventory/adjust - Adjust stock (in/out)
```

**Transaction Endpoints:**
```
GET /api/inventory/{id}/transactions - Get item transactions
GET /api/inventory/transactions/recent - Get all recent transactions
```

## API Usage Examples

### 1. Get Inventory Summary (Dashboard)

```bash
GET /api/inventory/summary
```

**Response:**
```json
{
  "totalSolventsOnHand": 1240.0,
  "totalSolventsUnit": "L",
  "solventsGrowthPercent": 12.0,
  "lowStockItemsCount": 4,
  "totalInventoryValue": 12840.00,
  "lastUpdateCycle": "24h",
  "totalItems": 124,
  "dyesCount": 45,
  "chemicalsCount": 38,
  "solventsCount": 28,
  "storageCapacityUsedPercent": 72.0
}
```

### 2. Create New Inventory Item

```bash
POST /api/inventory
Content-Type: application/json

{
  "itemName": "Reactive Blue 19",
  "type": "Dye",
  "unit": "kg",
  "costPerUnit": 15.50,
  "openingStock": 500.00,
  "lowStockThreshold": 100.00,
  "lowStockAlertsEnabled": true,
  "description": "Reactive dye for cotton fabrics",
  "location": "Warehouse A, Shelf 3",
  "supplier": "ColorChem Industries"
}
```

**Response:**
```json
{
  "id": 125,
  "itemCode": "DYE-RBL-456",
  "name": "Reactive Blue 19",
  "categoryName": "Dye",
  "currentStock": 500.00,
  "unitOfMeasure": "kg",
  "unitCost": 15.50,
  "stockStatus": "Healthy",
  "totalValue": 7750.00,
  ...
}
```

### 3. Get Inventory Item Detail

```bash
GET /api/inventory/125
```

**Response:**
```json
{
  "id": 125,
  "itemCode": "DYE-RBL-456",
  "name": "Reactive Blue 19",
  "description": "Reactive dye for cotton fabrics",
  "categoryName": "Dye",
  "currentStock": 500.00,
  "unitOfMeasure": "kg",
  "unitCost": 15.50,
  "minimumQuantity": 100.00,
  "stockStatus": "Healthy",
  "location": "Warehouse A, Shelf 3",
  "supplier": "ColorChem Industries",
  "lowStockAlertsEnabled": true,
  "totalValue": 7750.00,
  "recentTransactions": [
    {
      "transactionType": "ADJUSTMENT",
      "quantity": 500.00,
      "transactionDate": "2024-04-22",
      "notes": "Opening Stock",
      "balanceAfter": 500.00
    }
  ]
}
```

### 4. Adjust Stock (Stock In)

```bash
POST /api/inventory/adjust
Content-Type: application/json

{
  "inventoryItemId": 125,
  "movementType": "STOCK_IN",
  "quantity": 200.00,
  "reason": "Purchase Received",
  "effectiveDate": "2024-04-22",
  "notes": "Purchase order PO-9921",
  "linkedOrderId": "PO-9921",
  "referenceType": "PO"
}
```

### 5. Adjust Stock (Stock Out)

```bash
POST /api/inventory/adjust
Content-Type: application/json

{
  "inventoryItemId": 125,
  "movementType": "STOCK_OUT",
  "quantity": 120.00,
  "reason": "Production Usage",
  "effectiveDate": "2024-04-22",
  "notes": "Used for order ORD-8821",
  "linkedOrderId": "ORD-8821",
  "referenceType": "ORDER"
}
```

### 6. Get Inventory Transactions

```bash
GET /api/inventory/125/transactions
```

**Response:**
```json
[
  {
    "id": 456,
    "transactionType": "USAGE",
    "inventoryItemName": "Reactive Blue 19",
    "quantity": -120.00,
    "balanceAfter": 580.00,
    "transactionDate": "2024-04-22",
    "notes": "Used for order ORD-8821",
    "referenceType": "ORDER",
    "referenceNumber": "ORD-8821",
    "createdByName": "Admin"
  },
  {
    "id": 455,
    "transactionType": "PURCHASE",
    "inventoryItemName": "Reactive Blue 19",
    "quantity": 200.00,
    "balanceAfter": 700.00,
    "transactionDate": "2024-04-20",
    "notes": "Purchase order PO-9921",
    "referenceType": "PO",
    "referenceNumber": "PO-9921",
    "createdByName": "Admin"
  }
]
```

### 7. Get Items by Category

```bash
GET /api/inventory/category/Dyes
```

### 8. Get Low Stock Items

```bash
GET /api/inventory/low-stock
```

## Frontend Integration Guide

### 1. Inventory Home Page

**Load Dashboard:**
```javascript
// Get summary statistics
const summaryResponse = await fetch('/api/inventory/summary');
const summary = await summaryResponse.json();

// Display:
// - summary.totalSolventsOnHand L
// - summary.lowStockItemsCount items
// - summary.totalInventoryValue
```

**Load Inventory Table:**
```javascript
// Get all items or filter by category
const itemsResponse = await fetch('/api/inventory');
// OR: await fetch('/api/inventory/category/Dyes');
const items = await itemsResponse.json();

// For each item, display:
// - item.name
// - item.categoryName (with colored badge)
// - item.currentStock (red if low stock)
// - item.unitOfMeasure
// - item.unitCost
// - item.stockStatus (with colored indicator)
```

**Load Recent Transactions:**
```javascript
const transactionsResponse = await fetch('/api/inventory/transactions/recent');
const transactions = await transactionsResponse.json();
```

### 2. Create Inventory Page

```javascript
const formData = {
  itemName: 'Reactive Blue 19',
  type: 'Dye', // or 'Chemical', 'Solvent', 'Auxiliary'
  unit: 'kg',
  costPerUnit: 15.50,
  openingStock: 500.00,
  lowStockThreshold: 100.00,
  lowStockAlertsEnabled: true,
  description: '...',
  location: '...',
  weight: '...',
  width: '...',
  supplier: '...'
};

const response = await fetch('/api/inventory', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(formData)
});

const createdItem = await response.json();
// Redirect to detail page or show success message
```

### 3. Inventory Detail & Adjustment Page

**Load Item Detail:**
```javascript
const itemId = 125; // from URL parameter
const response = await fetch(`/api/inventory/${itemId}`);
const item = await response.json();

// Display:
// - item.itemCode (SKU)
// - item.name
// - item.currentStock
// - item.unitCost
// - item.stockStatus
// - item.weight, item.width, item.supplier
// - item.recentTransactions (table)
```

**Submit Stock Adjustment:**
```javascript
const adjustmentData = {
  inventoryItemId: itemId,
  movementType: 'STOCK_IN', // or 'STOCK_OUT'
  quantity: 200.00,
  reason: 'Purchase Received', // dropdown value
  effectiveDate: '2024-04-22',
  notes: 'Purchase order details...',
  linkedOrderId: 'PO-9921',
  referenceType: 'PO'
};

const response = await fetch('/api/inventory/adjust', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(adjustmentData)
});

const updatedItem = await response.json();
// Refresh item details and transaction history
```

## SKU Generation Logic

**Format:** `PREFIX-XXX-NNN`

**Prefixes:**
- `DYE` - Dyes
- `CHM` - Chemicals
- `SLV` - Solvents
- `AUX` - Auxiliaries
- `RAW` - Raw Materials
- `INV` - Other

**Example:** DYE-CYA-042, SLV-IPA-109, CHM-SOH-882

## Stock Status Logic

- **Healthy:** currentQuantity > minimumQuantity
- **Low Stock:** currentQuantity <= minimumQuantity AND > 0
- **Out of Stock:** currentQuantity = 0

## Transaction Types

- **PURCHASE** - Stock received from purchase orders
- **USAGE** - Stock consumed in production
- **RETURN** - Stock returned from customers
- **ADJUSTMENT** - Manual adjustments (audit, damage, wastage)
- **TRANSFER** - Stock transfers between locations

## Removed Files

The following previously created inventory files have been superseded:
- Old `InventoryController.java` has been replaced by `InventoryManagementController.java`
- Old `InventoryService.java` has been replaced by `InventoryManagementService.java`

## Testing

Use Swagger UI to test all endpoints:
```
http://localhost:8080/swagger-ui.html
```

Navigate to "Inventory Management" section.

## Summary

✅ **Dashboard Statistics** - Real-time inventory metrics
✅ **Multi-Category Support** - Dyes, Chemicals, Solvents, Auxiliaries, Textiles
✅ **Stock Adjustments** - Stock in/out with full audit trail
✅ **Low Stock Alerts** - Configurable per item
✅ **Transaction History** - Complete movement tracking
✅ **Specifications** - Weight, width, supplier for textiles
✅ **Auto-Generated SKUs** - Unique item codes
✅ **Stock Status Tracking** - Healthy, Low Stock, Out of Stock
✅ **Category Filtering** - View items by category
✅ **Total Value Calculation** - Real-time inventory valuation

The backend is fully ready for frontend integration with all three inventory screens!
