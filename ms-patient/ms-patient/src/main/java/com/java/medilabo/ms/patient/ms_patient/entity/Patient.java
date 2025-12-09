package com.java.medilabo.ms.patient.ms_patient.entity;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "last_name", nullable = false)
    private String lastname;
    @Column(name = "first_name", nullable = false)
    private String firstname;
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;
    @Column(name = "address", nullable = true)
    private String address;
    @Column(name = "phone_number", nullable = true, unique = true)
    private String phoneNumber;


    public Patient() {}

    public Patient(Integer id, String lastname, String firstname, LocalDate birthdate, Genre genre, String address, String phoneNumber) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.genre = genre;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Patient(PatientDTO dto) {
        this.lastname = dto.getLastname();
        this.firstname = dto.getFirstname();
        this.birthdate = dto.getBirthdate();
        this.genre = dto.getGenre();
        this.address = dto.getAddress();
        this.phoneNumber = dto.getPhoneNumber();
    }

    public void updateFromDto(PatientDTO dto) {
        if (dto.getFirstname() != null) {
            this.firstname = dto.getFirstname();
        }
        if (dto.getLastname() != null) {
            this.lastname = dto.getLastname();
        }
        if (dto.getBirthdate() != null) {
            this.birthdate = dto.getBirthdate();
        }
        if (dto.getGenre() != null) {
            this.genre = dto.getGenre();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        if (dto.getPhoneNumber() != null) {
            this.phoneNumber = dto.getPhoneNumber();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
