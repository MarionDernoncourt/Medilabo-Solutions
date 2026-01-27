package com.medilabo.ms_notes.repository;

import com.medilabo.ms_notes.dto.NoteDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.medilabo.ms_notes.entity.Note;

import java.util.List;

@Repository
public interface INoteRepository extends MongoRepository<Note, String> {
    List<Note> findByPatientIdOrderByCreatedAtDesc(Integer patientId);


}
