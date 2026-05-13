package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 * DTO for Low Inventory Alert Items
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowInventoryAlertDTO {
    private Long inventoryItemId;
    private String itemCode;
    private String itemName;
    private Integer currentQuantity;
    private Integer reorderPoint;
    private String unit;
    private String status; // "Critical" (0 units), "Low" (<= reorder point)
}
