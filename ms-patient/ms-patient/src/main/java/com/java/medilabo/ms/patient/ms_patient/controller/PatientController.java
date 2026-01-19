package com.java.medilabo.ms.patient.ms_patient.controller;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;

import com.java.medilabo.ms.patient.ms_patient.service.IPatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final IPatientService patientService;

    private final Logger logger = LoggerFactory.getLogger(PatientController.class);

    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/list") // Mappe sur GET /patients
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        logger.debug(" GET /patient/list request received : Fetching all patients.");
        List<PatientDTO> patients = patientService.getAllPatients();

        logger.info("Response received : 200 OK, patients found : {}", patients.size());
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Integer id) {
        logger.debug("GET /patient/{} request received", id);
        PatientDTO patient = patientService.getPatientById(id);

        logger.info("Response received : 200 OK for the patient with the id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @PostMapping("/")
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patient) {
        logger.info("POST /patient request received: New patient created :  ({} {}).", patient.getFirstname(), patient.getLastname());
        Patient newPatient = patientService.createPatient(patient);

        logger.info("Response received : 201 CREATED : The patient {} {} has been created with the id {}.", newPatient.getFirstname(), newPatient.getLastname(), newPatient.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PatientDTO(newPatient));

    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDTO patient) {

        logger.info("PUT /patient/{} request received: Trying to updated.", id);
        PatientDTO patientUpdated = patientService.updatePatient(id, patient);

        logger.info("Response received : 200 OK : The patient with id {} has been updated", id);
        return new ResponseEntity<>(patientUpdated, HttpStatus.OK);
    }

}
