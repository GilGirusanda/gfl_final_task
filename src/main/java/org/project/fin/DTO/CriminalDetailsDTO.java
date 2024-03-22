package org.project.fin.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CriminalDetailsDTO {
    private Long criminalId;
    String eyeColor;
    String hairColor;
    Float height;
    String birthPlace;
    LocalDate birthDate;
    String lastResidence;
    String citizenship;

    public boolean isEmpty() {
        return criminalId == null &&
                eyeColor == null &&
                hairColor == null &&
                height == null &&
                birthPlace == null &&
                birthDate == null &&
                lastResidence == null &&
                citizenship == null;
    }

//    public CriminalDetailsDTO(Long criminalId, String eyeColor, String hairColor, String height, String birthPlace, String birthDate, String lastResidence, String citizenship) {
//        this.criminalId = criminalId;
//        this.eyeColor = eyeColor;
//        this.hairColor = hairColor;
//        this.height = height;
//        this.birthPlace = birthPlace;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = null;
//        if(birthDate != null) localDate = LocalDate.parse(birthDate, formatter);
//        this.birthDate = localDate;
//        this.lastResidence = lastResidence;
//        this.citizenship = citizenship;
//    }
}
