package org.project.fin.config;

import org.project.fin.DTO.CriminalDTO;
import org.project.fin.models.Criminal;
import org.project.fin.utils.mapper.criminal.CriminalMapperImpl;
import org.project.fin.utils.mapper.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Mapper<Criminal, CriminalDTO> criminalMapper() {
        return new CriminalMapperImpl();
    }
}
