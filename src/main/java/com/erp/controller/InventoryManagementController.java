package com.erp.controller;

import com.erp.dto.*;
import com.erp.service.InventoryManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Inventory Management", description = "APIs for inventory management - materials, chemicals, and dyes")
public class InventoryManagementController {
    
    private final InventoryManagementService inventoryManagementService;
    
    // ============================================
    // Dashboard / Summary Endpoints
    // ============================================
    
    @GetMapping("/summary")
    @Operation(summary = "Get inventory summary", description = "Get dashboard statistics for inventory home page")
    public ResponseEntity<InventorySummaryDTO> getInventorySummary() {
        InventorySummaryDTO summary = inventoryManagementService.getInventorySummary();
        return ResponseEntity.ok(summary);
    }
    
    // ============================================
    // Inventory Items Endpoints
    // ============================================
    
    @GetMapping
    @Operation(summary = "Get all inventory items", description = "Get all inventory items with detailed information")
    public ResponseEntity<List<InventoryItemDetailDTO>> getAllInventoryItems() {
        List<InventoryItemDetailDTO> items = inventoryManagementService.getAllInventoryItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/paged")
    @Operation(summary = "Get paginated inventory items", description = "Get inventory items with pagination, search, and category filtering")
    public ResponseEntity<InventoryItemsPageDTO> getInventoryItemsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {
        InventoryItemsPageDTO response = inventoryManagementService.getInventoryItemsPage(page, size, search, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get inventory categories", description = "Get all inventory categories")
    public ResponseEntity<List<InventoryCategoryDTO>> getInventoryCategories() {
        List<InventoryCategoryDTO> categories = inventoryManagementService.getInventoryCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/category/{categoryName}")
    @Operation(summary = "Get inventory by category", description = "Filter inventory items by category (Dyes, Chemicals, Solvents, etc.)")
    public ResponseEntity<List<InventoryItemDetailDTO>> getInventoryByCategory(@PathVariable String categoryName) {
        List<InventoryItemDetailDTO> items = inventoryManagementService.getInventoryItemsByCategory(categoryName);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock items", description = "Get all items with low stock levels")
    public ResponseEntity<List<InventoryItemDetailDTO>> getLowStockItems() {
        List<InventoryItemDetailDTO> items = inventoryManagementService.getLowStockItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get inventory item detail", description = "Get detailed information for a specific inventory item including recent transactions")
    public ResponseEntity<InventoryItemDetailDTO> getInventoryItemDetail(@PathVariable Long id) {
        InventoryItemDetailDTO item = inventoryManagementService.getInventoryItemDetail(id);
        return ResponseEntity.ok(item);
    }
    
    @PostMapping
    @Operation(summary = "Create new inventory item", description = "Add a new material, chemical, or dye to inventory")
    public ResponseEntity<InventoryItemDetailDTO> createInventoryItem(@Valid @RequestBody InventoryItemCreateDTO createDTO) {
        InventoryItemDetailDTO createdItem = inventoryManagementService.createInventoryItem(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update inventory item", description = "Update an existing inventory item")
    public ResponseEntity<InventoryItemDetailDTO> updateInventoryItem(
            @PathVariable Long id,
            @Valid @RequestBody InventoryItemCreateDTO updateDTO) {
        InventoryItemDetailDTO updatedItem = inventoryManagementService.updateInventoryItem(id, updateDTO);
        return ResponseEntity.ok(updatedItem);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory item", description = "Soft delete (deactivate) an inventory item")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryManagementService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
    
    // ============================================
    // Stock Adjustment Endpoints
    // ============================================
    
    @PostMapping("/adjust")
    @Operation(summary = "Adjust stock", description = "Perform stock in/out adjustment for an inventory item")
    public ResponseEntity<InventoryItemDetailDTO> adjustStock(@Valid @RequestBody StockAdjustmentDTO adjustmentDTO) {
        InventoryItemDetailDTO updatedItem = inventoryManagementService.adjustStock(adjustmentDTO);
        return ResponseEntity.ok(updatedItem);
    }
    
    // ============================================
    // Transaction History Endpoints
    // ============================================
    
    @GetMapping("/{id}/transactions")
    @Operation(summary = "Get item transactions", description = "Get transaction history for a specific inventory item")
    public ResponseEntity<List<InventoryTransactionDTO>> getInventoryTransactions(@PathVariable Long id) {
        List<InventoryTransactionDTO> transactions = inventoryManagementService.getInventoryTransactions(id);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/recent")
    @Operation(summary = "Get recent transactions", description = "Get all recent inventory transactions across all items")
    public ResponseEntity<List<InventoryTransactionDTO>> getAllRecentTransactions() {
        List<InventoryTransactionDTO> transactions = inventoryManagementService.getAllRecentTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/recent/paged")
    @Operation(summary = "Get paginated recent transactions", description = "Get recent inventory transactions with pagination, search, and date filters")
    public ResponseEntity<InventoryTransactionsPageDTO> getRecentTransactionsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {
        InventoryTransactionsPageDTO response = inventoryManagementService
                .getRecentTransactionsPage(page, size, search, fromDate, toDate);
        return ResponseEntity.ok(response);
    }
}
