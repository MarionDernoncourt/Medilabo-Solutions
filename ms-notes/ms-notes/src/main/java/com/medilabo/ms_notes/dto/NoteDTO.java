package com.medilabo.ms_notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {

    private String id;

    @NotNull(message = "Merci de renseigner un patientId existant.")
    private Integer patientId;

    @NotBlank(message = "Le contenu de la note ne peut pas être vide")
    private String note;

    @PastOrPresent(message = "La date de création ne peut pas être dans le futur")
    private LocalDateTime createdAt;
}