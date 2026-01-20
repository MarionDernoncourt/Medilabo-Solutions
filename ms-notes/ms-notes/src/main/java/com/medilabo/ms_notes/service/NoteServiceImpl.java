package com.medilabo.ms_notes.service;


import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.repository.INoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoteServiceImpl implements INoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private INoteRepository noteRepository;

    @Override
    public List<Note> getNotesByPatientId(Integer patientId) {
        logger.debug("Trying to get Notes for patient with id: " + patientId);

        return noteRepository.findByPatientId(patientId);
    }

    @Override
    public Note save(Note note) {
        logger.debug("Trying to save new note for patient: {}", note.getPatientId());
        return noteRepository.save(note);
    }
}
