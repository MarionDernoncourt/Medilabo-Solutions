package com.java.medilabo.ms.patient.ms_patient.service;

import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientAlreadyExistException;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientNotFoundException;
import com.java.medilabo.ms.patient.ms_patient.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private Logger logger = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            logger.warn("No patients found");
        }
        return patients;
    }

    public Patient getPatientById(Integer id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> {
            logger.warn("Patient with id {} not found", id);
            throw new PatientNotFoundException("Aucun patient trouvé avec cet id " + id);
        });
        logger.info("Patient found with id {}", id);
        return patient;
    }

    public Patient createPatient(Patient patient) {
        Optional<Patient> newPatient = patientRepository.findByFirstnameAndLastnameAndBirthdate(patient.getFirstname(), patient.getLastname(), patient.getBirthdate());
        if (newPatient.isPresent()) {
            logger.warn("Patient with id {} already exists", patient.getId());
            throw new PatientAlreadyExistException("A patient with the same identity exists.");
        }
        logger.info("Patient created : {} {}", patient.getFirstname(), patient.getLastname());
        return patientRepository.save(patient);

    }

    public Patient updatePatient(Integer id, Patient patient) {
        Patient patientToUpdate = patientRepository.findById(id)
                .orElseThrow( () -> {
                    logger.warn("Patient with id {} not found", id);
                    throw new PatientNotFoundException("Aucun patient trouvé");
                });

        //Mise à jour des informations personnelles du patient
        patientToUpdate.setFirstname(patient.getFirstname());
        patientToUpdate.setLastname(patient.getLastname());
        patientToUpdate.setBirthdate(patient.getBirthdate());
        patientToUpdate.setGenre(patient.getGenre());
        patientToUpdate.setAddress(patient.getAddress());
        patientToUpdate.setPhoneNumber(patient.getPhoneNumber());

        //Sauvegarde de la mise à jour
        logger.info("Patient updated : {} '{}'", patient.getFirstname(), patient.getLastname());
        return patientRepository.save(patientToUpdate);

    }


}
