package com.erp.repository;

import com.erp.entity.OrderLabor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderLaborRepository extends JpaRepository<OrderLabor, Long> {
    
    List<OrderLabor> findByOrderId(Long orderId);
    
    List<OrderLabor> findByLaborId(Long laborId);
    
    @Query("SELECT SUM(ol.totalCost) FROM OrderLabor ol WHERE ol.order.id = :orderId")
    BigDecimal sumTotalCostByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(ol.durationHours) FROM OrderLabor ol WHERE ol.order.id = :orderId")
    BigDecimal sumDurationHoursByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT ol FROM OrderLabor ol WHERE ol.labor.id = :laborId AND ol.order.status = :status")
    List<OrderLabor> findByLaborIdAndOrderStatus(@Param("laborId") Long laborId, @Param("status") String status);
}
