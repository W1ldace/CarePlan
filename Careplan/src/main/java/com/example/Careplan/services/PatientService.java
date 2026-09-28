package com.example.Careplan.services;

import com.example.Careplan.models.Patient;
import com.example.Careplan.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepo;

    public Patient savePatient(Patient patient) {
        return patientRepo.save(patient);
    }

    public Patient getPatient(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
    }

    public java.util.List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }
}