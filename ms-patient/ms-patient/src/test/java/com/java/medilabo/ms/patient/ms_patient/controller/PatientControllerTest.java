package com.java.medilabo.ms.patient.ms_patient.controller;

import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientAlreadyExistException;
import com.java.medilabo.ms.patient.ms_patient.exception.PatientNotFoundException;
import com.java.medilabo.ms.patient.ms_patient.service.PatientService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@ActiveProfiles("test")
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PatientService patientService;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetAllPatients_ShouldReturn200OK() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(1986, 01, 01));

        List<Patient> allPatients = List.of(patient);

        when(patientService.getAllPatients()).thenReturn(allPatients);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetPatient_ById_ShouldReturn200OK() throws Exception {
        Patient patient1 = new Patient();
        patient1.setId(1);
        patient1.setFirstname("John");
        patient1.setLastname("Doe");
        patient1.setBirthdate(LocalDate.of(1986, 01, 01));

        when(patientService.getPatientById(any(Integer.class))).thenReturn(patient1);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstname").value("John"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetPatient_ById_ShouldReturn404NOTFOUND() throws Exception {

        when(patientService.getPatientById(999))
                .thenThrow(new PatientNotFoundException("Le patient n'existe pas."));

        mockMvc.perform(get("/patients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetPatient_ById_ShouldReturn500INTERNALERROR() throws Exception {

        final String serviceExceptionMessage = "Erreur de connexion non gérée.";

        when(patientService.getPatientById(any(Integer.class)))
                .thenThrow(new RuntimeException(serviceExceptionMessage));

        final String expectedControllerMessage = "Une erreur est survenue : " + serviceExceptionMessage;

        mockMvc.perform(get("/patients/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedControllerMessage));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testCreatePatient_Success_ShouldReturn201CreatedAndLocationHeader() throws Exception {
        final Integer NEW_ID = 5;

        Patient patientCreated = new Patient();
        patientCreated.setId(NEW_ID);
        patientCreated.setFirstname("Alice");

        final String patientJson = "{\"firstname\": \"Alice\", \"lastname\": \"Zoe\", \"birthdate\": \"1990-01-01\"}";

        when(patientService.createPatient(any(Patient.class))).thenReturn(patientCreated);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/patients/" + NEW_ID))
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testCreatePatient_Success_ShouldReturn409CONFLICT() throws Exception {
        final String expectedErrorMessage = "Le patient existe déjà dans la base de données.";
        final String patientJson = "{\"firstname\": \"Alice\", \"lastname\": \"Zoe\", \"birthdate\": \"1990-01-01\"}";

        when(patientService.createPatient(any(Patient.class)))
                .thenThrow(new PatientAlreadyExistException(expectedErrorMessage));

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson)
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testCreatePatient_Success_ShouldReturn500INTERNALERROR() throws Exception {
        final String serviceExceptionMessage = "Erreur de base de données inattendue.";
        final String patientJson = "{\"firstname\": \"Alice\", \"lastname\": \"Zoe\", \"birthdate\": \"1990-01-01\"}";

        when(patientService.createPatient(any(Patient.class)))
                .thenThrow(new RuntimeException(serviceExceptionMessage));

        final String expectedControllerMessage = "Une erreur est survenue : " + serviceExceptionMessage;

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson)
                        .with(csrf()))

                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedControllerMessage));
    }

    @Test
    @WithMockUser(username = "username", roles = {"USER"})
    public void testUpdatePatient_shouldReturn200OK() throws Exception {

        final Integer EXISTING_ID = 1;

        final String updatedFirstName = "Alice_UPDATED";
        final String patientJson = "{\"firstname\": \"" + updatedFirstName + "\", \"lastname\": \"Zoe\", \"birthdate\": \"1990-01-01\"}";

        Patient patientUpdated = new Patient();
        patientUpdated.setId(EXISTING_ID);
        patientUpdated.setFirstname(updatedFirstName);

        when(patientService.updatePatient(eq(EXISTING_ID), any(Patient.class))).thenReturn(patientUpdated);

        mockMvc.perform(put("/patients/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value(updatedFirstName));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdatePatient_shouldReturn404NOTFOUND() throws Exception {

        final Integer EXISTING_ID = 1;
        final String updatedFirstName = "Alice_UPDATED";
        final String patientJson = "{\"firstname\": \"" + updatedFirstName + "\", \"lastname\": \"Zoe\", \"birthdate\": \"1990-01-01\"}";

        when(patientService.updatePatient(eq(EXISTING_ID), any(Patient.class))).thenThrow(new PatientNotFoundException("Le patient n'existe pas"));

        mockMvc.perform(put("/patients/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "username", roles = {"USER"})
    public void testUpdatePatient_shouldReturn500INTERNALERROR() throws Exception {

        final String serviceExceptionMessage = "Erreur de base de données inattendue.";

        final Integer EXISTING_ID = 1;
        final String updatedFirstName = "Alice_UPDATED";
        final String patientJson = "{\"firstname\": \"" + updatedFirstName + "\", \"lastname\": \"Zoe\", \"birthdate\": \"1990-01-01\"}";

        when(patientService.updatePatient(eq(EXISTING_ID), any(Patient.class)))
                .thenThrow(new RuntimeException(serviceExceptionMessage));

        final String expectedControllerMessage = "Une erreur est survenue : " + serviceExceptionMessage;

        mockMvc.perform(put("/patients/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson)
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(expectedControllerMessage));

    }
}
