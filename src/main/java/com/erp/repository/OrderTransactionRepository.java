package com.erp.repository;

import com.erp.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
    
    List<OrderTransaction> findByOrderId(Long orderId);
    
    List<OrderTransaction> findByOrderIdOrderByTransactionDateDesc(Long orderId);
    
    List<OrderTransaction> findByTransactionType(String transactionType);
    
    @Query("SELECT ot FROM OrderTransaction ot WHERE ot.order.id = :orderId AND ot.transactionDate BETWEEN :startDate AND :endDate ORDER BY ot.transactionDate DESC")
    List<OrderTransaction> findByOrderIdAndDateRange(@Param("orderId") Long orderId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ot FROM OrderTransaction ot WHERE ot.order.id = :orderId AND ot.transactionType = :transactionType ORDER BY ot.transactionDate DESC")
    List<OrderTransaction> findByOrderIdAndTransactionType(@Param("orderId") Long orderId, @Param("transactionType") String transactionType);
    
    @Query("SELECT ot FROM OrderTransaction ot ORDER BY ot.transactionDate DESC")
    List<OrderTransaction> findAllOrderedByTransactionDateDesc();
}
