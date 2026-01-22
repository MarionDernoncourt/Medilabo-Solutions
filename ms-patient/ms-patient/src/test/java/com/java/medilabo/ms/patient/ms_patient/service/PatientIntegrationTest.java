package com.java.medilabo.ms.patient.ms_patient.service;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import com.java.medilabo.ms.patient.ms_patient.entity.Genre;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientAlreadyExistException;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientNotFoundException;
import com.java.medilabo.ms.patient.ms_patient.repository.IPatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PatientIntegrationTest {

    @Autowired
    private PatientService patientService;
    @Autowired
    private IPatientRepository patientRepository;


    @Test
    public void testGetAllPatient() {
        List<PatientDTO> patients = patientService.getAllPatients();
        assertEquals(4, patients.size());
    }

    @Test
    public void testGetPatientById() {
        PatientDTO patient = patientService.getPatientById(1);
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
        PatientDTO inputDTO = new PatientDTO(
                "John",
                "Doe",
                LocalDate.of(1966, 12, 31),
                Genre.MASCULIN,
                "111 main street",
                "123-255-2545"
        );

        Patient savedPatient = patientService.createPatient(inputDTO);

        assertNotNull(savedPatient);
        assertNotNull(savedPatient.getId());


        Optional<Patient> patientInDB = patientRepository.findById(savedPatient.getId());

        assertTrue(patientInDB.isPresent());
        assertEquals("John", patientInDB.get().getFirstname());
        assertEquals(LocalDate.of(1966, 12, 31), patientInDB.get().getBirthdate());
    }

    @Test
    public void testCreatePatient_AlreadyExists() {
        PatientDTO patientExistant = new PatientDTO(
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
    public void testUpdatePatient_Success_Isolation() {
        PatientDTO initialDTO = new PatientDTO(
                "UpdateTest", "User", LocalDate.of(1980, 5, 5), // <--- BIRTHDATE GARANTI NON-NULL
                Genre.FEMININ, "Old Address", "000-000-0000"
        );
        Patient createdPatient = patientService.createPatient(initialDTO);

        final Integer testId = createdPatient.getId();

        final String newAddress = "99 Rue de l'Update";
        final String newPhone = "555-123-4567";

        PatientDTO updateDTO = new PatientDTO(
                createdPatient.getFirstname(),
                createdPatient.getLastname(),
                createdPatient.getBirthdate(),
                createdPatient.getGenre(),
                newAddress,
                newPhone
        );

        PatientDTO updatedPatient = patientService.updatePatient(testId, updateDTO);

        assertEquals(newAddress, updatedPatient.getAddress());
        assertEquals(newPhone, updatedPatient.getPhoneNumber());

    }

    @Test
    public void testUpdatePatient_NotFound() {

        PatientDTO nonExistentPatient = new PatientDTO(
                "Non", "Existant", LocalDate.now(), Genre.FEMININ, "Fake Address", "111-111-1111"
        );

        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatient(99, nonExistentPatient);
        }, "PatientNotFoundException doit être levée si l'ID du patient à mettre à jour n'existe pas.");
    }

}
