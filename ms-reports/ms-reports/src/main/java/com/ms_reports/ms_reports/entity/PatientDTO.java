package com.ms_reports.ms_reports.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    private Integer id;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String genre;
}
