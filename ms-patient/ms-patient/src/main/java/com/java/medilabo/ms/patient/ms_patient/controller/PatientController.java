package com.java.medilabo.ms.patient.ms_patient.controller;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientAlreadyExistException;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientNotFoundException;
import com.java.medilabo.ms.patient.ms_patient.service.IPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final IPatientService patientService;

    private final Logger logger = LoggerFactory.getLogger(PatientController.class);

    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("")
    public ResponseEntity<?> getPatients() {
        logger.info("Reçu requete GET / patients : Récupération de tous les patients");
        try {
            List<PatientDTO> patients = patientService.getAllPatients();
            logger.info("Requete GET / Patient réussie : nombre de patients trouvés : {}", patients.size());
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Erreur lors de la récupération de tous les patients : {}", e.getMessage(), e);
            return new ResponseEntity<>("Une erreur est survenue : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatient(@PathVariable Integer id) {
        logger.info("Reçu requête GET /patients/{}: Récupération du patient.", id);
        try {
            PatientDTO patient = patientService.getPatientById(id);
            logger.info("Requête GET /patients/{} réussie. Statut: 200 OK.", id);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (PatientNotFoundException e) {
            logger.warn("Patient ID {} non trouvé (404).", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération du patient ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Une erreur est survenue : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createPatient(@RequestBody PatientDTO patient) {
        logger.info("Reçu requête POST /patients : Création d'un nouveau patient ({} {}).", patient.getFirstname(), patient.getLastname());
        try {
            Patient newPatient = patientService.createPatient(patient);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newPatient.getId())
                    .toUri();
            logger.info("Patient créé avec succès. ID généré : {}. Statut: 201 Created.", newPatient.getId());
            return ResponseEntity.created(location).build();
        } catch (PatientAlreadyExistException e) {
            logger.warn("Tentative de création d'un patient existant (409 Conflict).", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création du patient : {}", e.getMessage(), e);
            return new ResponseEntity<>("Une erreur est survenue : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @RequestBody PatientDTO patient) {
        logger.info("Reçu requête PUT /patients/{}: Tentative de mise à jour.", id);
        try {
            PatientDTO patientUpdated = patientService.updatePatient(id, patient);
            logger.info("Patient ID {} mis à jour avec succès. Statut: 200 OK.", id);
            return new ResponseEntity<>(patientUpdated, HttpStatus.OK);
        } catch (PatientNotFoundException e) {
            logger.warn("Tentative de mise à jour du patient ID {} non trouvé (404).", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la mise à jour du patient ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Une erreur est survenue : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
