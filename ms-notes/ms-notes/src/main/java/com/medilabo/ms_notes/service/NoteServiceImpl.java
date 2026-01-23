package com.medilabo.ms_notes.service;

import com.medilabo.ms_notes.dto.PatientDTO;
import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.exceptions.PatientNotFoundException;
import com.medilabo.ms_notes.exceptions.PatientServiceException;
import com.medilabo.ms_notes.proxies.IPatientProxy;
import com.medilabo.ms_notes.repository.INoteRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class NoteServiceImpl implements INoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private INoteRepository noteRepository;
    @Autowired
    private IPatientProxy patientProxy;

    public NoteServiceImpl(INoteRepository noteRepository, IPatientProxy patientProxy) {
        this.noteRepository = noteRepository;
        this.patientProxy = patientProxy;
    }
    private void validatePatientExists(Integer patientId) {
        try {
            patientProxy.getPatientById(patientId);
        } catch (FeignException.NotFound e) {
            throw new PatientNotFoundException("Patient not found with id: " + patientId);
        } catch (FeignException e) {
            throw new PatientServiceException("Communication error with Patient Service");
        }
    }

    @Override
    public List<Note> getNotesByPatientId(Integer patientId) {
        logger.debug("Trying to get Notes for patient with id: " + patientId);
       validatePatientExists(patientId);
        List<Note> notes =  noteRepository.findNotesByPatientId(patientId);
        if(notes.isEmpty()){
            logger.warn("Not notes found for this patient.");
            return Collections.emptyList();
        }
        return notes;
    }

    @Override
    public Note save(Note note) {
        logger.debug("Trying to save new note for patient: {}", note.getPatientId());
        validatePatientExists(note.getPatientId());
        Note savedNote= noteRepository.save(note);
        logger.info("Note saved with id: {} ", savedNote.getId());
        return savedNote;
    }
}
