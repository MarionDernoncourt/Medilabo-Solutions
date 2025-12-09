package com.java.medilabo.ms.patient.ms_patient.service;

import com.java.medilabo.ms.patient.ms_patient.dto.PatientDTO;
import com.java.medilabo.ms.patient.ms_patient.entity.Patient;

import java.util.List;

public interface IPatientService {
    List<PatientDTO> getAllPatients();

    PatientDTO getPatientById(Integer id);

    Patient createPatient(PatientDTO patient);

    PatientDTO updatePatient(Integer id, PatientDTO patient);
}
