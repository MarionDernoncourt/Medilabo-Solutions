package com.java.medilabo.ms.patient.ms_patient.service;

import com.java.medilabo.ms.patient.ms_patient.entity.Genre;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientAlreadyExistException;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientNotFoundException;
import com.java.medilabo.ms.patient.ms_patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PatientIntegrationTest {

    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;


    @Test
    public void testGetAllPatient() {
        List<Patient> patients = patientService.getAllPatients();
        assertEquals(4, patients.size());
    }

    @Test
    public void testGetPatientById() {
        Patient patient = patientService.getPatientById(1);
        assertEquals("Test", patient.getFirstname());
        assertEquals("TestNone", patient.getLastname());
    }

    @Test
    public void testGetPatientById_NotFound() {
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.getPatientById(28);
        });

    }

    @Test
    public void testCreatePatient_Success() {
        Patient newPatient = new Patient("John", "Doe", LocalDate.of(1966, 12, 31), Genre.MASCULIN, "111 main street", "123-255-2545");

        Patient patientEnregistre = patientService.createPatient(newPatient);

        assertNotNull(patientEnregistre.getId());
        assertEquals("John", patientEnregistre.getFirstname());
    }

    @Test
    public void testCreatePatient_AlreadyExists() {
        Patient patientExistant = new Patient(
                1,
                "Test",
                "TestNone",
                LocalDate.of(1966, 12, 31),
                Genre.FEMININ,
                "123 main Street",
                "111-222-4566"
        );
        assertThrows(PatientAlreadyExistException.class, () -> {
            patientService.createPatient(patientExistant);
        });
    }

    @Test
    public void testUpdatePatient_Success() {
        final Integer existingId = 1;
        final String newAddress = "99 Rue de l'Update";
        final String newPhone = "555-123-4567";

        Patient patientToUpdate = patientService.getPatientById(existingId);

        patientToUpdate.setAddress(newAddress);
        patientToUpdate.setPhoneNumber(newPhone);

        Patient updatedPatient = patientService.updatePatient(existingId, patientToUpdate);

               assertEquals(newAddress, updatedPatient.getAddress());
        assertEquals(newPhone, updatedPatient.getPhoneNumber());
        assertEquals(existingId, updatedPatient.getId());

        Patient patientInDB = patientRepository.findById(existingId).get();
        assertEquals(newAddress, patientInDB.getAddress());
        assertEquals("TestNone", patientInDB.getLastname());
    }

    @Test
    public void testUpdatePatient_NotFound() {
        Integer nonExistentId = 99;

        Patient nonExistentPatient = new Patient(
                nonExistentId,
                "Non", "Existant", LocalDate.now(), Genre.FEMININ, "Fake Address", "111-111-1111"
        );

        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatient(nonExistentId, nonExistentPatient);
        }, "PatientNotFoundException doit être levée si l'ID du patient à mettre à jour n'existe pas.");
    }

}
