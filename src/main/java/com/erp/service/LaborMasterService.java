package com.erp.service;

import com.erp.dto.LaborMasterDTO;
import com.erp.dto.LaborMasterSummaryDTO;
import com.erp.entity.LaborMaster;
import com.erp.repository.LaborMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**

 * Service for Labor Master Management
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LaborMasterService {
    
    private final LaborMasterRepository laborMasterRepository;
    
    /**
     * Get dashboard summary statistics
     */
    public LaborMasterSummaryDTO getDashboardSummary() {
        Long totalWorkforce = laborMasterRepository.count();
        Long activeEmployees = laborMasterRepository.countByIsActive(true);
        BigDecimal averageShiftWage = laborMasterRepository.getAverageShiftWage();
        Long activeRoles = laborMasterRepository.countActiveRoles();
        Long pendingApproval = laborMasterRepository.countByApprovalStatus("PENDING_APPROVAL");
        
        // Mock growth percentage (could be calculated from historical data)
        String growthPercentage = "+4%";
        
        return new LaborMasterSummaryDTO(
            totalWorkforce,
            activeEmployees,
            averageShiftWage != null ? averageShiftWage : BigDecimal.ZERO,
            activeRoles,
            pendingApproval,
            growthPercentage
        );
    }
    
    /**
     * Get all labor records
     */
    @Transactional(readOnly = true)
    public List<LaborMasterDTO> getAllLabor() {
        return laborMasterRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get active labor records only
     */
    @Transactional(readOnly = true)
    public List<LaborMasterDTO> getActiveLabor() {
        return laborMasterRepository.findByIsActive(true).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get labor by department
     */
    @Transactional(readOnly = true)
    public List<LaborMasterDTO> getLaborByDepartment(String department) {
        return laborMasterRepository.findByDepartment(department).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get labor by approval status
     */
    @Transactional(readOnly = true)
    public List<LaborMasterDTO> getLaborByApprovalStatus(String approvalStatus) {
        return laborMasterRepository.findByApprovalStatus(approvalStatus).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get labor by shift type
     */
    @Transactional(readOnly = true)
    public List<LaborMasterDTO> getLaborByShiftType(String shiftType) {
        return laborMasterRepository.findByShiftType(shiftType).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /** Get labor by ID
     */
    @Transactional(readOnly = true)
    public LaborMasterDTO getLaborById(Long id) {
        LaborMaster labor = laborMasterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Labor not found with id: " + id));
        return convertToDTO(labor);
    }
    
    /**
     * Get labor by employee code
     */
    @Transactional(readOnly = true)
    public LaborMasterDTO getLaborByEmployeeCode(String employeeCode) {
        LaborMaster labor = laborMasterRepository.findByEmployeeCode(employeeCode)
            .orElseThrow(() -> new RuntimeException("Labor not found with employee code: " + employeeCode));
        return convertToDTO(labor);
    }
    
    /**
     * Search labor by keyword
     */
    @Transactional(readOnly = true)
    public List<LaborMasterDTO> searchLabor(String keyword) {
        return laborMasterRepository.searchLabor(keyword).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Create new labor record
     */
    public LaborMasterDTO createLabor(LaborMasterDTO laborDTO) {
        // Check if employee code already exists
        if (laborMasterRepository.findByEmployeeCode(laborDTO.getEmployeeCode()).isPresent()) {
            throw new RuntimeException("Employee code already exists: " + laborDTO.getEmployeeCode());
        }
        
        LaborMaster labor = convertToEntity(laborDTO);
        
        // Set default values if not provided
        if (labor.getIsActive() == null) {
            labor.setIsActive(true);
        }
        if (labor.getApprovalStatus() == null) {
            labor.setApprovalStatus("DRAFT");
        }
        if (labor.getShiftType() == null) {
            labor.setShiftType("DAY");
        }
        
        LaborMaster savedLabor = laborMasterRepository.save(labor);
        return convertToDTO(savedLabor);
    }
    
    /**
     * Update existing labor record
     */
    public LaborMasterDTO updateLabor(Long id, LaborMasterDTO laborDTO) {
        LaborMaster existingLabor = laborMasterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Labor not found with id: " + id));
        
        // Update fields
        existingLabor.setFirstName(laborDTO.getFirstName());
        existingLabor.setLastName(laborDTO.getLastName());
        existingLabor.setEmail(laborDTO.getEmail());
        existingLabor.setPhone(laborDTO.getPhone());
        existingLabor.setJobTitle(laborDTO.getJobTitle());
        existingLabor.setDepartment(laborDTO.getDepartment());
        existingLabor.setHourlyRate(laborDTO.getHourlyRate());
        existingLabor.setDailyRate(laborDTO.getDailyRate());
        existingLabor.setShiftWage(laborDTO.getShiftWage());
        existingLabor.setShiftType(laborDTO.getShiftType());
        existingLabor.setPhotoUrl(laborDTO.getPhotoUrl());
        existingLabor.setSkillLevel(laborDTO.getSkillLevel());
        existingLabor.setApprovalStatus(laborDTO.getApprovalStatus());
        existingLabor.setIsActive(laborDTO.getIsActive());
        existingLabor.setHireDate(laborDTO.getHireDate());
        
        LaborMaster updatedLabor = laborMasterRepository.save(existingLabor);
        return convertToDTO(updatedLabor);
    }
    
    /**
     * Delete labor record (soft delete)
     */
    public void deleteLabor(Long id) {
        LaborMaster labor = laborMasterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Labor not found with id: " + id));
        labor.setIsActive(false);
        laborMasterRepository.save(labor);
    }
    
    /**
     * Approve labor record
     */
    public LaborMasterDTO approveLabor(Long id) {
        LaborMaster labor = laborMasterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Labor not found with id: " + id));
        labor.setApprovalStatus("APPROVED");
        laborMasterRepository.save(labor);
        return convertToDTO(labor);
    }
    
    /**
     * Reject labor record
     */
    public LaborMasterDTO rejectLabor(Long id) {
        LaborMaster labor = laborMasterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Labor not found with id: " + id));
        labor.setApprovalStatus("REJECTED");
        laborMasterRepository.save(labor);
        return convertToDTO(labor);
    }
    
    // ========== Helper Methods ==========
    
    private LaborMasterDTO convertToDTO(LaborMaster labor) {
        LaborMasterDTO dto = new LaborMasterDTO();
        dto.setId(labor.getId());
        dto.setEmployeeCode(labor.getEmployeeCode());
        dto.setFirstName(labor.getFirstName());
        dto.setLastName(labor.getLastName());
        dto.setEmail(labor.getEmail());
        dto.setPhone(labor.getPhone());
        dto.setJobTitle(labor.getJobTitle());
        dto.setDepartment(labor.getDepartment());
        dto.setHourlyRate(labor.getHourlyRate());
        dto.setDailyRate(labor.getDailyRate());
        dto.setShiftWage(labor.getShiftWage());
        dto.setShiftType(labor.getShiftType());
        dto.setPhotoUrl(labor.getPhotoUrl());
        dto.setSkillLevel(labor.getSkillLevel());
        dto.setApprovalStatus(labor.getApprovalStatus());
        dto.setIsActive(labor.getIsActive());
        dto.setHireDate(labor.getHireDate());
        return dto;
    }
    
    private LaborMaster convertToEntity(LaborMasterDTO dto) {
        LaborMaster labor = new LaborMaster();
        labor.setId(dto.getId());
        labor.setEmployeeCode(dto.getEmployeeCode());
        labor.setFirstName(dto.getFirstName());
        labor.setLastName(dto.getLastName());
        labor.setEmail(dto.getEmail());
        labor.setPhone(dto.getPhone());
        labor.setJobTitle(dto.getJobTitle());
        labor.setDepartment(dto.getDepartment());
        labor.setHourlyRate(dto.getHourlyRate());
        labor.setDailyRate(dto.getDailyRate());
        labor.setShiftWage(dto.getShiftWage());
        labor.setShiftType(dto.getShiftType());
        labor.setPhotoUrl(dto.getPhotoUrl());
        labor.setSkillLevel(dto.getSkillLevel());
        labor.setApprovalStatus(dto.getApprovalStatus());
        labor.setIsActive(dto.getIsActive());
        labor.setHireDate(dto.getHireDate());
        return labor;
    }
}
