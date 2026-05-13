package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive DTO for detailed order view with all related entities
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    
    private Long id;
    private String orderNumber;
    
    // Customer Information
    private Long customerId;
    private String customerName;
    private String customerCode;
    private String customerContact;
    
    // Order Dates
    private LocalDate orderDate;
    private LocalDate requestedDeliveryDate;
    private LocalDate requiredDate;
    private LocalDate deliveryDate;
    
    // Status and Progress
    private String status;
    private String priority;
    private String warehouseOrigin;
    private BigDecimal completionProgress;
    private String currentStage;
    
    // Financial Information
    private Integer orderQuantity;
    private BigDecimal quotedPrice;
    
    // Estimated Costs
    private BigDecimal estimatedLaborCost;
    private BigDecimal estimatedMaterialCost;
    private BigDecimal estimatedMachineCost;
    private BigDecimal totalEstimatedCost;
    
    // Actual Costs
    private BigDecimal actualLaborCost;
    private BigDecimal actualMaterialCost;
    private BigDecimal actualMachineCost;
    private BigDecimal totalActualCost;
    
    // Profit/Loss
    private BigDecimal profitLoss;
    private BigDecimal marginPercentage;
    
    // Payment Information
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    
    // Additional Information
    private String productType;
    private String fabricType;
    private Integer gsm;
    private String baseColor;
    private Integer sizeS;
    private Integer sizeM;
    private Integer sizeL;
    private Integer sizeXL;
    private String printType;
    private Integer numberOfColors;
    private String printPlacement;
    private String designReference;
    private BigDecimal budgetThresholdPercent;
    private Boolean deliveryProximityAlertEnabled;
    private Integer proximityThresholdHours;
    private Boolean budgetOverrunAlertEnabled;
    private String specialInstructions;
    private String deliveryAddress;
    private String notes;
    
    // Related Entities
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private List<OrderMaterialDTO> orderMaterials = new ArrayList<>();
    private List<OrderLaborDTO> orderLabor = new ArrayList<>();
    private List<OrderMachineDTO> orderMachines = new ArrayList<>();
    private List<OrderTransactionDTO> orderTransactions = new ArrayList<>();
    private List<OrderAlertDTO> orderAlerts = new ArrayList<>();
    
    // Metadata
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
