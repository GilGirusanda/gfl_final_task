package org.project.fin.utils.mapper.criminalDetails;

import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.Criminal;
import org.project.fin.models.CriminalDetails;
import org.project.fin.models.enums.AttributeType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CriminalDetailsMapperImpl implements CriminalDetailsMapper {
    @Override
    public CriminalDetailsDTO toDTO(List<CriminalDetails> entities) {
        // if Criminal's id is different for any detail - return
        CriminalDetails firstDetail = entities.get(0);
        if(entities.stream()
                .anyMatch(detail -> detail.getCriminal().getId()
                .equals(firstDetail.getCriminal().getId()))) {
            return null;
        }

        CriminalDetailsDTO dto = new CriminalDetailsDTO();

        for (CriminalDetails detail : entities) {
            if (detail != null) {
                switch (detail.getAttributeType()) {
                    case EYE_COLOR:
                        dto.setEyeColor(detail.getAttributeValue());
                        break;
                    case HAIR_COLOR:
                        dto.setHairColor(detail.getAttributeValue());
                        break;
                    case HEIGHT:
                        dto.setHeight(Float.parseFloat(detail.getAttributeValue()));
                        break;
                    case BIRTH_PLACE:
                        dto.setBirthPlace(detail.getAttributeValue());
                        break;
                    case BIRTH_DATE:
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate birthDate = LocalDate.parse(detail.getAttributeValue(), formatter);
                        dto.setBirthDate(birthDate);
                        break;
                    case LAST_RESIDENCE:
                        dto.setLastResidence(detail.getAttributeValue());
                        break;
                    case CITIZENSHIP:
                        dto.setCitizenship(detail.getAttributeValue());
                        break;
                    default:
                        // Handle unknown attribute type
                        break;
                }
            }
        }
//        dto.setCriminalId();
//        dto.setEyeColor();
//        dto.setHairColor();
//        dto.setHeight();
//        dto.setBirthPlace();
//        dto.setBirthDate();
//        dto.setLastResidence();
//        dto.setCitizenship();
        return dto;
    }

    @Override
    public List<CriminalDetails> toEntity(CriminalDetailsDTO dto) {
        List<CriminalDetails> entities = new ArrayList<>();

        // Create CriminalDetails objects for each attribute in the DTO
        if (dto != null) {
            if (dto.getEyeColor() != null) {
                CriminalDetails eyeColorDetail = new CriminalDetails();
                eyeColorDetail.setAttributeType(AttributeType.EYE_COLOR);
                eyeColorDetail.setAttributeValue(dto.getEyeColor());
                entities.add(eyeColorDetail);
            }
            if (dto.getHairColor() != null) {
                CriminalDetails hairColorDetail = new CriminalDetails();
                hairColorDetail.setAttributeType(AttributeType.HAIR_COLOR);
                hairColorDetail.setAttributeValue(dto.getHairColor());
                entities.add(hairColorDetail);
            }
            if (dto.getHeight() != null) {
                CriminalDetails heightDetail = new CriminalDetails();
                heightDetail.setAttributeType(AttributeType.HEIGHT);
                heightDetail.setAttributeValue(String.valueOf(dto.getHeight()));
                entities.add(heightDetail);
            }
            if (dto.getBirthPlace() != null) {
                CriminalDetails birthPlaceDetail = new CriminalDetails();
                birthPlaceDetail.setAttributeType(AttributeType.BIRTH_PLACE);
                birthPlaceDetail.setAttributeValue(dto.getBirthPlace());
                entities.add(birthPlaceDetail);
            }
            if (dto.getBirthDate() != null) {
                CriminalDetails birthDateDetail = new CriminalDetails();
                birthDateDetail.setAttributeType(AttributeType.BIRTH_DATE);
                birthDateDetail.setAttributeValue(String.valueOf(dto.getBirthDate()));
                entities.add(birthDateDetail);
            }
            if (dto.getLastResidence() != null) {
                CriminalDetails lastResidenceDetail = new CriminalDetails();
                lastResidenceDetail.setAttributeType(AttributeType.LAST_RESIDENCE);
                lastResidenceDetail.setAttributeValue(dto.getLastResidence());
                entities.add(lastResidenceDetail);
            }
            if (dto.getCitizenship() != null) {
                CriminalDetails citizenshipDetail = new CriminalDetails();
                citizenshipDetail.setAttributeType(AttributeType.CITIZENSHIP);
                citizenshipDetail.setAttributeValue(dto.getCitizenship());
                entities.add(citizenshipDetail);
            }
        }

        entities.stream()
                .map(e -> {
                    Criminal cr = new Criminal();
                    cr.setId(dto.getCriminalId());
                    e.setCriminal(cr);
                    return e;
                })
                .collect(Collectors.toList());

        return entities;
    }
}
