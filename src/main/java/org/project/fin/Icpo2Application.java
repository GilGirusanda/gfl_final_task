package org.project.fin;

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

//    @Bean
//    public CommandLineRunner cmd(CriminalService criminalService){
//        return args -> {
//
//            Criminal criminal = Criminal.builder(
//                    "John",
//                    new SpecialSigns(1.8f),
//                    CriminalProfession.BURGLAR,
//                    "A man stole a blanket from the shop",
//                    LocalDate.of(1997, 7, 1),
//                    Citizenship.Canada).build();
//
//            boolean isSuccess = criminalService.save(criminal);
//            System.out.println("Saved: " + isSuccess);
//
//            criminalService.findAll().forEach(System.out::println);
//        };
//    }

}
