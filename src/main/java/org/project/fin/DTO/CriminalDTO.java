package org.project.fin.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.project.fin.models.Archive;
import org.project.fin.models.CrimeGroup;
import org.project.fin.models.CriminalDetails;
import org.project.fin.models.Language;
import org.project.fin.models.enums.CriminalProfession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriminalDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String nickname;
    private CriminalProfession criminalProfession;

    public boolean isEmpty() {
        return id == null &&
                lastName == null &&
                firstName == null &&
                nickname == null &&
                criminalProfession == null;
    }
}
