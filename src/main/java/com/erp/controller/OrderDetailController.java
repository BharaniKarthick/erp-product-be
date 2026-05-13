package com.erp.controller;

import com.erp.dto.*;
import com.erp.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Order Detail Management", description = "APIs for detailed order management with materials, labor, and machines")
public class OrderDetailController {
    
    private final OrderDetailService orderDetailService;
    

    @GetMapping("/new/detail")
    @Operation(summary = "Get new order detail template", description = "Get a default payload template for creating a new detailed order")
    public ResponseEntity<OrderDetailDTO> getNewOrderDetailTemplate() {
        return ResponseEntity.ok(orderDetailService.getNewOrderDetailTemplate());
    }

    @GetMapping("/{id:\\d+}/detail")
    @Operation(summary = "Get detailed order", description = "Get comprehensive order details including materials, labor, machines, and transactions")

    public ResponseEntity<OrderDetailDTO> getOrderDetail(@PathVariable("id") Long id) {
        OrderDetailDTO orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }
    
    // ============================================
    // Material Management Endpoints
    // ============================================
    
    @PostMapping("/{orderId}/materials")
    @Operation(summary = "Add material to order", description = "Add a new material item to the order")
    public ResponseEntity<OrderMaterialDTO> addMaterial(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderMaterialDTO materialDTO) {
        materialDTO.setOrderId(orderId);
        OrderMaterialDTO savedMaterial = orderDetailService.addMaterialToOrder(materialDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaterial);
    }
    
    @DeleteMapping("/materials/{materialId}")
    @Operation(summary = "Remove material from order", description = "Delete a material item from the order")
    public ResponseEntity<Void> removeMaterial(@PathVariable("materialId") Long materialId) {
        orderDetailService.removeMaterialFromOrder(materialId);
        return ResponseEntity.noContent().build();
    }
    
    // ============================================
    // Labor Management Endpoints
    // ============================================
    
    @PostMapping("/{orderId}/labor")
    @Operation(summary = "Add labor to order", description = "Add a labor assignment to the order")
    public ResponseEntity<OrderLaborDTO> addLabor(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderLaborDTO laborDTO) {
        laborDTO.setOrderId(orderId);
        OrderLaborDTO savedLabor = orderDetailService.addLaborToOrder(laborDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLabor);
    }
    
    @DeleteMapping("/labor/{laborId}")
    @Operation(summary = "Remove labor from order", description = "Delete a labor assignment from the order")
    public ResponseEntity<Void> removeLabor(@PathVariable("laborId") Long laborId) {
        orderDetailService.removeLaborFromOrder(laborId);
        return ResponseEntity.noContent().build();
    }
    
    // ============================================
    // Machine Management Endpoints
    // ============================================
    
    @PostMapping("/{orderId}/machines")
    @Operation(summary = "Add machine to order", description = "Add a machine allocation to the order")
    public ResponseEntity<OrderMachineDTO> addMachine(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderMachineDTO machineDTO) {
        machineDTO.setOrderId(orderId);
        OrderMachineDTO savedMachine = orderDetailService.addMachineToOrder(machineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMachine);
    }
    
    @DeleteMapping("/machines/{machineId}")
    @Operation(summary = "Remove machine from order", description = "Delete a machine allocation from the order")
    public ResponseEntity<Void> removeMachine(@PathVariable("machineId") Long machineId) {
        orderDetailService.removeMachineFromOrder(machineId);
        return ResponseEntity.noContent().build();
    }
    
    // ============================================
    // Transaction History Endpoints
    // ============================================
    
    @GetMapping("/{orderId:\\d+}/transactions")
    @Operation(summary = "Get order transactions", description = "Get transaction history for the order")
    public ResponseEntity<List<OrderTransactionDTO>> getOrderTransactions(@PathVariable("orderId") Long orderId) {
        List<OrderTransactionDTO> transactions = orderDetailService.getOrderTransactions(orderId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/all")
    @Operation(summary = "Get all order transactions", description = "Get transaction history for all orders sorted by date descending (for dashboard)")
    public ResponseEntity<List<OrderTransactionDTO>> getAllOrderTransactions() {
        List<OrderTransactionDTO> transactions = orderDetailService.getAllOrderTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    // ============================================
    // Progress Update Endpoint
    // ============================================
    
    @PatchMapping("/{orderId:\\d+}/progress")
    @Operation(summary = "Update order progress", description = "Update completion progress and current stage of the order")
    public ResponseEntity<Void> updateProgress(
            @PathVariable("orderId") Long orderId,
            @RequestParam("progress") BigDecimal progress,
            @RequestParam("currentStage") String currentStage) {
        orderDetailService.updateOrderProgress(orderId, progress, currentStage);
        return ResponseEntity.ok().build();
    }
}
