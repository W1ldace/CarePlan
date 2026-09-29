package com.example.Careplan.config;

import com.example.Careplan.models.*;
import com.example.Careplan.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final DoseLogRepository doseLogRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed data if database is currently empty
        if (patientRepository.count() == 0) {
            
            // 1. Create Patients
            Patient p1 = new Patient();
            p1.setName("Eleanor Vance");
            p1.setAge(48);
            patientRepository.save(p1);

            Patient p2 = new Patient();
            p2.setName("Marcus Brody");
            p2.setAge(62);
            patientRepository.save(p2);

            Patient p3 = new Patient();
            p3.setName("Sophia Martinez");
            p3.setAge(34);
            patientRepository.save(p3);

            // 2. Add Prescription 1 (Eleanor - Lisinopril 08:00 AM)
            Medicine m1 = new Medicine();
            m1.setName("Lisinopril");
            m1.setDosage("10mg");
            m1.setInstructions("Take 1 tablet every morning with water");
            m1.setPatient(p1);

            Schedule s1 = new Schedule();
            s1.setScheduledTime(LocalTime.of(8, 0));
            s1.setFrequency("DAILY");
            s1.setStartDate(LocalDate.now().minusDays(5));
            s1.setMedicine(m1);
            m1.setSchedules(List.of(s1));
            medicineRepository.save(m1);

            // 3. Add Prescription 2 (Eleanor - Metformin, set to 2 hours ago to trigger Overdue Alert!)
            Medicine m2 = new Medicine();
            m2.setName("Metformin");
            m2.setDosage("500mg");
            m2.setInstructions("Take after meals");
            m2.setPatient(p1);

            Schedule s2 = new Schedule();
            s2.setScheduledTime(LocalTime.now().minusHours(2).withMinute(0));
            s2.setFrequency("DAILY");
            s2.setStartDate(LocalDate.now().minusDays(5));
            s2.setMedicine(m2);
            m2.setSchedules(List.of(s2));
            medicineRepository.save(m2);

            // 4. Add Prescription 3 (Marcus - Atorvastatin 09:00 PM)
            Medicine m3 = new Medicine();
            m3.setName("Atorvastatin");
            m3.setDosage("20mg");
            m3.setInstructions("Take at bedtime");
            m3.setPatient(p2);

            Schedule s3 = new Schedule();
            s3.setScheduledTime(LocalTime.of(21, 0));
            s3.setFrequency("DAILY");
            s3.setStartDate(LocalDate.now().minusDays(3));
            s3.setMedicine(m3);
            m3.setSchedules(List.of(s3));
            medicineRepository.save(m3);

            // 5. Add Dose Log (Mark Lisinopril as TAKEN today)
            DoseLog log1 = new DoseLog();
            log1.setSchedule(s1);
            log1.setScheduledDate(LocalDate.now());
            log1.setScheduledTime(s1.getScheduledTime());
            log1.setStatus("TAKEN");
            doseLogRepository.save(log1);
        }
    }
}
