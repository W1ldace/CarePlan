package com.example.Careplan.controllers;

import com.example.Careplan.models.Medicine;
import com.example.Careplan.services.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MedicineController {
    
    private final MedicineService medicineService;

    @PostMapping("/patients/{patientId}/medicines")
    public Medicine addMedicine(@PathVariable Long patientId, @Valid @RequestBody Medicine medicine) {
        return medicineService.saveMedicineWithSchedule(patientId, medicine);
    }
}