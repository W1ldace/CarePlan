package com.example.Careplan.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.example.Careplan.models.DoseLog;
import java.time.*;
import java.util.*;
@Repository 

public interface DoseLogRepository extends JpaRepository<DoseLog, Long> {
    
    // Checks if a specific dose slot is already logged
    Optional<DoseLog> findByScheduleIdAndScheduledDate(Long scheduleId, LocalDate scheduledDate);
    
    // Fetches missed dose history for a specific patient within a date range
    List<DoseLog> findByScheduleMedicinePatientIdAndStatusAndScheduledDateBetween(
            Long patientId, String status, LocalDate start, LocalDate end);
            
    // Used for general queries based on date and status
    List<DoseLog> findByScheduledDateAndStatus(LocalDate date, String status);
}