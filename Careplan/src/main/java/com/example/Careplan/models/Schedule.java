package com.example.Careplan.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.time.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime scheduledTime;
    
    private String frequency;

    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    @JsonIgnore
    private Medicine medicine;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<DoseLog> logs;
}