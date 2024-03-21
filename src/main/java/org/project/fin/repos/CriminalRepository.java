package org.project.fin.repos;

import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.Criminal;
import org.project.fin.models.CriminalDetails;
import org.project.fin.models.enums.CriminalProfession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CriminalRepository extends JpaRepository<Criminal, Long> {

    @Query(value = "SELECT new org.project.fin.DTO.CriminalDTO(c.id, c.lastName, c.firstName, c.nickname, c.criminalProfession) FROM Criminal c")
    public List<CriminalDTO> findAllQuery();

//    @Query(value = "SELECT ct.rowid as criminal_id, " +
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
//            "AND (:citizenship IS NULL OR ct.CITIZENSHIP = :citizenship)",
//            nativeQuery = true)
//    List<CriminalDetailsDTO> findCriminalsByAttributes(
//            String eyeColor,
//            String hairColor,
//            String height,
//            String birthPlace,
//            String birthDate,
//            String lastResidence,
//            String citizenship
//    );

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

    List<Criminal> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT new org.project.fin.DTO.CriminalDTO(c.id, c.lastName, c.firstName, c.nickname, c.criminalProfession) FROM Criminal c "+
            "WHERE c.firstName=:firstName " +
            "AND c.lastName=:lastName " +
            "AND c.nickname=:nickname " +
            "AND c.criminalProfession=:criminalProfession ")
    List<CriminalDTO> findCriminalsByCriminalInfoStrict(@Param("firstName") String firstName,
                                                  @Param("lastName") String lastName,
                                                  @Param("nickname") String nickname,
                                                  @Param("criminalProfession") CriminalProfession criminalProfession);

    @Query(value = "SELECT new org.project.fin.DTO.CriminalDTO(c.id, c.lastName, c.firstName, c.nickname, c.criminalProfession) FROM Criminal c "+
            "WHERE LOWER(c.firstName) LIKE CONCAT('%', LOWER(:firstName), '%') " +
            "AND LOWER(c.lastName) LIKE CONCAT('%', LOWER(:lastName), '%') " +
            "AND LOWER(c.nickname) LIKE CONCAT('%', LOWER(:nickname), '%') " +
            "AND LOWER(c.criminalProfession) LIKE CONCAT('%', LOWER(:criminalProfession), '%')")
    List<CriminalDTO> findCriminalsByCriminalInfoNotStrict(@Param("firstName") String firstName,
                                                  @Param("lastName") String lastName,
                                                  @Param("nickname") String nickname,
                                                  @Param("criminalProfession") CriminalProfession criminalProfession);

//    @Query("SELECT c FROM Criminal c LEFT JOIN FETCH c.archive WHERE c.id = :id")
//    Optional<Criminal> findByIdWithArchive(@Param("id") Long id);
}
