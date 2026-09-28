package com.example.Careplan.services;

import com.example.Careplan.models.Medicine;
import com.example.Careplan.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepo;
    private final PatientRepository patientRepo;

    public Medicine saveMedicineWithSchedule(Long patientId, Medicine medicine) {
        medicine.setPatient(patientRepo.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found")));
        
        if (medicine.getSchedules() != null) {
            medicine.getSchedules().forEach(s -> s.setMedicine(medicine));
        }
        return medicineRepo.save(medicine);
    }
}