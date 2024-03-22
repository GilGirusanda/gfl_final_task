package org.project.fin.DTO;

import org.project.fin.models.Criminal;

public class CriminalSearchDTO {
    private CriminalDTO criminalDto = new CriminalDTO();
    private CriminalDetailsDTO criminalDetailsDto = new CriminalDetailsDTO();

    public CriminalSearchDTO() {
    }

    public CriminalDTO getCriminalDto() {
        return criminalDto;
    }

    public void setCriminalDto(CriminalDTO criminalDto) {
        this.criminalDto = criminalDto;
    }

    public CriminalDetailsDTO getCriminalDetailsDto() {
        return criminalDetailsDto;
    }

    public void setCriminalDetailsDto(CriminalDetailsDTO criminalDetailsDto) {
        this.criminalDetailsDto = criminalDetailsDto;
    }
}
