package org.project.fin.utils.mapper.criminal;

import org.project.fin.DTO.CriminalDTO;
import org.project.fin.models.Criminal;

public class CriminalMapperImpl implements CriminalMapper {

    @Override
    public CriminalDTO toDTO(Criminal criminal) {
        CriminalDTO dto = new CriminalDTO();
        dto.setId(criminal.getId());
        dto.setFirstName(criminal.getFirstName());
        dto.setLastName(criminal.getLastName());
        dto.setNickname(criminal.getNickname());
        dto.setCriminalProfession(criminal.getCriminalProfession());
        return dto;
    }

    @Override
    public Criminal toEntity(CriminalDTO dto) {
        Criminal criminal = new Criminal();
        criminal.setId(dto.getId());
        criminal.setFirstName(dto.getFirstName());
        criminal.setLastName(dto.getLastName());
        criminal.setNickname(dto.getNickname());
        criminal.setCriminalProfession(dto.getCriminalProfession());
        return criminal;
    }
}

