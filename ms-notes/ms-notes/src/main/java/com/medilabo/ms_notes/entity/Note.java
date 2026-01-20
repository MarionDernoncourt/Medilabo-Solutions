package com.medilabo.ms_notes.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    private String id;
    @NotNull
    private Integer patientId;
    @NotBlank(message = "Le contenu de la note ne peut pas être vide")
    private String note;
    @CreatedDate
    private LocalDateTime createdAt;

}
