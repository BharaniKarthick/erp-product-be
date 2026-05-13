package com.erp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}