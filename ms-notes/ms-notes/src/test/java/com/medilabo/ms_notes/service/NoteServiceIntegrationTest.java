package com.medilabo.ms_notes.service;

import com.medilabo.ms_notes.dto.NoteDTO;
import com.medilabo.ms_notes.dto.PatientDTO;
import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.exceptions.PatientNotFoundException;
import com.medilabo.ms_notes.exceptions.PatientServiceException;
import com.medilabo.ms_notes.proxies.IPatientProxy;
import com.medilabo.ms_notes.repository.INoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class NoteServiceIntegrationTest {

    @Autowired
    private NoteServiceImpl noteService;
    @Autowired
    private INoteRepository noteRepository;
    @MockitoBean
    private IPatientProxy patientProxy;

    private Note referenceNote;

    @BeforeEach
    public void setUp() {
        noteRepository.deleteAll();

        Note note = new Note();
        note.setPatientId(99);
        note.setNote("Note de référence pré-existante");

        this.referenceNote = noteRepository.save(note);
    }

    @AfterEach
    public void tearDown() {
        noteRepository.deleteAll();
    }

    @Test
    public void getNotesTest() {
        List<NoteDTO> notes = noteService.getNotesByPatientId(99);
        assertEquals(1, notes.size());
        assertEquals(referenceNote.getNote(), notes.get(0).getNote());
        assertEquals(referenceNote.getId(), notes.get(0).getId());
    }

    @Test
    public void getNotesTest_WhenPatientIsNotFound() {
        when(patientProxy.getPatientById(any(Integer.class))).thenThrow(new PatientNotFoundException("Patient not found with id: " + 99));
        assertThrows(PatientNotFoundException.class, () -> noteService.getNotesByPatientId(1));
    }

    @Test
    public void getNotesTest_WhenPatientServiceIsUnavailable() {
        when(patientProxy.getPatientById(any(Integer.class))).thenThrow(new PatientServiceException("Erreur de communication avec le service"));
        assertThrows(PatientServiceException.class, () -> noteService.getNotesByPatientId(99));
    }

    @Test
    public void saveNotesTest_WhenSuccess() {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(99);
        newNote.setNote("Ma 2eme note");

        NoteDTO note = noteService.save(newNote);

        List<NoteDTO> notes = noteService.getNotesByPatientId(99);

        assertEquals(2, notes.size());
        assertEquals("Ma 2eme note", note.getNote());
    }

    @Test
    public void saveNotesTest_WhenPatientIsNotFound() {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(12);
        newNote.setNote("Ma 2eme note");

        when(patientProxy.getPatientById(any(Integer.class))).thenThrow(new PatientNotFoundException("Patient not found"));

        assertThrows(PatientNotFoundException.class, () -> noteService.save(newNote));
    }

    @Test
    public void saveNotedTest_WhenPatientServiceIsUnavailable() {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(12);
        newNote.setNote("Ma 2eme note");

        when(patientProxy.getPatientById(any(Integer.class))).thenThrow(new PatientServiceException("Erreur de communication avec le service"));

        assertThrows(PatientServiceException.class, () -> noteService.save(newNote));
    }

    @Test
    public void saveNotesTest_ShouldGenerateCreatedAt() {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(99);
        newNote.setNote("Test audit");
        when(patientProxy.getPatientById(99)).thenReturn(new PatientDTO());

        NoteDTO savedNote = noteService.save(newNote);

        assertNotNull(savedNote.getCreatedAt());
    }


}