package com.java.medilabo.ms.patient.ms_patient.repository;

import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {
}
