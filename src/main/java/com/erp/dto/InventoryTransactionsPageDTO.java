package com.erp.dto;

import lombok.Data;

import java.util.List;

@Data
public class InventoryTransactionsPageDTO {
    private List<InventoryTransactionDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}