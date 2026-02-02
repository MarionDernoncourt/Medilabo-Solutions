package com.ms_reports.ms_reports.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.time.LocalDate;

public class MedicalUtils {

    private static final Logger logger = LoggerFactory.getLogger(MedicalUtils.class);

    private MedicalUtils() {}

    public static int calculateAge(LocalDate birthdate) {
        logger.debug("Trying to calculate age for patient birthdate : " + birthdate);
        return java.time.Period.between(birthdate, LocalDate.now()).getYears();
    };

    public static boolean patientIs30OrMore(LocalDate birthdate) {
        logger.debug("Trying to find is patient is more than 30 years: " + birthdate);
        return calculateAge(birthdate) >= 30;
    }

    public static String simplifyText(String text) {
        if (text == null) return "";
        return Normalizer.normalize(text, Normalizer.Form.NFD) // Separe l'accent de la lettre
                .replaceAll("\\p{M}", "") // supprime l'accent seul
                .toLowerCase() // en minuscule
                .trim(); // suppression des espaces vides en début ou fin de mot
    }

}
