package com.example.Careplan.services;

import com.example.Careplan.models.*;
import com.example.Careplan.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoseLogService {

    private final DoseLogRepository doseLogRepository;
    private final ScheduleRepository scheduleRepository;

    // Feature 3: Mark dose as taken or missed
    public DoseLog logDose(DoseLog logRequest) {
        
        // 1. Verify the schedule exists
        Schedule schedule = scheduleRepository.findById(logRequest.getSchedule().getId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        // 2. Check if a log already exists for this exact date
        Optional<DoseLog> existingLogOpt = doseLogRepository.findByScheduleIdAndScheduledDate(
                schedule.getId(), logRequest.getScheduledDate());

        if (existingLogOpt.isPresent()) {
            DoseLog existingLog = existingLogOpt.get();
            
            // Rule: Stop them from changing a TAKEN dose
            if ("TAKEN".equalsIgnoreCase(existingLog.getStatus())) {
                throw new IllegalStateException("A dose already marked taken cannot be logged again for the same slot");
            }
            
            // Update the existing record (e.g., changing MISSED to TAKEN)
            existingLog.setStatus(logRequest.getStatus());
            if ("TAKEN".equalsIgnoreCase(logRequest.getStatus())) {
                existingLog.setTakenTime(LocalDateTime.now());
            }
            return doseLogRepository.save(existingLog);
        }

        // 3. If no log exists yet, prepare and save the new one
        logRequest.setSchedule(schedule);
        if ("TAKEN".equalsIgnoreCase(logRequest.getStatus())) {
            logRequest.setTakenTime(LocalDateTime.now());
        }
        
        return doseLogRepository.save(logRequest);
    }

    // Feature 4: View missed-dose history
    public List<DoseLog> getMissedDoses(Long patientId, LocalDate startDate, LocalDate endDate) {
        // We let the custom repository method do all the hard work here
        return doseLogRepository.findByScheduleMedicinePatientIdAndStatusAndScheduledDateBetween(
                patientId, "MISSED", startDate, endDate);
    }

    // Feature 5: Alerts doses overdue by more than 1 hour
    public List<Schedule> getOverdueAlerts(Long patientId) {
        List<Schedule> allPatientSchedules = scheduleRepository.findByMedicinePatientId(patientId);
        List<Schedule> overdueAlerts = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        LocalTime oneHourAgo = LocalTime.now().minusHours(1);

        for (Schedule schedule : allPatientSchedules) {
            
            // 1. Check if the schedule is currently active for today's date
            boolean isStarted = !today.isBefore(schedule.getStartDate());
            boolean isNotEnded = (schedule.getEndDate() == null) || !today.isAfter(schedule.getEndDate());
            
            if (isStarted && isNotEnded) {
                
                // 2. Check if the scheduled time passed more than an hour ago
                if (schedule.getScheduledTime().isBefore(oneHourAgo)) {
                    
                    // 3. Check if the patient already took it today
                    Optional<DoseLog> todayLog = doseLogRepository.findByScheduleIdAndScheduledDate(schedule.getId(), today);
                    
                    boolean isTaken = todayLog.isPresent() && "TAKEN".equalsIgnoreCase(todayLog.get().getStatus());
                    
                    // If it is not taken, add it to our alert list
                    if (!isTaken) {
                        overdueAlerts.add(schedule);
                    }
                }
            }
        }
        
        return overdueAlerts;
    }
}