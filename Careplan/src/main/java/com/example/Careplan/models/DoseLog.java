package com.example.Careplan.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.time.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DoseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate scheduledDate;
    
    private LocalTime scheduledTime;

    private LocalDateTime takenTime;

    private String status; 

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonIgnore
    private Schedule schedule;
}