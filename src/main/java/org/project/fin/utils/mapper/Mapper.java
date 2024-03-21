package org.project.fin.utils.mapper;

import org.project.fin.DTO.CriminalDTO;
import org.project.fin.models.Criminal;

public interface Mapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);
}
