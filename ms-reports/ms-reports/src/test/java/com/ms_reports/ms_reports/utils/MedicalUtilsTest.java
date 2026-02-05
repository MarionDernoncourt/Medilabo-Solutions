package com.ms_reports.ms_reports.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MedicalUtilsTest {

    @Test
    @DisplayName("L'âge doit être calculé correctement")
    void calculateAge_ShouldReturnCorrectAge() {
        LocalDate birthdate = LocalDate.now().minusYears(25);
        assertEquals(25, MedicalUtils.calculateAge(birthdate));
    }

    @Test
    @DisplayName("Doit confirmer si le patient a 30 ans ou plus")
    void patientIs30OrMore_ShouldReturnTrue_When30() {
        LocalDate birthdateAt30 = LocalDate.now().minusYears(30);
        LocalDate birthdateAt40 = LocalDate.now().minusYears(40);

        assertTrue(MedicalUtils.patientIs30OrMore(birthdateAt30));
        assertTrue(MedicalUtils.patientIs30OrMore(birthdateAt40));
    }

    @Test
    @DisplayName("Doit nettoyer le texte (accents, majuscules, espaces)")
    void simplifyText_ShouldNormalizeString() {
        String input = "  Hémoglobine A1C  ";
        String expected = "hemoglobine a1c";

        assertEquals(expected, MedicalUtils.simplifyText(input));
    }

    @Test
    @DisplayName("Doit gérer les cas de texte nul")
    void simplifyText_ShouldReturnEmpty_WhenNull() {
        assertEquals("", MedicalUtils.simplifyText(null));
    }
}
