package com.erp.repository;

import com.erp.entity.LaborMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LaborMasterRepository extends JpaRepository<LaborMaster, Long> {
    Optional<LaborMaster> findByEmployeeCode(String employeeCode);
    List<LaborMaster> findByIsActive(Boolean isActive);
    List<LaborMaster> findByDepartment(String department);
    List<LaborMaster> findByApprovalStatus(String approvalStatus);
    List<LaborMaster> findByShiftType(String shiftType);
    
    Long countByIsActive(Boolean isActive);
    Long countByApprovalStatus(String approvalStatus);
    
    @Query("SELECT COUNT(DISTINCT l.jobTitle) FROM LaborMaster l WHERE l.isActive = true")
    Long countActiveRoles();
    
    @Query("SELECT AVG(l.shiftWage) FROM LaborMaster l WHERE l.isActive = true")
    BigDecimal getAverageShiftWage();
    
    @Query("SELECT l FROM LaborMaster l WHERE " +
           "LOWER(l.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<LaborMaster> searchLabor(@Param("keyword") String keyword);
}
