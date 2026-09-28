package com.example.Careplan.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String dosage;

    private String instructions;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore 
    private Patient patient;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}