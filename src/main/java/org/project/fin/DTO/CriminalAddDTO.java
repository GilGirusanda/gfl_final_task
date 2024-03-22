package org.project.fin.DTO;

public class CriminalAddDTO {
    private CriminalDTO criminalDto = new CriminalDTO();
    private CriminalDetailsDTO criminalDetailsDto = new CriminalDetailsDTO();

    public CriminalAddDTO() {
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