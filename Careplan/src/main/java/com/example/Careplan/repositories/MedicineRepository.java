package com.example.Careplan.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.example.Careplan.models.Medicine;
import java.util.*;
@Repository 

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    List<Medicine> findByPatientId(Long patientId);
}