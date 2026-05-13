package com.erp.repository;

import com.erp.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    List<InventoryTransaction> findByInventoryItemId(Long inventoryItemId);
    
    List<InventoryTransaction> findByInventoryItemIdOrderByTransactionDateDesc(Long inventoryItemId);
    
    List<InventoryTransaction> findTop10ByInventoryItemIdOrderByTransactionDateDesc(Long inventoryItemId);
    
    List<InventoryTransaction> findTop50ByOrderByTransactionDateDesc();
    
    List<InventoryTransaction> findByTransactionType(String transactionType);
    
    List<InventoryTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<InventoryTransaction> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);

    @Query("SELECT t FROM InventoryTransaction t JOIN t.inventoryItem i WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.transactionType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(COALESCE(t.referenceNumber, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(COALESCE(t.notes, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:fromDate IS NULL OR t.transactionDate >= :fromDate) " +
           "AND (:toDate IS NULL OR t.transactionDate <= :toDate)")
    Page<InventoryTransaction> searchRecentTransactions(
            @Param("search") String search,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);
}
