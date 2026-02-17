package com.medilabo.ms_notes.config;

import com.medilabo.ms_notes.entity.Note;
import com.medilabo.ms_notes.repository.INoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(INoteRepository repository) {
        return args -> {
            repository.deleteAll();
            System.out.println(">>> MongoDB: Nettoyage de la base effectué.");

            repository.saveAll(Arrays.asList(

                    // Patient 1
                    new Note(null, 1, "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé", LocalDateTime.now()),

                    // Patient 2
                    new Note(null, 2, "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement", LocalDateTime.now()),
                    new Note(null, 2, "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale", LocalDateTime.now()),

                    // Patient 3
                    new Note(null, 3, "Le patient déclare qu'il fume depuis peu", LocalDateTime.now()),
                    new Note(null, 3, "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé", LocalDateTime.now()),

                    // Patient 4
                    new Note(null, 4, "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments", LocalDateTime.now()),
                    new Note(null, 4, "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", LocalDateTime.now()),
                    new Note(null, 4, "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé", LocalDateTime.now()),
                    new Note(null, 4, "Taille, Poids, Cholestérol, Vertige et Réaction", LocalDateTime.now())
            ));

            System.out.println(">>> MongoDB : Notes de test chargées avec succès !");


        };
    }
}