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
    List<CriminalDTO> findAllQuery();

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
            "AND c.criminalProfession = :criminalProfession")
    List<CriminalDTO> findCriminalsByCriminalInfoNotStrict(@Param("firstName") String firstName,
                                                  @Param("lastName") String lastName,
                                                  @Param("nickname") String nickname,
                                                  @Param("criminalProfession") CriminalProfession criminalProfession);

//    @Query("SELECT c FROM Criminal c LEFT JOIN FETCH c.archive WHERE c.id = :id")
//    Optional<Criminal> findByIdWithArchive(@Param("id") Long id);
}
