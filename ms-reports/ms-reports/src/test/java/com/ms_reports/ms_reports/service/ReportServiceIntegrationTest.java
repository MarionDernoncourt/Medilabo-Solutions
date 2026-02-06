package com.ms_reports.ms_reports.service;

import com.ms_reports.ms_reports.entity.NoteDTO;
import com.ms_reports.ms_reports.entity.PatientDTO;
import com.ms_reports.ms_reports.entity.RiskLevel;
import com.ms_reports.ms_reports.exceptions.NoteNotFoundException;
import com.ms_reports.ms_reports.exceptions.NoteServiceException;
import com.ms_reports.ms_reports.exceptions.PatientNotFoundException;
import com.ms_reports.ms_reports.exceptions.PatientServiceException;
import com.ms_reports.ms_reports.proxies.INotesProxy;
import com.ms_reports.ms_reports.proxies.IPatientProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReportServiceIntegrationTest {

    @Autowired
    private IReportService reportService;
    @MockitoBean
    private INotesProxy notesProxy;
    @MockitoBean
    private IPatientProxy patientProxy;

    private final Integer TEST_ID = 1;

    @Test
    public void calculateRisk_ShouldReturnNone_WhenNoTriggersFound() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("FEMININ", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Le patient va bien"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("Aucun",  risk.getLabel());
    }

    @Test
    public void calculateRisk_ShouldReturnBorderline_ForPatientOver30WithTwoTriggers() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("FEMININ", 32));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Taille, poids a surveiller"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("Risque limité",  risk.getLabel());
    }

    @Test
    public void calculateRisk_ShouldReturnInDanger_ForYoungManWithThreeTriggers() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Taille, poids a surveiller + Cholesterol ++"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("En danger",  risk.getLabel());
    }

    @Test
    public void calculateRisk_ShouldReturnInDanger_ForWomanOver30WithSixTriggers() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("FEMININ", 35));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Taille, poids a surveiller + Cholesterol ++, vertiges, fumeuse, microalbumine"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("En danger",  risk.getLabel());
    }

    @Test
    public void calculateRisk_ShouldReturnEarlyOnset_ForYoungWomanWithSevenTriggers() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("FEMININ", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Taille, poids a surveiller + Cholesterol, vertiges, fumeuse, microalbumine et anticorps"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("Apparition précoce",  risk.getLabel());
    }

    @Test
    public void calculateRisk_ShouldReturnEarlyOnset_ForPatientOver30WithEightTriggers() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 42));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Réaction, Taille, poids a surveiller + Cholesterol, vertiges, fumeuse, microalbumine et anticorps"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("Apparition précoce",  risk.getLabel());
    }

    @Test
    public void countTriggers_ShouldBeCaseInsensitive() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("REACTION, taille,CHOLEStérol"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("En danger",  risk.getLabel());
    }

    @Test
    public void countTriggers_ShouldIgnoreAccents() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Reaction, hemoglobine A1C, cholesterol"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("En danger",  risk.getLabel());
    }

    @Test
    public void countTriggers_ShouldHandlePlurals() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Reactions, hemoglobine A1C, cholesterols"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("En danger",  risk.getLabel());
    }

    @Test
    public void countTriggers_ShouldNotCountTwiceSameTriggerInDifferentNotes() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 25));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Reactions, hemoglobine A1C, cholesterols", "Reaction"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("En danger",  risk.getLabel());
    }

    @Test
    public void evaluateRiskLevel_ShouldHandleExactly30YearsOld() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 30));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("Taille, poids a surveiller + Cholesterol ++"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("Risque limité",  risk.getLabel());
    }

    @Test
    public void evaluateRiskLevel_ShouldReturnNotDefined_WhenNoConditionsAreMet() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 30));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("poids a surveiller"));

        RiskLevel risk = reportService.calculateRisk(TEST_ID);

        assertEquals("Indéterminé",  risk.getLabel());
    }

    @Test
    public void getPatientById_ShouldThrowPatientNotFoundException_WhenFeignReturns404() {
        when(patientProxy.getPatientById(any(Integer.class))).thenThrow(new PatientNotFoundException("Aucun patient trouvé"));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("poids a surveiller"));

    assertThrows(PatientNotFoundException.class, () -> {
        reportService.calculateRisk(3);
    });
    }

    @Test
    public void getPatientById_ShouldThrowPatientServiceException_WhenFeignFails() {
        when(patientProxy.getPatientById(any(Integer.class))).thenThrow(new PatientServiceException("Erreur de communication"));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenReturn(createNotes("poids a surveiller"));

        assertThrows(PatientServiceException.class, () -> {
            reportService.calculateRisk(3);
        });
    }

    @Test
    public void getNotesByPatientId_ShouldThrowNoteNotFoundException_WhenNoNotesFoundAtProxyLevel() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 30));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenThrow(new NoteNotFoundException("Aucune note trouvée"));

        assertThrows(NoteNotFoundException.class, () -> {
            reportService.calculateRisk(TEST_ID);
        });
    }

    @Test
    public void getNotesByPatientId_ShouldThrowNoteServiceException_WhenNotesProxyIsDown() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(createPatient("MASCULIN", 30));
        when(notesProxy.getNotesByPatientId(any(Integer.class))).thenThrow(new NoteServiceException("Erreur de communication"));

        assertThrows(NoteServiceException.class, () -> {
            reportService.calculateRisk(TEST_ID);
        });
    }

    ///  --- HELPERS --- ///
    // Crée un patient
    private PatientDTO createPatient(String genre, int age) {
        PatientDTO patient = new PatientDTO();
        patient.setId(TEST_ID);
        patient.setGenre(genre);
        patient.setBirthdate(LocalDate.now().minusYears(age));
        return patient;
    }
    // Crée une liste de notes
    public List<NoteDTO> createNotes(String ... contents) {
        List<NoteDTO> notes = new ArrayList<>();
        for (String content : contents) {
            notes.add(new NoteDTO(null, TEST_ID, content));
        }
    return notes;
    }
}
