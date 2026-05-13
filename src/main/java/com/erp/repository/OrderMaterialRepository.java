package com.erp.repository;

import com.erp.entity.OrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {
    
    List<OrderMaterial> findByOrderId(Long orderId);

    List<OrderMaterial> findByOrderIdAndMaterialTypeIgnoreCase(Long orderId, String materialType);

    void deleteByOrderIdAndMaterialTypeIgnoreCase(Long orderId, String materialType);
    
    List<OrderMaterial> findByInventoryItemId(Long inventoryItemId);
    
    List<OrderMaterial> findByStockStatus(String stockStatus);
    
    @Query("SELECT SUM(om.totalCost) FROM OrderMaterial om WHERE om.order.id = :orderId")
    BigDecimal sumTotalCostByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT om FROM OrderMaterial om WHERE om.order.id = :orderId AND om.stockStatus = :stockStatus")
    List<OrderMaterial> findByOrderIdAndStockStatus(@Param("orderId") Long orderId, @Param("stockStatus") String stockStatus);
}
