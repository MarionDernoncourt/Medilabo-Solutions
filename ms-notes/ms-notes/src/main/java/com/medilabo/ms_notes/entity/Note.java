package com.medilabo.ms_notes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {


}
