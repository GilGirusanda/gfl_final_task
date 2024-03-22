package org.project.fin.config;

import org.project.fin.DTO.CriminalDTO;
import org.project.fin.models.Criminal;
import org.project.fin.utils.mapper.criminal.CriminalMapperImpl;
import org.project.fin.utils.mapper.Mapper;
import org.project.fin.utils.mapper.criminalDetails.CriminalDetailsMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CriminalMapperImpl criminalMapper() {
        return new CriminalMapperImpl();
    }
    @Bean
    public CriminalDetailsMapperImpl criminalDetailsMapper() {
        return new CriminalDetailsMapperImpl();
    }
}
