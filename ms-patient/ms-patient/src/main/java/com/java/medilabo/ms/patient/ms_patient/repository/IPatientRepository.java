package com.java.medilabo.ms.patient.ms_patient.repository;

import com.java.medilabo.ms.patient.ms_patient.entity.Genre;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<Patient,Integer> {

    Optional<Patient> findByFirstnameAndLastnameAndBirthdateAndGenre(String firstname, String lastname, LocalDate birthdate, Genre genre);
}
