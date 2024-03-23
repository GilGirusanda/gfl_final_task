package org.project.fin.services;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.StandardBasicTypes;
import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.CrimeGroup;
import org.project.fin.models.Criminal;
import org.project.fin.repos.CrimeGroupRepository;
import org.project.fin.repos.CriminalDetailsRepository;
import org.project.fin.repos.CriminalRepository;
import org.project.fin.utils.mapper.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CriminalService {

    private CriminalRepository criminalRepository;
    private CriminalDetailsRepository criminalDetailsRepository;
    private CrimeGroupRepository crimeGroupRepository;
    private Mapper<Criminal, CriminalDTO> criminalMapper;

    @PersistenceContext
    private EntityManager em;

    public List<Criminal> findAll() {
        return criminalRepository.findAll();
    }

    public List<CriminalDTO> findAllDTO() {
        return criminalRepository.findAllQuery();
    }

    public Criminal findById(long criminalId) {
        Optional<Criminal> criminalOpt = criminalRepository.findById(criminalId);
        return criminalOpt.orElse(null);
    }

    public Criminal save(Criminal criminal) {
        if(criminal.getFirstName().isBlank()) {
            criminal.setFirstName(null);
        }
        if(criminal.getLastName().isBlank()) {
            criminal.setLastName(null);
        }
        Criminal savedCriminal = criminalRepository.save(criminal);
        return savedCriminal;//.getId() > 0
    }

    @Transactional
    public boolean update(long criminalId, Criminal criminal) {
        Optional<Criminal> criminalOpt = criminalRepository.findById(criminalId);
        if(criminalOpt.isEmpty()) {
            return false;
        }
        // ensure we don't remove relationship between the Criminal and the Language inserted earlier
//        criminal.setCrimeGroups(criminalOpt.get().getCrimeGroups());

        // prevents DataIntegrityViolationException due to empty ArchiveDate field of HTML form
        if(criminal.getArchive().getArchiveDate() == null) {
            criminal.setArchive(null);
        } else {
            criminal.getArchive().setCriminal(criminalOpt.get());
        }

        criminalRepository.save(criminal);
        return true;
    }

//    @Transactional
//    public CrimeGroup addGroup(CrimeGroup crimeGroup) {
//        Optional<CrimeGroup> foundGroupOpt = crimeGroupRepository.findByGroupNameIgnoreCase(crimeGroup.getGroupName());
//        return foundGroupOpt.orElseGet(() -> crimeGroupRepository.save(crimeGroup));
//    }

    public void delete(long id) {
        criminalRepository.deleteById(id);
    }

    public List<Criminal> searchCriminalsByAttributes(CriminalDetailsDTO criminalDetailsDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBirthDate = null;
        if(criminalDetailsDTO.getBirthDate() != null)
            formattedBirthDate = criminalDetailsDTO.getBirthDate().format(formatter);

        List<CriminalDetailsDTO> cdDTOs = criminalDetailsRepository.findCriminals_by_Attr_Crosstab_Named_2(
                null != criminalDetailsDTO.getEyeColor() ? criminalDetailsDTO.getEyeColor(): null,
                null != criminalDetailsDTO.getHairColor() ? criminalDetailsDTO.getHairColor() : null,
                null != criminalDetailsDTO.getHeight() ? criminalDetailsDTO.getHeight() : null,
                null != criminalDetailsDTO.getBirthPlace() ? criminalDetailsDTO.getBirthPlace() : null,
                formattedBirthDate,
                null != criminalDetailsDTO.getLastResidence() ? criminalDetailsDTO.getLastResidence() : null,
                null != criminalDetailsDTO.getCitizenship() ? criminalDetailsDTO.getCitizenship() : null
        );

        List<Long> filteredCriminalIds = cdDTOs.stream().map(CriminalDetailsDTO::getCriminalId).collect(Collectors.toList());
        return criminalRepository.findAllByIdIn(filteredCriminalIds);
    }

    public List<CriminalDTO> searchCriminalsByCriminalInfoStrict(CriminalDTO criminalDTO) {
        List<CriminalDTO> filteredCriminals = criminalRepository.findCriminalsByCriminalInfoStrict(
                criminalDTO.getFirstName(),
                criminalDTO.getLastName(),
                criminalDTO.getNickname(),
                criminalDTO.getCriminalProfession()
        );
        return filteredCriminals;
    }

    public List<CriminalDTO> searchCriminalsByCriminalInfoNotStrict(CriminalDTO criminalDTO) {
//        String criminalProfession = criminalDTO.getCriminalProfession() != null ? criminalDTO.getCriminalProfession().toString() : null;
        List<CriminalDTO> filteredCriminals = criminalRepository.findCriminalsByCriminalInfoNotStrict(
                criminalDTO.getFirstName() != null ? criminalDTO.getFirstName() : "",
                criminalDTO.getLastName() != null ? criminalDTO.getLastName() : "",
                criminalDTO.getNickname() != null ? criminalDTO.getNickname() : "",
                criminalDTO.getCriminalProfession()
        );
        return filteredCriminals;
    }

}
