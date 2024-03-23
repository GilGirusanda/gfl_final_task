package org.project.fin.services;

import lombok.AllArgsConstructor;
import org.project.fin.models.CrimeGroup;
import org.project.fin.models.Criminal;
import org.project.fin.models.Language;
import org.project.fin.repos.CrimeGroupRepository;
import org.project.fin.repos.CriminalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CrimeGroupService {

    private CrimeGroupRepository crimeGroupRepository;
    private CriminalRepository criminalRepository;

    @Transactional
    public void addCriminalToGroup(Long criminalId, String groupName) {
        if(groupName.isBlank()) return;

        Criminal criminal = criminalRepository.findById(criminalId)
                .orElseThrow(() -> new RuntimeException("Criminal not found with id: " + criminalId));

        final boolean[] isNew = new boolean[1];

        CrimeGroup group = crimeGroupRepository.findByGroupNameIgnoreCase(groupName)
                .orElseGet(() -> {
                    CrimeGroup newSavedGroup = crimeGroupRepository.save(new CrimeGroup(null, groupName, null));
                    newSavedGroup.setMembers(new HashSet<>());
                    newSavedGroup.getMembers().add(criminal);
                    isNew[0] = true;
                    return newSavedGroup;
                });

//        criminal.getCrimeGroups().add(group);
        if(!isNew[0]) {
            group.getMembers().add(criminal);
        }

        crimeGroupRepository.save(group);
//        criminalRepository.save(criminal);
    }

    public List<Criminal> filterCriminalsByGroup(List<Criminal> criminals, String groupName) {
        if (!groupName.isBlank()) {
            HashSet<Long> criminalIds = criminals.stream()
                    .map(Criminal::getId)
                    .collect(Collectors.toCollection(HashSet::new));

            List<Long> groupedCriminalIds = crimeGroupRepository.findCriminalIdsByCrimeGroupNameNotStrict(groupName);

            return criminals.stream()
                    .filter(c -> groupedCriminalIds.contains(c.getId()))
                    .collect(Collectors.toList());
        } else {
            return criminals;
        }
    }
}
