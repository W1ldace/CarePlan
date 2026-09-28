package com.example.Careplan.controllers;

import com.example.Careplan.models.DoseLog;
import com.example.Careplan.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;

    // Feature 2: Returns all expected doses for today
    @GetMapping("/patients/{patientId}/doses/today")
    public ResponseEntity<List<DoseLog>> getTodayDoses(@PathVariable Long patientId) {
        return ResponseEntity.ok(scheduleService.getTodayDoses(patientId));
    }
}