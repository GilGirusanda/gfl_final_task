package org.project.fin.services;

import lombok.AllArgsConstructor;
import org.project.fin.models.Criminal;
import org.project.fin.models.Language;
import org.project.fin.repos.CriminalRepository;
import org.project.fin.repos.LanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LanguageService {

    private LanguageRepository languageRepository;
    private CriminalRepository criminalRepository;

    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    public Language save(Language language) {
        Optional<Language> languageOpt = languageRepository.findByLanguage(language.getLanguage());
        return languageOpt.orElseGet(() -> languageRepository.save(language));
    }

    @Transactional
    public void addLanguageToCriminal(Long criminalId, String languageName) {
        if(languageName.isBlank()) return;

        Criminal criminal = criminalRepository.findById(criminalId)
                .orElseThrow(() -> new RuntimeException("Criminal not found with id: " + criminalId));

        final boolean[] isNew = new boolean[1];

        Language language = languageRepository.findByLanguage(languageName)
                .orElseGet(() -> {
                    Language newSavedLanguage = languageRepository.save(new Language(null, languageName, null));
                    newSavedLanguage.setCriminals(new HashSet<>());
                    newSavedLanguage.getCriminals().add(criminal);
                    isNew[0] = true;
                    return newSavedLanguage;
                });

        if(!isNew[0]) {
            language.getCriminals().add(criminal);
        }

        criminalRepository.save(criminal);
    }

}
