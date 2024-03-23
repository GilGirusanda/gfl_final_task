package org.project.fin.repos;

import org.project.fin.models.CrimeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrimeGroupRepository extends JpaRepository<CrimeGroup, Long> {
    Optional<CrimeGroup> findByGroupNameIgnoreCase(String groupName);
}
