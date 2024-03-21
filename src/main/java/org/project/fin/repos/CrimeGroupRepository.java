package org.project.fin.repos;

import org.project.fin.models.CrimeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrimeGroupRepository extends JpaRepository<CrimeGroup, Long> {
}
