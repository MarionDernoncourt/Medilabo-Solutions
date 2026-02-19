package com.medilabo.ms_notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.ms_notes.dto.NoteDTO;
import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.exceptions.PatientNotFoundException;
import com.medilabo.ms_notes.exceptions.PatientServiceException;
import com.medilabo.ms_notes.service.NoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(NotesController.class)
@ActiveProfiles("test")
@WithMockUser(roles = "GATEWAY")
public class NoteControllerTest {
    @Autowired
    private NotesController patientController;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private NoteServiceImpl noteService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getNotesByPatientId_shouldReturn200OK() throws Exception {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(1);
        newNote.setNote("Test for new note");

        when(noteService.getNotesByPatientId(any(Integer.class))).thenReturn(List.of(newNote));

        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].note").value("Test for new note"));
    }

    @Test
    public void getNotesByPatientId_shouldReturn404() throws Exception {
        when(noteService.getNotesByPatientId(any(Integer.class))).thenThrow(new PatientNotFoundException("Aucun patient trouvé"));

        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aucun patient trouvé"));
    }

    @Test
    public void getNotesByPatientId_shouldReturn503() throws Exception {
        when(noteService.getNotesByPatientId(any(Integer.class))).thenThrow(new PatientServiceException("Erreur de communication avec le service"));
        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Erreur de communication avec le service"));
    }

    @Test
    public void getNotesByPatientId_shouldReturn500() throws Exception {
        when(noteService.getNotesByPatientId(any(Integer.class))).thenThrow(new RuntimeException("Erreur interne"));
        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erreur interne"));
    }

    @Test
    public void saveNote_shouldReturn200_OK() throws Exception {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(1);
        newNote.setNote("Test for new note");

        String jsonNote = objectMapper.writeValueAsString(newNote);

        when(noteService.save(any(NoteDTO.class))).thenReturn(newNote);

        mockMvc.perform(post("/notes/add")
                        .with(csrf()) // Nécessaire car Spring Security protège par défaut contre le CSRF,
                        // même si celui-ci est désactivé en production pour le mode Stateless
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNote))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.note").value("Test for new note"));
    }

    @Test
    public void saveNote_shouldReturn404_NOTFOUND() throws Exception {
        Note newNote = new Note();
        newNote.setPatientId(1);
        newNote.setNote("Test for new note");

        String jsonNote = objectMapper.writeValueAsString(newNote);

        when(noteService.save(any(NoteDTO.class))).thenThrow(new PatientNotFoundException("Erreur interne"));

        mockMvc.perform(post("/notes/add")
                        .with(csrf()) // Nécessaire car Spring Security protège par défaut contre le CSRF,
                        // même si celui-ci est désactivé en production pour le mode Stateless
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNote))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Erreur interne"));
    }

    @Test
    public void saveNote_shouldReturn_SERVICEUNVALAIBLE() throws Exception {
        Note newNote = new Note();
        newNote.setPatientId(1);
        newNote.setNote("Test for new note");

        String jsonNote = objectMapper.writeValueAsString(newNote);

        when(noteService.save(any(NoteDTO.class))).thenThrow(new PatientServiceException("Erreur de communication avec le service"));

        mockMvc.perform(post("/notes/add")
                        .with(csrf()) // Nécessaire car Spring Security protège par défaut contre le CSRF,
                        // même si celui-ci est désactivé en production pour le mode Stateless
                        .contentType(MediaType.APPLICATION_JSON).content(jsonNote))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Erreur de communication avec le service"));
    }

    @Test
    public void saveNote_shouldReturn400_BADREQUEST() throws Exception {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(1);
        newNote.setNote("");

        String jsonNote = objectMapper.writeValueAsString(newNote);

        mockMvc.perform(post("/notes/add")
                        .with(csrf()) // Nécessaire car Spring Security protège par défaut contre le CSRF,
                        // même si celui-ci est désactivé en production pour le mode Stateless
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNote))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void saveNote_shouldReturn500_INTERNALERRORSERVICE() throws Exception {
        NoteDTO newNote = new NoteDTO();
        newNote.setPatientId(1);
        newNote.setNote("Test for new note");

        String jsonNote = objectMapper.writeValueAsString(newNote);

        when(noteService.save(any(NoteDTO.class))).thenThrow(new RuntimeException("Erreur interne"));

        mockMvc.perform(post("/notes/add")
                        .with(csrf()) // Nécessaire car Spring Security protège par défaut contre le CSRF,
                        // même si celui-ci est désactivé en production pour le mode Stateless
                        .contentType(MediaType.APPLICATION_JSON).content(jsonNote))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erreur interne"));
    }
}
