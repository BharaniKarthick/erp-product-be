package com.erp.service;

import com.erp.dto.*;
import com.erp.entity.*;
import com.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDetailService {
    
    private final OrderRepository orderRepository;
    private final OrderMaterialRepository orderMaterialRepository;
    private final OrderLaborRepository orderLaborRepository;
    private final OrderMachineRepository orderMachineRepository;
    private final OrderTransactionRepository orderTransactionRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final LaborMasterRepository laborMasterRepository;
    private final MachineRepository machineRepository;
    private final ModelMapper modelMapper;

        public OrderDetailDTO getNewOrderDetailTemplate() {
                OrderDetailDTO detailDTO = new OrderDetailDTO();
                detailDTO.setOrderDate(LocalDate.now());
                detailDTO.setRequestedDeliveryDate(LocalDate.now().plusDays(7));
                detailDTO.setStatus("DRAFT");
                detailDTO.setPriority("NORMAL");
                detailDTO.setWarehouseOrigin("MAIN");
                detailDTO.setOrderQuantity(0);
                detailDTO.setProductType("BROCHURE");
                detailDTO.setFabricType("COTTON");
                detailDTO.setPrintType("SCREEN_PRINTING");
                detailDTO.setPrintPlacement("FRONT");
                detailDTO.setBudgetThresholdPercent(BigDecimal.valueOf(100));
                detailDTO.setDeliveryProximityAlertEnabled(true);
                detailDTO.setProximityThresholdHours(48);
                detailDTO.setBudgetOverrunAlertEnabled(true);
                detailDTO.setCompletionProgress(BigDecimal.ZERO);
                detailDTO.setCurrentStage("Order Created");
                detailDTO.setPaymentStatus("UNPAID");
                detailDTO.setEstimatedLaborCost(BigDecimal.ZERO);
                detailDTO.setEstimatedMaterialCost(BigDecimal.ZERO);
                detailDTO.setEstimatedMachineCost(BigDecimal.ZERO);
                detailDTO.setTotalEstimatedCost(BigDecimal.ZERO);
                detailDTO.setActualLaborCost(BigDecimal.ZERO);
                detailDTO.setActualMaterialCost(BigDecimal.ZERO);
                detailDTO.setActualMachineCost(BigDecimal.ZERO);
                detailDTO.setTotalActualCost(BigDecimal.ZERO);
                detailDTO.setQuotedPrice(BigDecimal.ZERO);
                detailDTO.setSubtotal(BigDecimal.ZERO);
                detailDTO.setTaxAmount(BigDecimal.ZERO);
                detailDTO.setDiscountAmount(BigDecimal.ZERO);
                detailDTO.setTotalAmount(BigDecimal.ZERO);
                detailDTO.setPaidAmount(BigDecimal.ZERO);
                detailDTO.setProfitLoss(BigDecimal.ZERO);
                detailDTO.setMarginPercentage(BigDecimal.ZERO);
                return detailDTO;
        }
    
    /**
     * Get detailed order with all related entities
     */
    public OrderDetailDTO getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        OrderDetailDTO detailDTO = new OrderDetailDTO();
        
        // Basic order information
        detailDTO.setId(order.getId());
        detailDTO.setOrderNumber(order.getOrderNumber());
        detailDTO.setCustomerId(order.getCustomer().getId());
        detailDTO.setCustomerName(order.getCustomer().getCompanyName());
        detailDTO.setCustomerCode(order.getCustomer().getCustomerCode());
        detailDTO.setCustomerContact(order.getCustomerContact());
        
        // Dates
        detailDTO.setOrderDate(order.getOrderDate());
        detailDTO.setRequestedDeliveryDate(order.getRequestedDeliveryDate());
        detailDTO.setRequiredDate(order.getRequiredDate());
        detailDTO.setDeliveryDate(order.getDeliveryDate());
        
        // Status and progress
        detailDTO.setStatus(order.getStatus());
        detailDTO.setPriority(order.getPriority());
        detailDTO.setWarehouseOrigin(order.getWarehouseOrigin());
        detailDTO.setCompletionProgress(order.getCompletionProgress());
        detailDTO.setCurrentStage(order.getCurrentStage());
        
        // Financial information
        detailDTO.setQuotedPrice(order.getQuotedPrice());
        detailDTO.setOrderQuantity(order.getOrderQuantity());
        detailDTO.setEstimatedLaborCost(order.getEstimatedLaborCost());
        detailDTO.setEstimatedMaterialCost(order.getEstimatedMaterialCost());
        detailDTO.setEstimatedMachineCost(order.getEstimatedMachineCost());
        detailDTO.setTotalEstimatedCost(order.getTotalEstimatedCost());
        detailDTO.setActualLaborCost(order.getActualLaborCost());
        detailDTO.setActualMaterialCost(order.getActualMaterialCost());
        detailDTO.setActualMachineCost(order.getActualMachineCost());
        detailDTO.setTotalActualCost(order.getTotalActualCost());
        detailDTO.setProfitLoss(order.getProfitLoss());
        detailDTO.setMarginPercentage(order.getMarginPercentage());
        
        // Payment information
        detailDTO.setPaymentStatus(order.getPaymentStatus());
        detailDTO.setPaymentMethod(order.getPaymentMethod());
        detailDTO.setSubtotal(order.getSubtotal());
        detailDTO.setTaxAmount(order.getTaxAmount());
        detailDTO.setDiscountAmount(order.getDiscountAmount());
        detailDTO.setTotalAmount(order.getTotalAmount());
        detailDTO.setPaidAmount(order.getPaidAmount());
        
        // Additional information
        detailDTO.setProductType(order.getProductType());
        detailDTO.setFabricType(order.getFabricType());
        detailDTO.setGsm(order.getGsm());
        detailDTO.setBaseColor(order.getBaseColor());
        detailDTO.setSizeS(order.getSizeS());
        detailDTO.setSizeM(order.getSizeM());
        detailDTO.setSizeL(order.getSizeL());
        detailDTO.setSizeXL(order.getSizeXL());
        detailDTO.setPrintType(order.getPrintType());
        detailDTO.setNumberOfColors(order.getNumberOfColors());
        detailDTO.setPrintPlacement(order.getPrintPlacement());
        detailDTO.setDesignReference(order.getDesignReference());
        detailDTO.setBudgetThresholdPercent(order.getBudgetThresholdPercent());
        detailDTO.setDeliveryProximityAlertEnabled(order.getDeliveryProximityAlertEnabled());
        detailDTO.setProximityThresholdHours(order.getProximityThresholdHours());
        detailDTO.setBudgetOverrunAlertEnabled(order.getBudgetOverrunAlertEnabled());
        detailDTO.setSpecialInstructions(order.getSpecialInstructions());
        detailDTO.setDeliveryAddress(order.getDeliveryAddress());
        detailDTO.setNotes(order.getNotes());
        
        // Related entities
        detailDTO.setOrderMaterials(order.getOrderMaterials().stream()
                .map(om -> modelMapper.map(om, OrderMaterialDTO.class))
                .collect(Collectors.toList()));
        

        detailDTO.setOrderLabor(order.getOrderLabor().stream()
                .map(this::mapLaborToDTO)
                .collect(Collectors.toList()));
        
        detailDTO.setOrderMachines(order.getOrderMachines().stream()
                .map(om -> modelMapper.map(om, OrderMachineDTO.class))
                .collect(Collectors.toList()));
        
        detailDTO.setOrderTransactions(order.getOrderTransactions().stream()
                .map(ot -> modelMapper.map(ot, OrderTransactionDTO.class))
                .collect(Collectors.toList()));
        
        detailDTO.setOrderAlerts(order.getOrderAlerts().stream()
                .map(oa -> modelMapper.map(oa, OrderAlertDTO.class))
                .collect(Collectors.toList()));
        
        // Metadata
        detailDTO.setCreatedBy(order.getCreatedBy() != null ? order.getCreatedBy().getId() : null);
        detailDTO.setCreatedByName(order.getCreatedBy() != null ? order.getCreatedBy().getUsername() : null);
        detailDTO.setCreatedAt(order.getCreatedAt());
        detailDTO.setUpdatedAt(order.getUpdatedAt());
        
        return detailDTO;
    }
    
    /**
     * Add material to order and update actual costs
     */
    public OrderMaterialDTO addMaterialToOrder(OrderMaterialDTO materialDTO) {
        Order order = orderRepository.findById(materialDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        OrderMaterial material = new OrderMaterial();
        material.setOrder(order);
        
        if (materialDTO.getInventoryItemId() != null) {
            InventoryItem inventoryItem = inventoryItemRepository.findById(materialDTO.getInventoryItemId())
                    .orElseThrow(() -> new RuntimeException("Inventory item not found"));
            material.setInventoryItem(inventoryItem);
            material.setMaterialCode(inventoryItem.getItemCode());
            material.setMaterialName(inventoryItem.getName());
            material.setUnitOfMeasure(inventoryItem.getUnitOfMeasure());
        } else {
            material.setMaterialCode(materialDTO.getMaterialCode());
            material.setMaterialName(materialDTO.getMaterialName());
            material.setUnitOfMeasure(materialDTO.getUnitOfMeasure());
        }
        
        material.setDescription(materialDTO.getDescription());
        material.setQuantity(materialDTO.getQuantity());
        material.setUnitCost(materialDTO.getUnitCost());
        material.setStockStatus(materialDTO.getStockStatus());

        material.setMaterialType("ACTUAL");
        material.setNotes(materialDTO.getNotes());
        
        OrderMaterial savedMaterial = orderMaterialRepository.save(material);
        
        // Update order actual material cost
        updateOrderActualCosts(order.getId());
        
        // Log transaction
        logTransaction(order.getId(), "MATERIAL_DEDUCTION", 
                "Material Deduction: " + material.getMaterialName(),
                material.getQuantity() + " " + material.getUnitOfMeasure(),
                material.getTotalCost());
        
        return modelMapper.map(savedMaterial, OrderMaterialDTO.class);
    }
    
    /**
     * Add labor to order and update actual costs.
     * All labor metadata (name, rate, role) is resolved from labor_master via laborId.
     */

        public OrderLaborDTO addLaborToOrder(OrderLaborDTO laborDTO) {
        Order order = orderRepository.findById(laborDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        LaborMaster laborMaster = laborMasterRepository.findById(laborDTO.getLaborId())
                .orElseThrow(() -> new RuntimeException("Labor master not found with id: " + laborDTO.getLaborId()));

        OrderLabor labor = new OrderLabor();
        labor.setOrder(order);
        labor.setLabor(laborMaster);
        labor.setDurationHours(laborDTO.getDurationHours());
        labor.setShiftDate(laborDTO.getShiftDate() != null ? laborDTO.getShiftDate() : java.time.LocalDate.now());
        labor.setNotes(laborDTO.getNotes());
        labor.setTotalCost(resolveLaborTotalCost(laborDTO, laborMaster));

        OrderLabor savedLabor = orderLaborRepository.save(labor);

        // Update order actual labor cost
        updateOrderActualCosts(order.getId());

        // Log transaction
        String employeeName = laborMaster.getFirstName() + " " + laborMaster.getLastName();
        logTransaction(order.getId(), "LABOR_ENTRY",
                "Labor Entry: " + employeeName + " - " + laborMaster.getJobTitle(),
                savedLabor.getDurationHours() + " h",
                savedLabor.getTotalCost());

        return mapLaborToDTO(savedLabor);
    }

    private OrderLaborDTO mapLaborToDTO(OrderLabor labor) {
        OrderLaborDTO dto = new OrderLaborDTO();
        dto.setId(labor.getId());
        dto.setOrderId(labor.getOrder().getId());
        dto.setDurationHours(labor.getDurationHours());
        dto.setShiftDate(labor.getShiftDate());
        dto.setNotes(labor.getNotes());
        dto.setTotalCost(labor.getTotalCost());
        if (labor.getLabor() != null) {
            LaborMaster lm = labor.getLabor();
            dto.setLaborId(lm.getId());
            dto.setEmployeeCode(lm.getEmployeeCode());
            dto.setEmployeeName(lm.getFirstName() + " " + lm.getLastName());
            dto.setJobTitle(lm.getJobTitle());
            dto.setDepartment(lm.getDepartment());
            dto.setShiftType(lm.getShiftType());
            dto.setSkillLevel(lm.getSkillLevel());
        }
        return dto;
    }

        private BigDecimal resolveLaborTotalCost(OrderLaborDTO laborDTO, LaborMaster laborMaster) {
                if (laborDTO.getTotalCost() != null) {
                        return laborDTO.getTotalCost();
                }

                if (laborMaster.getShiftWage() != null) {
                        return laborMaster.getShiftWage();
                }

                return BigDecimal.ZERO;
        }
    
    /**
     * Add machine to order and update actual costs
     */
    public OrderMachineDTO addMachineToOrder(OrderMachineDTO machineDTO) {
        Order order = orderRepository.findById(machineDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        OrderMachine orderMachine = new OrderMachine();
        orderMachine.setOrder(order);
        
        if (machineDTO.getMachineId() != null) {
            Machine machine = machineRepository.findById(machineDTO.getMachineId())
                    .orElseThrow(() -> new RuntimeException("Machine not found"));
            orderMachine.setMachine(machine);
            orderMachine.setMachineCode(machine.getMachineCode());
            orderMachine.setMachineName(machine.getMachineName());
            orderMachine.setHourlyCost(machine.getHourlyCost());
        } else {
            orderMachine.setMachineCode(machineDTO.getMachineCode());
            orderMachine.setMachineName(machineDTO.getMachineName());
            orderMachine.setHourlyCost(machineDTO.getHourlyCost());
        }
        
        orderMachine.setProcessDescription(machineDTO.getProcessDescription());
        orderMachine.setUptimeHours(machineDTO.getUptimeHours());
        orderMachine.setStartTime(machineDTO.getStartTime());
        orderMachine.setEndTime(machineDTO.getEndTime());
        orderMachine.setNotes(machineDTO.getNotes());
        
        OrderMachine savedMachine = orderMachineRepository.save(orderMachine);
        
        // Update order actual machine cost
        updateOrderActualCosts(order.getId());
        
        // Log transaction
        logTransaction(order.getId(), "MACHINE_ALLOCATION",
                "Machine Setup: " + orderMachine.getMachineName(),
                orderMachine.getUptimeHours() + " h",
                orderMachine.getTotalCost());
        
        return modelMapper.map(savedMachine, OrderMachineDTO.class);
    }
    
    /**
     * Update order progress
     */
    public void updateOrderProgress(Long orderId, BigDecimal progress, String currentStage) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setCompletionProgress(progress);
        order.setCurrentStage(currentStage);
        orderRepository.save(order);
    }
    
    /**
     * Update order actual costs based on materials, labor, and machines
     */
    private void updateOrderActualCosts(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.updateActualCosts();
        orderRepository.save(order);
    }
    
    /**
     * Log transaction for audit trail
     */
    private void logTransaction(Long orderId, String type, String description, 
                                 String quantityOrDuration, BigDecimal costImpact) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        OrderTransaction transaction = new OrderTransaction();
        transaction.setOrder(order);
        transaction.setTransactionType(type);
        transaction.setActionDescription(description);
        transaction.setQuantityOrDuration(quantityOrDuration);
        transaction.setCostImpact(costImpact);
        transaction.setUserName("Admin"); // TODO: Get from security context
        
        orderTransactionRepository.save(transaction);
    }
    
    /**
     * Get order transactions (recent 24 hours or all)
     */
    public List<OrderTransactionDTO> getOrderTransactions(Long orderId) {
        List<OrderTransaction> transactions = orderTransactionRepository.findByOrderIdOrderByTransactionDateDesc(orderId);
        return transactions.stream()
                .map(t -> modelMapper.map(t, OrderTransactionDTO.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all order transactions across all orders, sorted by transaction date descending (for dashboard/history view)
     */
    public List<OrderTransactionDTO> getAllOrderTransactions() {
        List<OrderTransaction> transactions = orderTransactionRepository.findAllOrderedByTransactionDateDesc();
        return transactions.stream()
                .map(t -> {
                    OrderTransactionDTO dto = modelMapper.map(t, OrderTransactionDTO.class);
                    // Enrich with order number and customer info for display
                    if (t.getOrder() != null) {
                        dto.setOrderId(t.getOrder().getId());
                        dto.setOrderNumber(t.getOrder().getOrderNumber());
                        dto.setCustomerName(t.getOrder().getCustomer() != null ? t.getOrder().getCustomer().getCompanyName() : "Unknown");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Delete material from order
     */
    public void removeMaterialFromOrder(Long materialId) {
        OrderMaterial material = orderMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));
        
        Long orderId = material.getOrder().getId();
        orderMaterialRepository.delete(material);
        updateOrderActualCosts(orderId);
    }
    
    /**
     * Delete labor from order
     */
    public void removeLaborFromOrder(Long laborId) {
        OrderLabor labor = orderLaborRepository.findById(laborId)
                .orElseThrow(() -> new RuntimeException("Labor not found"));
        
        Long orderId = labor.getOrder().getId();
        orderLaborRepository.delete(labor);
        updateOrderActualCosts(orderId);
    }
    
    /**
     * Delete machine from order
     */
    public void removeMachineFromOrder(Long machineId) {
        OrderMachine machine = orderMachineRepository.findById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found"));
        
        Long orderId = machine.getOrder().getId();
        orderMachineRepository.delete(machine);
        updateOrderActualCosts(orderId);
    }
}
