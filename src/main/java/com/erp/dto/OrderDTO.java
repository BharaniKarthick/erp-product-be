package com.erp.dto;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;

    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerContact;
    private String customerCode;

    private LocalDate orderDate;
    private LocalDate requestedDeliveryDate;
    private LocalDate requiredDate;
    private LocalDate deliveryDate;

    private String status;
    private String priority;

    // Product fields
    private String productType;
    private String fabricType;
    private Integer gsm;
    private String baseColor;
    private Integer sizeS;
    private Integer sizeM;
    private Integer sizeL;
    private Integer sizeXL;

    // Printing fields
    private String printType;
    private Integer numberOfColors;
    private String printPlacement;
    private String designReference;

    // Cost and quantity fields
    private Integer orderQuantity;
    private BigDecimal quotedPrice;
    private BigDecimal estimatedLaborCost;
    private BigDecimal estimatedMaterialCost;
    private BigDecimal estimatedMachineCost;

    private BigDecimal actualLaborCost;
    private BigDecimal actualMaterialCost;
    private BigDecimal actualMachineCost;
    private BigDecimal totalActualCost;
    private BigDecimal profitLoss;
    private BigDecimal marginPercentage;

    // Alert configuration fields
    private BigDecimal budgetThresholdPercent;
    private Boolean deliveryProximityAlertEnabled;
    private Integer proximityThresholdHours;
    private Boolean budgetOverrunAlertEnabled;

    private String paymentStatus;
    private String paymentMethod;

    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;

    private String specialInstructions;
    private String notes;
    private String deliveryAddress;

    @Valid
    private List<OrderItemDTO> orderItems = new ArrayList<>();

    @Valid
    private List<OrderMaterialDTO> orderMaterials = new ArrayList<>();
}
