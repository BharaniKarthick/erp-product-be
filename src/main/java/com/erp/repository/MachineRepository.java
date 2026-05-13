package com.erp.repository;

import com.erp.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    
    Optional<Machine> findByMachineCode(String machineCode);
    
    List<Machine> findByStatus(String status);
    
    List<Machine> findByIsActiveTrue();
    
    List<Machine> findByMachineType(String machineType);
    
    List<Machine> findByLocation(String location);
    
    List<Machine> findByStatusAndIsActiveTrue(String status);
    
    boolean existsByMachineCode(String machineCode);
}
