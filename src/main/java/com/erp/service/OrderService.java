package com.erp.service;

import com.erp.dto.OrderDTO;
import com.erp.dto.OrderItemDTO;
import com.erp.dto.OrderMaterialDTO;
import com.erp.entity.Customer;
import com.erp.entity.Order;
import com.erp.entity.OrderItem;
import com.erp.entity.OrderMaterial;
import com.erp.entity.Product;
import com.erp.entity.InventoryItem;
import com.erp.entity.OrderTransaction;
import com.erp.repository.CustomerRepository;
import com.erp.repository.OrderRepository;
import com.erp.repository.OrderMaterialRepository;
import com.erp.repository.ProductRepository;
import com.erp.repository.InventoryItemRepository;
import com.erp.repository.OrderTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMaterialRepository orderMaterialRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final OrderTransactionRepository orderTransactionRepository;
    private final ModelMapper modelMapper;
    
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToDTO(order);
    }
    
    public OrderDTO getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        return convertToDTO(order);
    }
    
    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO createOrder(OrderDTO orderDTO) {

        String orderNumber = normalize(orderDTO.getOrderNumber());
        if (orderNumber == null) {
            orderNumber = generateOrderNumber();
        }
        if (orderRepository.findByOrderNumber(orderNumber).isPresent()) {
            throw new RuntimeException("Order number already exists: " + orderNumber);
        }

        Customer customer = resolveCustomer(orderDTO);
        
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomer(customer);
        order.setCustomerName(normalize(orderDTO.getCustomerName()) != null ? normalize(orderDTO.getCustomerName()) : customer.getCompanyName());
        order.setCustomerContact(normalize(orderDTO.getCustomerContact()));
        order.setCustomerCode(normalize(orderDTO.getCustomerCode()) != null ? normalize(orderDTO.getCustomerCode()) : customer.getCustomerCode());
        order.setOrderDate(orderDTO.getOrderDate() != null ? orderDTO.getOrderDate() : LocalDate.now());
        LocalDate requestedDeliveryDate = orderDTO.getRequestedDeliveryDate() != null
                ? orderDTO.getRequestedDeliveryDate()
                : orderDTO.getRequiredDate();
        order.setRequestedDeliveryDate(requestedDeliveryDate);
        order.setRequiredDate(requestedDeliveryDate);
        order.setDeliveryDate(orderDTO.getDeliveryDate());
        order.setStatus(orderDTO.getStatus() != null ? orderDTO.getStatus() : "PENDING");
        order.setPriority(orderDTO.getPriority() != null ? orderDTO.getPriority() : "STANDARD_NORMAL");
        order.setProductType(orderDTO.getProductType());
        order.setFabricType(orderDTO.getFabricType());
        order.setGsm(orderDTO.getGsm());
        order.setBaseColor(orderDTO.getBaseColor());
        order.setSizeS(orderDTO.getSizeS());
        order.setSizeM(orderDTO.getSizeM());
        order.setSizeL(orderDTO.getSizeL());
        order.setSizeXL(orderDTO.getSizeXL());
        order.setPrintType(orderDTO.getPrintType());
        order.setNumberOfColors(orderDTO.getNumberOfColors());
        order.setPrintPlacement(orderDTO.getPrintPlacement());
        order.setDesignReference(orderDTO.getDesignReference());
        order.setOrderQuantity(orderDTO.getOrderQuantity());
        order.setQuotedPrice(defaultDecimal(orderDTO.getQuotedPrice()));
        order.setEstimatedLaborCost(defaultDecimal(orderDTO.getEstimatedLaborCost()));
        order.setEstimatedMaterialCost(defaultDecimal(orderDTO.getEstimatedMaterialCost()));
        order.setBudgetThresholdPercent(defaultDecimal(orderDTO.getBudgetThresholdPercent(), BigDecimal.valueOf(100)));
        order.setDeliveryProximityAlertEnabled(orderDTO.getDeliveryProximityAlertEnabled() == null ? Boolean.TRUE : orderDTO.getDeliveryProximityAlertEnabled());
        order.setProximityThresholdHours(orderDTO.getProximityThresholdHours() == null ? 48 : orderDTO.getProximityThresholdHours());
        order.setBudgetOverrunAlertEnabled(orderDTO.getBudgetOverrunAlertEnabled() == null ? Boolean.TRUE : orderDTO.getBudgetOverrunAlertEnabled());
        order.setPaymentStatus(orderDTO.getPaymentStatus() != null ? orderDTO.getPaymentStatus() : "UNPAID");
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setSpecialInstructions(orderDTO.getSpecialInstructions());
        order.setDeliveryAddress(orderDTO.getDeliveryAddress());
        order.setNotes(orderDTO.getNotes());
        
        // Process order items
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem orderItem = createOrderItem(itemDTO, order);
                order.getOrderItems().add(orderItem);
            }
        }
        
        // Calculate totals
        calculateOrderTotals(order);
        
        Order savedOrder = orderRepository.save(order);

        // Process and save estimated materials from create/edit form.
        if (orderDTO.getOrderMaterials() != null && !orderDTO.getOrderMaterials().isEmpty()) {
            for (OrderMaterialDTO materialDTO : orderDTO.getOrderMaterials()) {
                OrderMaterial material = new OrderMaterial();
                material.setOrder(savedOrder);
                material.setMaterialName(materialDTO.getMaterialName());
                material.setMaterialCode(materialDTO.getMaterialCode());
                material.setDescription(materialDTO.getDescription());
                material.setQuantity(materialDTO.getQuantity() != null ? materialDTO.getQuantity() : BigDecimal.ZERO);
                material.setUnitOfMeasure(materialDTO.getUnitOfMeasure() != null ? materialDTO.getUnitOfMeasure() : "KG");
                material.setUnitCost(materialDTO.getUnitCost() != null ? materialDTO.getUnitCost() : BigDecimal.ZERO);
                material.setNotes(materialDTO.getNotes());
                material.setStockStatus(materialDTO.getStockStatus() != null ? materialDTO.getStockStatus() : "IN_STOCK");
                material.setMaterialType("ESTIMATED");
                
                if (materialDTO.getInventoryItemId() != null) {
                    InventoryItem inventoryItem = inventoryItemRepository.findById(materialDTO.getInventoryItemId())
                            .orElse(null);
                    material.setInventoryItem(inventoryItem);
                }
                
                orderMaterialRepository.save(material);
            }
        }
        
        return convertToDTO(savedOrder);
    }
    
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (orderDTO.getCustomerId() != null || normalize(orderDTO.getCustomerCode()) != null || normalize(orderDTO.getCustomerName()) != null) {
            Customer customer = resolveCustomer(orderDTO);
            existingOrder.setCustomer(customer);
            existingOrder.setCustomerName(normalize(orderDTO.getCustomerName()) != null ? normalize(orderDTO.getCustomerName()) : customer.getCompanyName());
            existingOrder.setCustomerCode(normalize(orderDTO.getCustomerCode()) != null ? normalize(orderDTO.getCustomerCode()) : customer.getCustomerCode());
        }

        existingOrder.setCustomerContact(normalize(orderDTO.getCustomerContact()));
        LocalDate requestedDeliveryDate = orderDTO.getRequestedDeliveryDate() != null
            ? orderDTO.getRequestedDeliveryDate()
            : orderDTO.getRequiredDate();
        existingOrder.setRequestedDeliveryDate(requestedDeliveryDate);
        existingOrder.setRequiredDate(requestedDeliveryDate);
        existingOrder.setDeliveryDate(orderDTO.getDeliveryDate());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setPriority(orderDTO.getPriority());
        existingOrder.setProductType(orderDTO.getProductType());
        existingOrder.setFabricType(orderDTO.getFabricType());
        existingOrder.setGsm(orderDTO.getGsm());
        existingOrder.setBaseColor(orderDTO.getBaseColor());
        existingOrder.setSizeS(orderDTO.getSizeS());
        existingOrder.setSizeM(orderDTO.getSizeM());
        existingOrder.setSizeL(orderDTO.getSizeL());
        existingOrder.setSizeXL(orderDTO.getSizeXL());
        existingOrder.setPrintType(orderDTO.getPrintType());
        existingOrder.setNumberOfColors(orderDTO.getNumberOfColors());
        existingOrder.setPrintPlacement(orderDTO.getPrintPlacement());
        existingOrder.setDesignReference(orderDTO.getDesignReference());
        existingOrder.setOrderQuantity(orderDTO.getOrderQuantity());
        existingOrder.setQuotedPrice(defaultDecimal(orderDTO.getQuotedPrice()));
        existingOrder.setEstimatedLaborCost(defaultDecimal(orderDTO.getEstimatedLaborCost()));
        existingOrder.setEstimatedMaterialCost(defaultDecimal(orderDTO.getEstimatedMaterialCost()));
        existingOrder.setBudgetThresholdPercent(defaultDecimal(orderDTO.getBudgetThresholdPercent(), BigDecimal.valueOf(100)));
        existingOrder.setDeliveryProximityAlertEnabled(orderDTO.getDeliveryProximityAlertEnabled() == null ? Boolean.TRUE : orderDTO.getDeliveryProximityAlertEnabled());
        existingOrder.setProximityThresholdHours(orderDTO.getProximityThresholdHours() == null ? 48 : orderDTO.getProximityThresholdHours());
        existingOrder.setBudgetOverrunAlertEnabled(orderDTO.getBudgetOverrunAlertEnabled() == null ? Boolean.TRUE : orderDTO.getBudgetOverrunAlertEnabled());
        existingOrder.setPaymentStatus(orderDTO.getPaymentStatus());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setSpecialInstructions(orderDTO.getSpecialInstructions());
        existingOrder.setDeliveryAddress(orderDTO.getDeliveryAddress());
        existingOrder.setNotes(orderDTO.getNotes());
        
        // Update order items if provided
        if (orderDTO.getOrderItems() != null) {
            existingOrder.getOrderItems().clear();
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem orderItem = createOrderItem(itemDTO, existingOrder);
                existingOrder.getOrderItems().add(orderItem);
            }
        }

        calculateOrderTotals(existingOrder);

        // Update only estimated materials from order form, preserve actual production consumption entries.
        // Fix: manipulate the managed collection directly so orphanRemoval handles deletes and no
        // stale first-level-cache entries are re-flushed as inserts (which caused duplication).
        if (orderDTO.getOrderMaterials() != null) {
            // Remove existing ESTIMATED materials from the managed collection;
            // orphanRemoval=true on the @OneToMany will issue the DELETE on flush.
            existingOrder.getOrderMaterials().removeIf(
                m -> "ESTIMATED".equalsIgnoreCase(m.getMaterialType())
            );

            for (OrderMaterialDTO materialDTO : orderDTO.getOrderMaterials()) {
                OrderMaterial material = new OrderMaterial();
                material.setOrder(existingOrder);
                material.setMaterialName(materialDTO.getMaterialName());
                material.setMaterialCode(materialDTO.getMaterialCode());
                material.setDescription(materialDTO.getDescription());
                material.setQuantity(materialDTO.getQuantity() != null ? materialDTO.getQuantity() : BigDecimal.ZERO);
                material.setUnitOfMeasure(materialDTO.getUnitOfMeasure() != null ? materialDTO.getUnitOfMeasure() : "KG");
                material.setUnitCost(materialDTO.getUnitCost() != null ? materialDTO.getUnitCost() : BigDecimal.ZERO);
                material.setNotes(materialDTO.getNotes());
                material.setStockStatus(materialDTO.getStockStatus() != null ? materialDTO.getStockStatus() : "IN_STOCK");
                material.setMaterialType("ESTIMATED");

                if (materialDTO.getInventoryItemId() != null) {
                    InventoryItem inventoryItem = inventoryItemRepository.findById(materialDTO.getInventoryItemId())
                            .orElse(null);
                    material.setInventoryItem(inventoryItem);
                }

                // Add to the collection so Hibernate persists it via cascade instead of calling save() separately.
                existingOrder.getOrderMaterials().add(material);
            }
        }

        Order updatedOrder = orderRepository.save(existingOrder);

        return convertToDTO(updatedOrder);
    }
    
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
    
    public List<OrderDTO> searchOrders(String keyword) {
        return orderRepository.searchOrders(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private OrderItem createOrderItem(OrderItemDTO itemDTO, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        
        if (itemDTO.getProductId() != null) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemDTO.getProductId()));
            orderItem.setProduct(product);
        }
        
        orderItem.setDescription(itemDTO.getDescription());
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setUnitPrice(itemDTO.getUnitPrice());
        orderItem.setDiscountPercent(itemDTO.getDiscountPercent() != null ? itemDTO.getDiscountPercent() : BigDecimal.ZERO);
        orderItem.setTaxPercent(itemDTO.getTaxPercent() != null ? itemDTO.getTaxPercent() : BigDecimal.ZERO);
        
        // Generic specifications (can store any product-specific data as JSON)
        orderItem.setSpecifications(itemDTO.getSpecifications());
        orderItem.setNotes(itemDTO.getNotes());
        
        // Calculate line total
        BigDecimal lineTotal = itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
        BigDecimal discount = lineTotal.multiply(orderItem.getDiscountPercent()).divide(BigDecimal.valueOf(100));
        lineTotal = lineTotal.subtract(discount);
        BigDecimal tax = lineTotal.multiply(orderItem.getTaxPercent()).divide(BigDecimal.valueOf(100));
        lineTotal = lineTotal.add(tax);
        
        orderItem.setLineTotal(lineTotal);
        
        return orderItem;
    }
    
    private void calculateOrderTotals(Order order) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        
        for (OrderItem item : order.getOrderItems()) {
            BigDecimal itemSubtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemSubtotal);
            
            BigDecimal itemDiscount = itemSubtotal.multiply(item.getDiscountPercent()).divide(BigDecimal.valueOf(100));
            totalDiscount = totalDiscount.add(itemDiscount);
            
            BigDecimal itemTax = itemSubtotal.subtract(itemDiscount).multiply(item.getTaxPercent()).divide(BigDecimal.valueOf(100));
            totalTax = totalTax.add(itemTax);
        }

        // Fallback when line items are not sent from create-order form
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            BigDecimal unitPrice = defaultDecimal(order.getQuotedPrice());
            BigDecimal qty = BigDecimal.valueOf(order.getOrderQuantity() == null ? 0 : order.getOrderQuantity());
            subtotal = unitPrice.multiply(qty);
        }
        
        BigDecimal total = subtotal.subtract(totalDiscount).add(totalTax);
        
        order.setSubtotal(subtotal);
        order.setDiscountAmount(totalDiscount);
        order.setTaxAmount(totalTax);
        order.setTotalAmount(total);
    }
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getCompanyName());
            dto.setCustomerCode(order.getCustomer().getCustomerCode());
        }

        dto.setCustomerName(order.getCustomerName() != null ? order.getCustomerName() : dto.getCustomerName());
        dto.setCustomerContact(order.getCustomerContact());
        dto.setRequestedDeliveryDate(order.getRequestedDeliveryDate());
        dto.setRequiredDate(order.getRequiredDate());
        dto.setProductType(order.getProductType());
        dto.setFabricType(order.getFabricType());
        dto.setGsm(order.getGsm());
        dto.setBaseColor(order.getBaseColor());
        dto.setSizeS(order.getSizeS());
        dto.setSizeM(order.getSizeM());
        dto.setSizeL(order.getSizeL());
        dto.setSizeXL(order.getSizeXL());
        dto.setPrintType(order.getPrintType());
        dto.setNumberOfColors(order.getNumberOfColors());
        dto.setPrintPlacement(order.getPrintPlacement());
        dto.setDesignReference(order.getDesignReference());
        dto.setOrderQuantity(order.getOrderQuantity());
        dto.setQuotedPrice(order.getQuotedPrice());
        dto.setEstimatedLaborCost(order.getEstimatedLaborCost());
        dto.setEstimatedMaterialCost(order.getEstimatedMaterialCost());

        dto.setActualLaborCost(order.getActualLaborCost());
        dto.setActualMaterialCost(order.getActualMaterialCost());
        dto.setActualMachineCost(order.getActualMachineCost());
        dto.setTotalActualCost(order.getTotalActualCost());
        dto.setProfitLoss(order.getProfitLoss());
        dto.setMarginPercentage(order.getMarginPercentage());
        dto.setBudgetThresholdPercent(order.getBudgetThresholdPercent());
        dto.setDeliveryProximityAlertEnabled(order.getDeliveryProximityAlertEnabled());
        dto.setProximityThresholdHours(order.getProximityThresholdHours());
        dto.setBudgetOverrunAlertEnabled(order.getBudgetOverrunAlertEnabled());
        dto.setNotes(order.getNotes());
        
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            List<OrderItemDTO> items = order.getOrderItems().stream()
                    .map(item -> {
                        OrderItemDTO itemDTO = modelMapper.map(item, OrderItemDTO.class);
                        if (item.getProduct() != null) {
                            itemDTO.setProductId(item.getProduct().getId());
                            itemDTO.setProductName(item.getProduct().getName());
                        }
                        return itemDTO;
                    })
                    .collect(Collectors.toList());
            dto.setOrderItems(items);
        }
        

        // Return estimated materials in order CRUD DTOs; actual usage is handled in order detail APIs.
        List<OrderMaterial> materials = orderMaterialRepository.findByOrderIdAndMaterialTypeIgnoreCase(order.getId(), "ESTIMATED");
        if (materials != null && !materials.isEmpty()) {
            List<OrderMaterialDTO> materialDTOs = materials.stream()
                    .map(material -> {
                        OrderMaterialDTO materialDTO = modelMapper.map(material, OrderMaterialDTO.class);
                        materialDTO.setOrderId(order.getId());
                        materialDTO.setMaterialType(material.getMaterialType());
                        if (material.getInventoryItem() != null) {
                            materialDTO.setInventoryItemId(material.getInventoryItem().getId());
                        }
                        return materialDTO;
                    })
                    .collect(Collectors.toList());
            dto.setOrderMaterials(materialDTOs);
        }
        
        return dto;
    }

    private Customer resolveCustomer(OrderDTO orderDTO) {
        if (orderDTO.getCustomerId() != null) {
            return customerRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + orderDTO.getCustomerId()));
        }

        String customerCode = normalize(orderDTO.getCustomerCode());
        if (customerCode != null) {
            Optional<Customer> byCode = customerRepository.findByCustomerCode(customerCode);
            if (byCode.isPresent()) {
                return byCode.get();
            }
        }

        String customerName = normalize(orderDTO.getCustomerName());
        if (customerName == null) {
            throw new RuntimeException("Customer details are required. Provide customerId or customerName.");
        }

        List<Customer> matches = customerRepository.searchCustomers(customerName);
        Optional<Customer> exactNameMatch = matches.stream()
                .filter(c -> customerName.equalsIgnoreCase(c.getCompanyName()))
                .findFirst();
        if (exactNameMatch.isPresent()) {
            return exactNameMatch.get();
        }

        Customer newCustomer = new Customer();
        newCustomer.setCustomerCode(customerCode != null ? customerCode : generateCustomerCode());
        newCustomer.setCompanyName(customerName);
        newCustomer.setContactPerson(customerName);
        String contact = normalize(orderDTO.getCustomerContact());
        if (contact != null && contact.contains("@")) {
            newCustomer.setEmail(contact);
        } else {
            newCustomer.setPhone(contact);
        }
        newCustomer.setIsActive(true);
        return customerRepository.save(newCustomer);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal defaultDecimal(BigDecimal value, BigDecimal fallback) {
        return value == null ? fallback : value;
    }

    private String generateOrderNumber() {
        return "ORD-" + LocalDate.now().getYear() + "-" + System.currentTimeMillis();
    }

    private String generateCustomerCode() {
        return "CUST-" + System.currentTimeMillis();
    }

    public OrderDTO changeOrderStatus(Long id, String newStatus, String notes) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        String oldStatus = order.getStatus();
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        // Create transaction record
        OrderTransaction transaction = new OrderTransaction();
        transaction.setOrder(updatedOrder);
        transaction.setTransactionType("STATUS_CHANGE");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setActionDescription(oldStatus + " -> " + newStatus);
        transaction.setUserName("System");
        transaction.setNotes(notes != null ? notes : "");
        orderTransactionRepository.save(transaction);
        
        return convertToDTO(updatedOrder);
    }
}
