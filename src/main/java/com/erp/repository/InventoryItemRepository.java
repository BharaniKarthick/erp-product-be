package com.erp.repository;

import com.erp.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByItemCode(String itemCode);
    List<InventoryItem> findByIsActive(Boolean isActive);
    List<InventoryItem> findByCategoryId(Long categoryId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.currentQuantity <= i.minimumQuantity")
    List<InventoryItem> findLowStockItems();
    
    @Query("SELECT i FROM InventoryItem i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<InventoryItem> searchInventoryItems(@Param("keyword") String keyword);

        @Query("SELECT i FROM InventoryItem i LEFT JOIN i.category c WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:category IS NULL OR :category = '' OR UPPER(COALESCE(c.name, '')) = UPPER(:category))")
        Page<InventoryItem> searchInventoryItemsPaged(
             @Param("search") String search,
             @Param("category") String category,
             Pageable pageable);
}
