package com.medilabo.ms_notes.controller;

import com.medilabo.ms_notes.dto.NoteDTO;
import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.service.INoteService;
import feign.Response;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    private final INoteService noteService;

    public NotesController(INoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NoteDTO>> getNotesByPatientId(@PathVariable("patientId") Integer patientId){
        logger.info("GET /notes/patient/{} : Request received for patient: {}", patientId, patientId);
        List<NoteDTO> notes = noteService.getNotesByPatientId(patientId);

        logger.info("Response received : 200 OK : The patient has {} notes", notes.size());
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @PostMapping("/add")
    public ResponseEntity<NoteDTO> saveNote(@Valid @RequestBody NoteDTO note){
        logger.info("POST /notes/add request received for patient: {}", note.getPatientId());
        NoteDTO newNote = noteService.save(note);
        logger.info("Response received : 201 CREATED: The note has been add to the patient {}.", newNote.getPatientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newNote);
        }
}
