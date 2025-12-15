package com.java.medilabo.ms.patient.ms_patient.dto;

import com.java.medilabo.ms.patient.ms_patient.entity.Genre;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PatientDTO {



    @NotBlank
    @Pattern(regexp = "^[\\p{L}\\s`]+$", message = "Le prénom doit contenir uniquement de lettres, espace ou tiret.")
    @Size(min=1, max = 50, message = "Le prénom ne doit pas dépasser 50 caractères.")
    private String firstname;
    @NotBlank
    @Pattern(regexp = "^[\\p{L}\\s`]+$", message = "Le nom doit contenir uniquement de lettres, espace ou tiret.")
    @Size(min=1, max = 50, message = "Le nom ne doit pas dépasser 50 caractères.")
    private String lastname;
    @NotNull
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate birthdate;
    @NotNull(message = "Le genre est obligatoire")
    private Genre genre;
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères.")
    private String address;
    @Pattern(regexp = "^$|^[0-9\\s()+\\-.]{7,20}$",
            message = "Le numéro de téléphone est invalide. Il doit être entre 7 et 20 caractères et contenir des chiffres, espaces, +, -, . ou parenthèses.")
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères.")
    private String phoneNumber;

    public PatientDTO() {
    }

    public PatientDTO(Patient patient) {
        this.firstname = patient.getFirstname();
        this.lastname = patient.getLastname();
        this.birthdate = patient.getBirthdate();
        this.genre = patient.getGenre();
        this.address = patient.getAddress();
        this.phoneNumber = patient.getPhoneNumber();
    }

    public PatientDTO(String firstname, String lastname, LocalDate birthdate, Genre genre, String address, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.genre = genre;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
