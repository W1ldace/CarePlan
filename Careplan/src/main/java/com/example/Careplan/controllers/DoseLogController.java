package com.example.Careplan.controllers;

import com.example.Careplan.models.*;
import com.example.Careplan.services.DoseLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DoseLogController {
    
    private final DoseLogService doseLogService;

    @PostMapping("/doselogs")
    public DoseLog logDose(@Valid @RequestBody DoseLog doseLog) {
        return doseLogService.logDose(doseLog);
    }

    @GetMapping("/patients/{patientId}/doselogs/missed")
    public List<DoseLog> getMissedDoses(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return doseLogService.getMissedDoses(patientId, startDate, endDate);
    }

    @GetMapping("/patients/{patientId}/alerts/overdue")
    public List<Schedule> getOverdueAlerts(@PathVariable Long patientId) {
        return doseLogService.getOverdueAlerts(patientId);
    }
}