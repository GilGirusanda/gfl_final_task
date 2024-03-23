package org.project.fin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.project.fin.DTO.*;
import org.project.fin.models.Archive;
import org.project.fin.models.Criminal;
import org.project.fin.models.CriminalDetails;
import org.project.fin.models.Language;
import org.project.fin.models.enums.AttributeType;
import org.project.fin.services.*;
import org.project.fin.utils.mapper.criminal.CriminalMapperImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.*;
import org.w3c.dom.Attr;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class CriminalController {
    private ArchiveService archiveService;
    private CrimeGroupService crimeGroupService;
    private LanguageService languageService;
    private CriminalService criminalService;
    private CriminalDetailsService criminalDetailsService;
    private CriminalMapperImpl criminalCriminalDTOMapper;

    private static final String PREVIOUS_PAGE_ATTRIBUTE = "previousPage";

    private HttpServletRequest request;

    // Main page
    @GetMapping
    public String getAllCriminals(Model model) {
        request.getSession().setAttribute(PREVIOUS_PAGE_ATTRIBUTE, "/");

        List<CriminalDTO> criminalList = criminalService.findAllDTO();
        model.addAttribute("criminals", criminalList);
        return "home";
    }

    // Particular criminal details
    @GetMapping("/criminal/{criminal_id}")
    public String getOneCriminal(@PathVariable("criminal_id") long criminalId, Model model) {
        String previousPage = (String) request.getSession().getAttribute(PREVIOUS_PAGE_ATTRIBUTE);
        model.addAttribute("previousPage", previousPage);

        Criminal foundCriminal = criminalService.findById(criminalId);
        model.addAttribute("criminal", foundCriminal);
        return "criminal_details";
    }

    // Filter criminals: get page
    @GetMapping("/search")
    public String searchCriminalsPage(Model model) {
        request.getSession().setAttribute(PREVIOUS_PAGE_ATTRIBUTE, "/search");

        List<CriminalDTO> criminalList = criminalService.findAllDTO();
        model.addAttribute("criminals", criminalList);
        model.addAttribute("criminalSearchDto", new CriminalSearchDTO());
        model.addAttribute("isArchived", Boolean.FALSE);
        model.addAttribute("selectedCrimeGroup", "");
        return "search_panel";
    }

    // Filter criminals: do search by attributes only
    @PostMapping("/search")
    public String searchCriminals(@ModelAttribute CriminalSearchDTO criminalSearchDTO,
                                  @RequestParam(name="isArchived", required = false) Boolean isArchived,
                                  @RequestParam(name="selectedCrimeGroup", required = false) String selectedCrimeGroup,
                                  Model model) {

        CriminalDetailsDTO criminalDetailsDTO = criminalSearchDTO.getCriminalDetailsDto();
        CriminalDTO criminalDTO = criminalSearchDTO.getCriminalDto();

        processFormDataEmptyValues(criminalDetailsDTO);

        List<Criminal> criminals;

        if(!criminalDetailsDTO.isEmpty()) {
            List<Criminal> foundCriminalsByAttrs = criminalService.searchCriminalsByAttributes(criminalDetailsDTO);
            List<Criminal> foundCriminalsByInfo = criminalService.searchCriminalsByCriminalInfoNotStrict(criminalDTO).stream()
                    .map(criminalCriminalDTOMapper::toEntity)
                    .collect(Collectors.toList());

            criminals = foundCriminalsByAttrs.stream()
                    .filter(crByAttr -> foundCriminalsByInfo.stream()
                            .anyMatch(crByInfo -> Objects.equals(crByInfo.getId(), crByAttr.getId())))
                    .collect(Collectors.toList());
        } else if(criminalDTO.isEmpty() && !criminalDetailsDTO.isEmpty()) {
            criminals = criminalService.searchCriminalsByAttributes(criminalDetailsDTO);
        } else {
            criminals = criminalService.searchCriminalsByCriminalInfoNotStrict(criminalDTO).stream()
                    .map(criminalCriminalDTOMapper::toEntity)
                    .collect(Collectors.toList());
        }

        if(isArchived != null && isArchived) {
            criminals = archiveService.filterArchivedCriminals(criminals, isArchived);
        }

        criminals = crimeGroupService.filterCriminalsByGroup(criminals, selectedCrimeGroup);

        model.addAttribute("criminals", criminals);
        model.addAttribute("criminalSearchDto", criminalSearchDTO);
        model.addAttribute("isArchived", isArchived);
        model.addAttribute("selectedCrimeGroup", selectedCrimeGroup);
        return "search_panel";
    }

    private void processFormDataEmptyValues(CriminalDetailsDTO dto) {
        // check each field and replace blank strings with null
        if (dto.getEyeColor() != null && dto.getEyeColor().isEmpty()) {
            dto.setEyeColor(null);
        }
        if (dto.getHairColor() != null && dto.getHairColor().isEmpty()) {
            dto.setHairColor(null);
        }
        if (dto.getBirthPlace() != null && dto.getBirthPlace().isEmpty()) {
            dto.setBirthPlace(null);
        }
        if (dto.getLastResidence() != null && dto.getLastResidence().isEmpty()) {
            dto.setLastResidence(null);
        }
        if (dto.getCitizenship() != null && dto.getCitizenship().isEmpty()) {
            dto.setCitizenship(null);
        }
    }

    // Add new criminal
    @PostMapping("/criminal/add")
    @ResponseBody
    public String add(@RequestBody CriminalAddDTO criminalAddDTO) {
        try {
            boolean isSuccessDetails = false;
            boolean isSuccessCriminal = false;
            Criminal savedCriminal = null;

            // Save Criminal
            CriminalDTO criminalDTO = criminalAddDTO.getCriminalDto();
            if(!criminalDTO.isEmpty()) {
                Criminal criminal = criminalCriminalDTOMapper.toEntity(criminalDTO);
                criminal.setLastCase("Few details");
                savedCriminal = criminalService.save(criminal);
                isSuccessCriminal = savedCriminal.getId() > 0;
            }

            // Save Criminal Details
            CriminalDetailsDTO criminalDetailsDTO = criminalAddDTO.getCriminalDetailsDto();
            if(!criminalDetailsDTO.isEmpty()) {
                processFormDataEmptyValues(criminalDetailsDTO);
                if(savedCriminal!=null && savedCriminal.getId() > 0) {
                    criminalDetailsDTO.setCriminalId(savedCriminal.getId());
                    isSuccessDetails = criminalDetailsService.save(criminalDetailsDTO);
                }
            }

            // Return success message
            if(isSuccessCriminal && isSuccessDetails)
                return "Criminal and Details added successfully!";
            else if(isSuccessCriminal)
                return "Only criminal added successfully!";
            else if(isSuccessDetails)
                return "Only details added successfully!";
            else
                return "No success";
        } catch (Exception e) {
            e.printStackTrace();
            // Return error message
            return "Failed to add criminal: " + e.getMessage();
        }
    }

    // Update criminal's data: get page
    @GetMapping("/criminal/{criminal_id}/update")
    public String updateGetPage(@PathVariable("criminal_id") long criminalId,
                                Model model
    ) {
        Criminal foundCriminal = criminalService.findById(criminalId);
        List<Language> availableLanguages = languageService.findAll();
        List<AttributeType> detailsTypes = Arrays.stream(AttributeType.values()).toList();

        HashMap<AttributeType, String> detailsMap = new HashMap<>();
        for(AttributeType t : detailsTypes) {
            Optional<CriminalDetails> criminalDetailsOpt = foundCriminal.getCriminalDetails().stream().filter(d->d.getAttributeType().equals(t)).findFirst();
            if(criminalDetailsOpt.isPresent()) {
                String criminalDetailValue = criminalDetailsOpt.get().getAttributeValue();
                detailsMap.put(t, criminalDetailValue);
            } else {
                detailsMap.put(t, "");
            }
        }

        CriminalDetailsMapForm criminalDetailsMapForm = new CriminalDetailsMapForm();
        criminalDetailsMapForm.setDetailsMap(detailsMap);
        criminalDetailsMapForm.setCriminal(foundCriminal);

        model.addAttribute("availableLanguages", availableLanguages);
        model.addAttribute("detailsTypes", detailsTypes);
        model.addAttribute("criminalSearchDTO", new CriminalSearchDTO());

        model.addAttribute("criminalDetailsMapForm", criminalDetailsMapForm);
        model.addAttribute("newCrimeGroupName", "");
        model.addAttribute("newLanguage", "");
        return "criminal_dossier";
    }

    // Update criminal's data: Change data
    @PostMapping("/criminal/{criminal_id}/update")
    public String update(@PathVariable("criminal_id") long criminalId,
//                         @ModelAttribute("detailsMap") HashMap<AttributeType, String> detailsMap,
                         @ModelAttribute("criminalDetailsMapForm") CriminalDetailsMapForm criminalDetailsMapForm,
                         @ModelAttribute CriminalSearchDTO criminalSearchDTO,
//                         @ModelAttribute Criminal criminal,
                         @RequestParam("newLanguage") String newLanguage,
                         @RequestParam("newCrimeGroupName") String newCrimeGroupName,
                         Model model) {
        try {
            HashMap<AttributeType, String> detailsMap = criminalDetailsMapForm.getDetailsMap();
            Criminal criminal = criminalDetailsMapForm.getCriminal();
            criminal.setId(criminalId);
            languageService.addLanguageToCriminal(criminalId, newLanguage);

            // Update Criminal
            crimeGroupService.addCriminalToGroup(criminalId, newCrimeGroupName);
            criminalService.update(criminalId, criminal);


            // Update Criminal Details
            // Prevent duplicates of CriminalDetails
            Criminal criminalToCheck =  criminalService.findById(criminalId);
            Set<CriminalDetails> detailsToCheck = criminalToCheck.getCriminalDetails();

            // Process detailsMap
            // If some details are present
            // They should be checked not to duplicate them when insert updated ones
            if (detailsToCheck.size() > 0) {
                for (Map.Entry<AttributeType, String> entry : detailsMap.entrySet()) {
                    AttributeType attributeType = entry.getKey();
                    String attributeValue = entry.getValue();

                    Optional<CriminalDetails> foundExistingDetails = detailsToCheck.stream().filter(d -> d.getAttributeType().equals(attributeType)).findFirst();

                    if (foundExistingDetails.isPresent()) {
                        String existingAttributeValue = foundExistingDetails.get().getAttributeValue();
                        if(!existingAttributeValue.equals(attributeValue)) {
                            updateCriminalDetails(criminalId, attributeType, attributeValue, foundExistingDetails.get().getId());
                        }
                    } else {
                        updateCriminalDetails(criminalId, attributeType, attributeValue, null);
                    }
                }
            } else {
                // If no details were found
                for (Map.Entry<AttributeType, String> entry : detailsMap.entrySet()) {
                    AttributeType attributeType = entry.getKey();
                    String attributeValue = entry.getValue();
                    updateCriminalDetails(criminalId, attributeType, attributeValue, null);
                }
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/search";
    }

    private void updateCriminalDetails(Long criminalId, AttributeType attributeType, String attributeValue, Long attributeIdToUpdate) {
        if (attributeValue != null && !attributeValue.isBlank() && !attributeValue.isEmpty()) {
            criminalDetailsService.updateEntity(criminalId, attributeType, attributeValue, attributeIdToUpdate);
        }
    }

}
