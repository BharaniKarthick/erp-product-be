package com.erp.dto;

import lombok.Data;

import java.util.List;

@Data
public class InventoryItemsPageDTO {
    private List<InventoryItemDetailDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}