package com.ms_reports.ms_reports.service;

import com.ms_reports.ms_reports.entity.NoteDTO;
import com.ms_reports.ms_reports.entity.PatientDTO;
import com.ms_reports.ms_reports.entity.RiskLevel;
import com.ms_reports.ms_reports.exceptions.NoteNotFoundException;
import com.ms_reports.ms_reports.exceptions.NoteServiceException;
import com.ms_reports.ms_reports.exceptions.PatientNotFoundException;
import com.ms_reports.ms_reports.exceptions.PatientServiceException;
import com.ms_reports.ms_reports.proxies.INotesProxy;
import com.ms_reports.ms_reports.proxies.IPatientProxy;
import com.ms_reports.ms_reports.utils.MedicalUtils;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private INotesProxy noteService;
    private IPatientProxy patientService;

    public ReportServiceImpl(INotesProxy noteService, IPatientProxy patientService) {
        this.noteService = noteService;
        this.patientService = patientService;

    }

    // Liste des déclencheurs
    private static final List<String> TRIGGERS = List.of(
            "Hémoglobine A1C", "Microalbumine", "Taille", "Poids",
            "Fume", "Anormal", "Cholestérol",
            "Vertige", "Rechute", "Réaction", "Anticorps"
    );

    //Récupération du patient
    public PatientDTO getPatientById(Integer id) {
        logger.info("Trying to get Patient with id " + id);
        try {
            PatientDTO patient  = patientService.getPatientById(id);
            System.out.println(patient);
            return patient;
        } catch (FeignException.NotFound e) {
            throw new PatientNotFoundException("Aucun patient trouvé avec cet id : " + id);
        } catch (FeignException e) {
            throw new PatientServiceException("Erreur de communication entre les services report et patient");
        }
    }

    //Récupération des notes du patient
    public List<NoteDTO> getNotesByPatientId(Integer id) {
        logger.debug("Trying to get notes by patientId : " + id);
        try {
            return noteService.getNotesByPatientId(id);
        } catch (FeignException.NotFound e) {
            throw new NoteNotFoundException("Aucune note trouvé pour le patient avec l'id : " + id);
        } catch (FeignException e) {
            throw new NoteServiceException("Erreur de communication entre les services report et notes");
        }
    }

    //Calcul du niveau de risque
    @Override
    public RiskLevel calculateRisk(Integer patientId) {
        logger.debug("Trying to calculate Risk level for patient with id : " + patientId);
        PatientDTO patient = getPatientById(patientId);
        System.out.println("Patient : " + patient);
        List<NoteDTO> notes = getNotesByPatientId(patientId);
        System.out.println("notes : " + notes);

        boolean patientMoreThan30 = MedicalUtils.patientIs30OrMore(patient.getBirthdate());
        String gender = patient.getGenre();
        long triggers = countTriggers(notes);

        return evaluateRiskLevel(patientMoreThan30, gender, triggers);
    }

    //Calcul du nombre de déclencheurs
    public long countTriggers(List<NoteDTO> notes) {
        logger.debug("Trying to count triggers for patient");
        if (notes == null || notes.isEmpty()) {
            logger.warn("No notes found");
            return 0;
        }

        // Conversion des notes en 1 seule note en nettoyant chaque mot de la liste
        String allNotesCleaned = notes.stream()
                .map(note -> MedicalUtils.simplifyText(note.getNote()))
                .collect(Collectors.joining(" "));

        System.out.println(allNotesCleaned);
        // Filtre les TRIGGERS en nettoyant chaque trigger de la liste
        long triggersCount = TRIGGERS.stream()
                .filter(trigger -> allNotesCleaned.contains(MedicalUtils.simplifyText(trigger)))
                .count();

        logger.info("Triggers found for patient : {}", triggersCount);
        return triggersCount;
    }

    public RiskLevel evaluateRiskLevel(boolean patientMoreThan30, String gender, long triggers) {
        boolean isMan = "MASCULIN".equalsIgnoreCase(gender);
        boolean isWoman = "FEMININ".equalsIgnoreCase(gender);

     if((isMan && !patientMoreThan30 && triggers >= 5) ||
             (isWoman && !patientMoreThan30 && triggers >= 7) ||
             (patientMoreThan30 && triggers >= 8)) {
         return RiskLevel.EARLY_ONSET;
     }
     if((isMan && !patientMoreThan30 && triggers >= 3) ||
             (isWoman && !patientMoreThan30 && triggers >= 4) ||
             (patientMoreThan30 && (triggers == 6 || triggers == 7))) {
         return RiskLevel.IN_DANGER;
     }
     if(patientMoreThan30 && (triggers >= 2 && triggers <=5 )) {
         return RiskLevel.BORDERLINE;
     }
    if(triggers == 0) {
        return RiskLevel.NONE;
    }
     return RiskLevel.NOT_APPLICABLE;
    }
}
