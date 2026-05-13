package com.erp.repository;

import com.erp.entity.OrderMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderMachineRepository extends JpaRepository<OrderMachine, Long> {
    
    List<OrderMachine> findByOrderId(Long orderId);
    
    List<OrderMachine> findByMachineId(Long machineId);
    
    @Query("SELECT SUM(om.totalCost) FROM OrderMachine om WHERE om.order.id = :orderId")
    BigDecimal sumTotalCostByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(om.uptimeHours) FROM OrderMachine om WHERE om.order.id = :orderId")
    BigDecimal sumUptimeHoursByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT om FROM OrderMachine om WHERE om.machine.id = :machineId AND om.order.status = :status")
    List<OrderMachine> findByMachineIdAndOrderStatus(@Param("machineId") Long machineId, @Param("status") String status);
}
