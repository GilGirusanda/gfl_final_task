package org.project.fin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.enums.AttributeType;
import org.hibernate.annotations.NamedNativeQuery;

import java.time.LocalDate;
import java.util.*;

@NamedNativeQuery(
        name = "findCriminalsByAttributes_Named_Query",
        query = "SELECT ct.rowid as criminal_id, " +
                "ct.EYE_COLOR, " +
                "ct.HAIR_COLOR, " +
                "CAST(ct.HEIGHT AS FLOAT), " +
                "ct.BIRTH_PLACE, " +
                "TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') as BIRTH_DATE, " +
                "ct.LAST_RESIDENCE, " +
                "ct.CITIZENSHIP " +
                "FROM crosstab( " +
                "'SELECT criminal_id, attribute_type, attribute_value " +
                "FROM criminal_details " +
                "ORDER BY 1, 2', " +
                "$$VALUES (cast('EYE_COLOR' as varchar)), " +
                "('HAIR_COLOR'), " +
                "('HEIGHT'), " +
                "('BIRTH_PLACE'), " +
                "('BIRTH_DATE'), " +
                "('LAST_RESIDENCE'), " +
                "('CITIZENSHIP') $$) " +
                "AS ct(rowid bigint, EYE_COLOR varchar, HAIR_COLOR varchar, HEIGHT varchar, BIRTH_PLACE varchar, BIRTH_DATE varchar, LAST_RESIDENCE varchar, CITIZENSHIP varchar) " +
                "WHERE (:eyeColor IS NULL OR ct.EYE_COLOR = :eyeColor) " +
                "AND (:hairColor IS NULL OR ct.HAIR_COLOR = :hairColor) " +
                "AND (:height IS NULL OR CAST(ct.HEIGHT AS FLOAT) = CAST(:height AS FLOAT)) " +
                "AND (:birthPlace IS NULL OR ct.BIRTH_PLACE = :birthPlace) " +
                "AND (:birthDate IS NULL OR TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') = TO_TIMESTAMP(:birthDate, 'YYYY-MM-DD')) " +
                "AND (:lastResidence IS NULL OR ct.LAST_RESIDENCE = :lastResidence) " +
                "AND (:citizenship IS NULL OR ct.CITIZENSHIP = :citizenship)",
        resultSetMapping = "CriminalDetailsDTOMapping"
)
@SqlResultSetMapping(
        name = "CriminalDetailsDTOMapping",
        classes = @ConstructorResult(
                targetClass = CriminalDetailsDTO.class,
                columns = {
                        @ColumnResult(name = "criminal_id", type = Long.class),
                        @ColumnResult(name = "EYE_COLOR", type = String.class),
                        @ColumnResult(name = "HAIR_COLOR", type = String.class),
                        @ColumnResult(name = "HEIGHT", type = Float.class),
                        @ColumnResult(name = "BIRTH_PLACE", type = String.class),
                        @ColumnResult(name = "BIRTH_DATE", type = LocalDate.class),
                        @ColumnResult(name = "LAST_RESIDENCE", type = String.class),
                        @ColumnResult(name = "CITIZENSHIP", type = String.class)
                }
        )
)
@Getter
@Setter
@Entity
@Table(name = "criminal_details")
@AllArgsConstructor
@NoArgsConstructor
public class CriminalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType; // e.g., height, hair color, etc.

    @Column(name = "attribute_value", nullable = false)
    private String attributeValue;

    @ManyToOne
    @JoinColumn(name = "criminal_id")
    private Criminal criminal;

}
