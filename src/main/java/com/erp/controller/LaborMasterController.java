package com.erp.controller;

import com.erp.dto.LaborMasterDTO;
import com.erp.dto.LaborMasterSummaryDTO;
import com.erp.service.LaborMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 * REST Controller for Labor Master Settings
 */
@RestController
@RequestMapping("/api/settings/labor")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Labor Master Settings", description = "Workforce management and personnel directory API")
public class LaborMasterController {
    
    private final LaborMasterService laborMasterService;
    
    /**
     * Get Dashboard Summary
     * 
     * GET /api/settings/labor/summary
     */
    @GetMapping("/summary")
    @Operation(
        summary = "Get Labor Dashboard Summary",
        description = "Retrieve KPI statistics: total workforce, average wage, active roles, pending approvals"
    )
    public ResponseEntity<LaborMasterSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(laborMasterService.getDashboardSummary());
    }
    
    /**
     * Get All Labor Records
     * 
     * GET /api/settings/labor
     */
    @GetMapping
    @Operation(
        summary = "Get All Labor Records",
        description = "Retrieve complete personnel directory including inactive employees"
    )
    public ResponseEntity<List<LaborMasterDTO>> getAllLabor() {
        return ResponseEntity.ok(laborMasterService.getAllLabor());
    }
    
    /**
     * Get Active Labor Records Only
     * 
     * GET /api/settings/labor/active
     */
    @GetMapping("/active")
    @Operation(
        summary = "Get Active Employees",
        description = "Retrieve only active employees from the workforce"
    )
    public ResponseEntity<List<LaborMasterDTO>> getActiveLabor() {
        return ResponseEntity.ok(laborMasterService.getActiveLabor());
    }
    
    /**
     * Get Labor by Department
     * 
     * GET /api/settings/labor/department/{department}
     */
    @GetMapping("/department/{department}")
    @Operation(
        summary = "Get Labor by Department",
        description = "Filter employees by department (e.g., Offset Printing, Pre-Press, Binding & Finish)"
    )
    public ResponseEntity<List<LaborMasterDTO>> getLaborByDepartment(
        @Parameter(description = "Department name", required = true)
        @PathVariable String department
    ) {
        return ResponseEntity.ok(laborMasterService.getLaborByDepartment(department));
    }
    
    /**
     * Get Labor by Approval Status
     * 
     * GET /api/settings/labor/approval-status/{status}
     */
    @GetMapping("/approval-status/{status}")
    @Operation(
        summary = "Get Labor by Approval Status",
        description = "Filter employees by approval status: DRAFT, PENDING_APPROVAL, APPROVED, REJECTED"
    )
    public ResponseEntity<List<LaborMasterDTO>> getLaborByApprovalStatus(
        @Parameter(description = "Approval status", required = true)
        @PathVariable String status
    ) {
        return ResponseEntity.ok(laborMasterService.getLaborByApprovalStatus(status));
    }
    
    /**
     * Get Labor by Shift Type
     * 
     * GET /api/settings/labor/shift/{shiftType}
     */
    @GetMapping("/shift/{shiftType}")
    @Operation(
        summary = "Get Labor by Shift Type",
        description = "Filter employees by shift preference: DAY, NIGHT, ROTATING"
    )
    public ResponseEntity<List<LaborMasterDTO>> getLaborByShiftType(
        @Parameter(description = "Shift type", required = true)
        @PathVariable String shiftType
    ) {
        return ResponseEntity.ok(laborMasterService.getLaborByShiftType(shiftType));
    }
    
    /**
     * Get Labor by ID
     * 
     * GET /api/settings/labor/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get Labor by ID",
        description = "Retrieve single employee record by ID"
    )
    public ResponseEntity<LaborMasterDTO> getLaborById(
        @Parameter(description = "Labor ID", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(laborMasterService.getLaborById(id));
    }
    
    /**
     * Get Labor by Employee Code
     * 
     * GET /api/settings/labor/code/{employeeCode}
     */
    @GetMapping("/code/{employeeCode}")
    @Operation(
        summary = "Get Labor by Employee Code",
        description = "Retrieve employee by unique employee code (e.g., EMP-2041)"
    )
    public ResponseEntity<LaborMasterDTO> getLaborByEmployeeCode(
        @Parameter(description = "Employee code", required = true)
        @PathVariable String employeeCode
    ) {
        return ResponseEntity.ok(laborMasterService.getLaborByEmployeeCode(employeeCode));
    }
    
    /**
     * Search Labor Records
     * 
     * GET /api/settings/labor/search?keyword=sarah
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search Labor Records",
        description = "Search employees by keyword (name, employee code)"
    )
    public ResponseEntity<List<LaborMasterDTO>> searchLabor(
        @Parameter(description = "Search keyword")
        @RequestParam String keyword
    ) {
        return ResponseEntity.ok(laborMasterService.searchLabor(keyword));
    }
    
    /**
     * Create New Labor Record
     * 
     * POST /api/settings/labor
     */
    @PostMapping
    @Operation(
        summary = "Create New Employee",
        description = "Register new personnel in the system. Employee code must be unique."
    )
    public ResponseEntity<LaborMasterDTO> createLabor(
        @Valid @RequestBody LaborMasterDTO laborDTO
    ) {
        LaborMasterDTO created = laborMasterService.createLabor(laborDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    /**
     * Update Labor Record
     * 
     * PUT /api/settings/labor/{id}
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update Employee Record",
        description = "Update existing employee information"
    )
    public ResponseEntity<LaborMasterDTO> updateLabor(
        @Parameter(description = "Labor ID", required = true)
        @PathVariable Long id,
        
        @Valid @RequestBody LaborMasterDTO laborDTO
    ) {
        return ResponseEntity.ok(laborMasterService.updateLabor(id, laborDTO));
    }
    
    /**
     * Delete Labor Record (Soft Delete)
     * 
     * DELETE /api/settings/labor/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete Employee",
        description = "Soft delete employee (sets is_active to false)"
    )
    public ResponseEntity<Void> deleteLabor(
        @Parameter(description = "Labor ID", required = true)
        @PathVariable Long id
    ) {
        laborMasterService.deleteLabor(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Approve Labor Record
     * 
     * POST /api/settings/labor/{id}/approve
     */
    @PostMapping("/{id}/approve")
    @Operation(
        summary = "Approve Employee Registration",
        description = "Approve pending employee registration"
    )
    public ResponseEntity<LaborMasterDTO> approveLabor(
        @Parameter(description = "Labor ID", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(laborMasterService.approveLabor(id));
    }
    
    /**
     * Reject Labor Record
     * 
     * POST /api/settings/labor/{id}/reject
     */
    @PostMapping("/{id}/reject")
    @Operation(
        summary = "Reject Employee Registration",
        description = "Reject pending employee registration"
    )
    public ResponseEntity<LaborMasterDTO> rejectLabor(
        @Parameter(description = "Labor ID", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(laborMasterService.rejectLabor(id));
    }
}
