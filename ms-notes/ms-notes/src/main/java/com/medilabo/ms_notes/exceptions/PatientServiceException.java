package com.medilabo.ms_notes.exceptions;

public class PatientServiceException extends RuntimeException {
    public PatientServiceException(String message)
    {
        super(message);
    }
}
