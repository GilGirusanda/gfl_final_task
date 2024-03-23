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

}
