package com.medilabo.ms_notes.service;

import com.medilabo.ms_notes.dto.NoteDTO;
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
            throw new PatientNotFoundException("Aucun patient trouvé avec cet id : " + patientId);
        } catch (FeignException e) {
            throw new PatientServiceException("Erreur de communication entre les différents services");
        }
    }

    @Override
    public List<NoteDTO> getNotesByPatientId(Integer patientId) {
        logger.debug("Trying to get Notes for patient with id: " + patientId);
       validatePatientExists(patientId);
        List<Note> notes =  noteRepository.findNotesByPatientId(patientId);
        if(notes.isEmpty()){
            logger.warn("Not notes found for this patient.");
            return Collections.emptyList();
        }
        return notes.stream().map(this::mapToDTO).toList();
    }

    @Override
    public NoteDTO save(NoteDTO note) {
        logger.debug("Trying to save new note for patient: {}", note.getPatientId());
        validatePatientExists(note.getPatientId());
        Note noteToSaved = mapToEntity(note);
        Note savedNote= noteRepository.save(noteToSaved);
        logger.info("Note saved with id: {} ", savedNote.getId());
        return mapToDTO(savedNote);
    }

    // Convertit une Entité Note en NoteDTO
    private NoteDTO mapToDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setPatientId(note.getPatientId());
        dto.setNote(note.getNote());
        dto.setCreatedAt(note.getCreatedAt());
        return dto;
    }

    // Convertit un NoteDTO en Entité Note
    private Note mapToEntity(NoteDTO dto) {
        Note entity = new Note();
        entity.setId(dto.getId());
        entity.setPatientId(dto.getPatientId());
        entity.setNote(dto.getNote());
        return entity;
    }
}
