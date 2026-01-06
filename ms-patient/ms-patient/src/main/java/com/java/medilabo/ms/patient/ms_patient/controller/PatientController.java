package com.java.medilabo.ms.patient.ms_patient.controller;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;

import com.java.medilabo.ms.patient.ms_patient.service.IPatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        logger.debug("Requête GET /patient/list reçue : Récupération de tous les patients.");
        List<PatientDTO> patients = patientService.getAllPatients();

        logger.info("Réponse reçue : 200 OK, nombre de patients trouvés : {}", patients.size());
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Integer id) {
        logger.debug("Requête GET /patient/{} reçue.", id);
        PatientDTO patient = patientService.getPatientById(id);

        logger.info("Réponse reçue : 200 OK pour le patient ID {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @PostMapping("/")
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patient) {
        logger.info("Reçu requête POST /patient : Création d'un nouveau patient ({} {}).", patient.getFirstname(), patient.getLastname());
        Patient newPatient = patientService.createPatient(patient);

        logger.info("Réponse reçue : 201 CREATED : Le patient {} {} a bien été créé.", newPatient.getFirstname(), newPatient.getLastname());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PatientDTO(newPatient));

    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDTO patient) {
        logger.info("Reçu requête PUT /patient/{}: Tentative de mise à jour.", id);
        PatientDTO patientUpdated = patientService.updatePatient(id, patient);

        logger.info("Patient ID {} mis à jour avec succès. Statut: 200 OK.", id);
        return new ResponseEntity<>(patientUpdated, HttpStatus.OK);
    }

}
