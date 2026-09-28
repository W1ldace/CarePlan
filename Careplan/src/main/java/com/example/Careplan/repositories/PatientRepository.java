
package com.example.Careplan.repositories;

import org.springframework.data.jpa.repository.*;
import com.example.Careplan.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Standard CRUD methods like save(), findById(), and findAll() 
    // are automatically inherited from JpaRepository.
}