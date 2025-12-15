package com.java.medilabo.ms.patient.ms_patient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // 1. Collecter les erreurs
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        // 2. Structurer la réponse JSON
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Bad Request");
        errorDetails.put("message", "Erreur de validation des champs.");
        errorDetails.put("details", fieldErrors); // Contient la liste des erreurs par champ

        // 3. Retourner la réponse 400
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    // ---------------------- 2. NOUVEAU : GESTION 404 (Patient Non Trouvé) ----------------------
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePatientNotFoundException(PatientNotFoundException e) {

        logger.warn("Ressource non trouvée (404) : {}", e.getMessage());

        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("error", "Not Found");
        errorDetails.put("message", e.getMessage()); // Le message de l'exception métier

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // ---------------------- 3. NOUVEAU : GESTION 409 (Conflit / Doublon) ----------------------
    @ExceptionHandler(PatientAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handlePatientAlreadyExistException(PatientAlreadyExistException e) {

        logger.warn("Conflit de données (409) : {}", e.getMessage());

        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("error", "Conflict");
        errorDetails.put("message", e.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }


        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGenericException (Exception e){
            // Log de l'erreur critique avec la stack trace
            logger.error("Erreur critique inattendue : {}", e.getMessage(), e);

            Map<String, Object> errorDetails = new LinkedHashMap<>();
            errorDetails.put("timestamp", LocalDateTime.now().toString());
            errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorDetails.put("error", "Internal Server Error");
            errorDetails.put("message", "Une erreur interne inattendue est survenue sur le serveur.");

            // C'est le statut retourné au front-end
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
