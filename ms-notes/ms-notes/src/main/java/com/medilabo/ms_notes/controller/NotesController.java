package com.medilabo.ms_notes.controller;

import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.service.INoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);
    @Autowired
    private INoteService noteService;

    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatientId(@PathVariable("patientId") Integer patientId){
        return noteService.getNotesByPatientId(patientId);
    }

    @PostMapping("/add")
    public Note saveNote(@RequestBody Note note){
        return noteService.save(note);
    }
}
