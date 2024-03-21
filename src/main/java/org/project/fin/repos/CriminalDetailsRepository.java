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

    @Query(name="findCriminalsByAttributes_Named_Query", nativeQuery = true)
    List<CriminalDetailsDTO> findCriminals_by_Attr_Crosstab_Named_2(
            @Param("eyeColor") String eyeColor,
            @Param("hairColor") String hairColor,
            @Param("height") String height,
            @Param("birthPlace") String birthPlace,
            @Param("birthDate") String birthDate,
            @Param("lastResidence") String lastResidence,
            @Param("citizenship") String citizenship
    );

}
