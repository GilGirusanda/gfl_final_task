package org.project.fin.repos;

import jakarta.persistence.Tuple;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.CriminalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriminalDetailsRepository extends JpaRepository<CriminalDetails, Long> {
//    @Query(name="findCriminalsByAttributes_Named_Query", nativeQuery = true)
//    List<CriminalDetailsDTO> findCriminalsByAttributes_Named(
//            @Param("eyeColor") String eyeColor,
//            @Param("hairColor") String hairColor,
//            @Param("height") String height,
//            @Param("birthPlace") String birthPlace,
//            @Param("birthDate") String birthDate,
//            @Param("lastResidence") String lastResidence,
//            @Param("citizenship") String citizenship
//    );

//    @Query(name="SELECT ct.rowid as criminal_id, " +
//            "ct.EYE_COLOR, " +
//            "ct.HAIR_COLOR, " +
//            "ct.HEIGHT, " +
//            "ct.BIRTH_PLACE, " +
//            "TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') as BIRTH_DATE, " +
//            "ct.LAST_RESIDENCE, " +
//            "ct.CITIZENSHIP " +
//            "FROM crosstab( " +
//            "'SELECT criminal_id, attribute_type, attribute_value " +
//            "FROM criminal_details " +
//            "ORDER BY 1, 2', " +
//            "$$VALUES (cast('EYE_COLOR' as varchar)), " +
//            "('HAIR_COLOR'), " +
//            "('HEIGHT'), " +
//            "('BIRTH_PLACE'), " +
//            "('BIRTH_DATE'), " +
//            "('LAST_RESIDENCE'), " +
//            "('CITIZENSHIP') $$) " +
//            "AS ct(rowid bigint, EYE_COLOR varchar, HAIR_COLOR varchar, HEIGHT varchar, BIRTH_PLACE varchar, BIRTH_DATE varchar, LAST_RESIDENCE varchar, CITIZENSHIP varchar) " +
//            "WHERE (:eyeColor IS NULL OR ct.EYE_COLOR = :eyeColor) " +
//            "AND (:hairColor IS NULL OR ct.HAIR_COLOR = :hairColor) " +
//            "AND (:height IS NULL OR ct.HEIGHT = :height) " +
//            "AND (:birthPlace IS NULL OR ct.BIRTH_PLACE = :birthPlace) " +
//            "AND (:birthDate IS NULL OR TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') = TO_TIMESTAMP(:birthDate, 'YYYY-MM-DD')) " +
//            "AND (:lastResidence IS NULL OR ct.LAST_RESIDENCE = :lastResidence) " +
//            "AND (:citizenship IS NULL OR ct.CITIZENSHIP = :citizenship)", nativeQuery = true)
//    List<Tuple> findCriminalsByAttrCrosstab_Named_2(
//            @Param("eyeColor") String eyeColor,
//            @Param("hairColor") String hairColor,
//            @Param("height") String height,
//            @Param("birthPlace") String birthPlace,
//            @Param("birthDate") String birthDate,
//            @Param("lastResidence") String lastResidence,
//            @Param("citizenship") String citizenship
//    );

    @Query(name="findCriminalsByAttributes_Named_Query", nativeQuery = true)
    List<Tuple> findCriminals_by_Attr_Crosstab_Named_2(
            @Param("eyeColor") String eyeColor,
            @Param("hairColor") String hairColor,
            @Param("height") String height,
            @Param("birthPlace") String birthPlace,
            @Param("birthDate") String birthDate,
            @Param("lastResidence") String lastResidence,
            @Param("citizenship") String citizenship
    );

    @Query(nativeQuery = true, value = "SELECT * FROM crim_details_crosstab(:eyeColor, :hairColor, :height, :birthPlace, :birthDate, :lastResidence, :citizenship)")
    List<CriminalDetailsDTO> findByAttributesFunc(
            @Param("eyeColor") String eyeColor,
            @Param("hairColor") String hairColor,
            @Param("height") String height,
            @Param("birthPlace") String birthPlace,
            @Param("birthDate") String birthDate,
            @Param("lastResidence") String lastResidence,
            @Param("citizenship") String citizenship
    );
}
