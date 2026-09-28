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
        return scheduleRepo.findByMedicinePatientId(patientId).stream()
            .filter(s -> !today.isBefore(s.getStartDate()) && (s.getEndDate() == null || !today.isAfter(s.getEndDate())))
            .map(s -> doseLogRepo.findByScheduleIdAndScheduledDate(s.getId(), today)
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