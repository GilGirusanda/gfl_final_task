package org.project.fin.repos;

import org.project.fin.models.CrimeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrimeGroupRepository extends JpaRepository<CrimeGroup, Long> {
    Optional<CrimeGroup> findByGroupNameIgnoreCase(String groupName);

    @Query("SELECT c.id FROM Criminal c JOIN c.crimeGroups cg WHERE cg.groupName = :groupNameParam")
    List<Long> findCriminalIdsByCrimeGroupNameStrict(@Param("groupNameParam") String groupName);

    @Query("SELECT c.id FROM Criminal c JOIN c.crimeGroups cg WHERE LOWER(cg.groupName) like LOWER(CONCAT('%', :groupNameParam, '%'))")
    List<Long> findCriminalIdsByCrimeGroupNameNotStrict(@Param("groupNameParam") String groupName);
}
