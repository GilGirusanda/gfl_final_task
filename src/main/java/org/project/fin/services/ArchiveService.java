package org.project.fin.services;

import lombok.AllArgsConstructor;
import org.project.fin.models.Criminal;
import org.project.fin.repos.ArchiveRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArchiveService {

    private ArchiveRepository archiveRepository;

    public List<Criminal> filterArchivedCriminals(List<Criminal> criminals, Boolean isArchived) {
        if (isArchived != null && isArchived) {
            HashSet<Long> criminalIds = criminals.stream()
                    .map(Criminal::getId)
                    .collect(Collectors.toCollection(HashSet::new));

            HashSet<Long> archivedCriminalIds = archiveRepository.findArchivedCriminalIds(criminalIds);

            return criminals.stream()
                    .filter(c -> archivedCriminalIds.contains(c.getId()))
                    .collect(Collectors.toList());
        } else {
            return criminals;
        }
    }
}
