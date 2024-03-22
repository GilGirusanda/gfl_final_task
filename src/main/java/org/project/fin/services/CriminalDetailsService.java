package org.project.fin.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.Criminal;
import org.project.fin.models.CriminalDetails;
import org.project.fin.repos.CriminalDetailsRepository;
import org.project.fin.repos.CriminalRepository;
import org.project.fin.utils.mapper.Mapper;
import org.project.fin.utils.mapper.criminalDetails.CriminalDetailsMapperImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class CriminalDetailsService {
    private CriminalDetailsRepository criminalDetailsRepository;
    private CriminalDetailsMapperImpl criminalDetailsDTOMapper;

    @PersistenceContext
    private EntityManager em;

    public boolean save(CriminalDetailsDTO criminalDetailsDTO) {
        try {
            List<CriminalDetails> detailsToSave = criminalDetailsDTOMapper.toEntity(criminalDetailsDTO);
            criminalDetailsRepository.saveAll(detailsToSave);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
}
