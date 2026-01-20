package com.medilabo.ms_notes.service;

import com.medilabo.ms_notes.entity.Note;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INoteService {
    List<Note> getNotesByPatientId(Integer patientId);
    Note save(Note note);
}
