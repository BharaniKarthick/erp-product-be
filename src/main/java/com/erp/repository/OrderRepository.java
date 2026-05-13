package com.erp.repository;

import com.erp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(String status);
    List<Order> findByPaymentStatus(String paymentStatus);
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByStatusAndDateRange(
        @Param("status") String status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT o FROM Order o WHERE " +
           "LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.customer.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Order> searchOrders(@Param("keyword") String keyword);
}
