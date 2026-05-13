package com.erp.repository;

import com.erp.entity.OrderAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAlertRepository extends JpaRepository<OrderAlert, Long> {
    
    List<OrderAlert> findByOrderId(Long orderId);
    
    List<OrderAlert> findByAlertType(String alertType);
    
    List<OrderAlert> findByAlertStatus(String alertStatus);
    
    List<OrderAlert> findByIsEnabledTrue();
    
    @Query("SELECT oa FROM OrderAlert oa WHERE oa.order.id = :orderId AND oa.isEnabled = true AND oa.alertStatus = 'ACTIVE'")
    List<OrderAlert> findActiveAlertsByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT oa FROM OrderAlert oa WHERE oa.alertType = :alertType AND oa.alertStatus = 'TRIGGERED'")
    List<OrderAlert> findTriggeredAlertsByType(@Param("alertType") String alertType);
}
