package com.java.medilabo.ms.patient.ms_patient.service;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientAlreadyExistException;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientNotFoundException;
import com.java.medilabo.ms.patient.ms_patient.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientService {

    private final Logger logger = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            logger.warn("No patients found");
            return Collections.emptyList();
        }

        return patients.stream()
                .map(patient -> {
                    return new PatientDTO(patient);
                })
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getPatientById(Integer id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> {
            logger.warn("Patient with id {} not found", id);
            return new PatientNotFoundException("No patient found with this id : " + id);
        });
        logger.info("Patient found with id {}", id);

        return new PatientDTO(patient);
    }

    @Override
    public Patient createPatient(PatientDTO patient) {
        Optional<Patient> newPatient = patientRepository.findByFirstnameAndLastnameAndBirthdateAndGenre(patient.getFirstname(), patient.getLastname(), patient.getBirthdate(), patient.getGenre());
        if (newPatient.isPresent()) {
            logger.warn("Patient with this identity already exists : {} {} {} {}", patient.getFirstname(), patient.getLastname(), patient.getBirthdate(), patient.getGenre());
            throw new PatientAlreadyExistException("A patient with the same identity exists.");
        }

        Patient patientToSave = new Patient(patient);

        Patient savedPatient = patientRepository.save(patientToSave);

        logger.info("Patient created : {} {}", patient.getFirstname(), patient.getLastname());
        return savedPatient;

    }

    @Override
    public PatientDTO updatePatient(Integer id, PatientDTO patient) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Patient not found");
                    return new PatientNotFoundException("Patient not found");
                });
        //Mise à jour des informations personnelles du patient
        patientToUpdate.updateFromDto(patient);
        //Sauvegarde de la mise à jour
        Patient savedPatient = patientRepository.save(patientToUpdate);
        logger.info("Patient updated : {} '{}'", patient.getFirstname(), patient.getLastname());

        return new PatientDTO(savedPatient);

    }


}
