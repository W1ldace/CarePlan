package com.example.Careplan.services;

import com.example.Careplan.models.*;
import com.example.Careplan.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepo;
    private final DoseLogRepository doseLogRepo;

    public List<DoseLog> getTodayDoses(Long patientId) {
        LocalDate today = LocalDate.now();
        // Fetch all schedules for the patient and stream them for processing
        return scheduleRepo.findByMedicinePatientId(patientId).stream()
        // (Today must be >= start date AND today must be <= end date, if an end date exists)
            .filter(s -> !today.isBefore(s.getStartDate()) && (s.getEndDate() == null || !today.isAfter(s.getEndDate())))
            // Step 2: Map each active schedule to its corresponding DoseLog for today
            .map(s -> doseLogRepo.findByScheduleIdAndScheduledDate(s.getId(), today)
            // If no log exists for today yet, create a temporary "PENDING" entry in-memory
                .orElseGet(() -> {
                    DoseLog pending = new DoseLog();
                    pending.setSchedule(s);
                    pending.setScheduledDate(today);
                    pending.setScheduledTime(s.getScheduledTime());
                    pending.setStatus("PENDING");
                    return pending;
                }))
            .toList();
    }
}