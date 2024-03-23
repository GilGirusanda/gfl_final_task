package org.project.fin.repos;

import org.project.fin.models.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("select a.criminal.id from Archive a where a.criminal.id IN (:criminalIds)")
    HashSet<Long> findArchivedCriminalIds(@Param("criminalIds") HashSet<Long> criminalIds);
}
