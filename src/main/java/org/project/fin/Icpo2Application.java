package org.project.fin;

import org.project.fin.models.Language;
import org.project.fin.services.CriminalService;
import org.project.fin.services.LanguageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class Icpo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Icpo2Application.class, args);
    }

    @Bean
    public CommandLineRunner cmd(LanguageService languageService){
        return args -> {
            // Fill `languages` table with test values
            languageService.save(new Language(null, "Ukrainian", null));
            languageService.save(new Language(null, "English(UK)", null));
            languageService.save(new Language(null, "English(USA)", null));
            languageService.save(new Language(null, "French", null));
            languageService.save(new Language(null, "German", null));
            languageService.save(new Language(null, "Italian", null));
            languageService.save(new Language(null, "Portuguese", null));
            languageService.save(new Language(null, "Sweden", null));
            languageService.save(new Language(null, "Canadian", null));
        };
    }

}
