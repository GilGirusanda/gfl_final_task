package org.project.fin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.project.fin.DTO.CriminalAddDTO;
import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.DTO.CriminalSearchDTO;
import org.project.fin.models.Criminal;
import org.project.fin.models.CriminalDetails;
import org.project.fin.services.CriminalDetailsService;
import org.project.fin.services.CriminalService;
import org.project.fin.utils.mapper.Mapper;
import org.project.fin.utils.mapper.criminal.CriminalMapper;
import org.project.fin.utils.mapper.criminal.CriminalMapperImpl;
import org.project.fin.utils.mapper.criminalDetails.CriminalDetailsMapperImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class CriminalController {
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
        return "search_panel";
    }

    // Filter criminals: do search by attributes only
    @PostMapping("/search")
    public String searchCriminals(@ModelAttribute CriminalSearchDTO criminalSearchDTO,
                                  Model model) {

        CriminalDetailsDTO criminalDetailsDTO = criminalSearchDTO.getCriminalDetailsDto();
        CriminalDTO criminalDTO = criminalSearchDTO.getCriminalDto();

        processFormData(criminalDetailsDTO);

        List<Criminal> criminals;

        if(!criminalDetailsDTO.isEmpty()) {
            List<Criminal> foundCriminalsByAttrs = criminalService.searchCriminalsByAttributes(criminalDetailsDTO);
            List<Criminal> foundCriminalsByInfo = criminalService.searchCriminalsByCriminalInfoNotStrict(criminalDTO).stream()
                    .map(criminalCriminalDTOMapper::toEntity)
                    .collect(Collectors.toList());

//            System.out.println("1 branch");
//            System.out.println("Attrs: " + foundCriminalsByAttrs);
//            System.out.println("Info: " + foundCriminalsByInfo);

            criminals = foundCriminalsByAttrs.stream()
                    .filter(crByAttr -> foundCriminalsByInfo.stream()
                            .anyMatch(crByInfo -> Objects.equals(crByInfo.getId(), crByAttr.getId())))
                    .collect(Collectors.toList());
        } else if(criminalDTO.isEmpty() && !criminalDetailsDTO.isEmpty()) {
//            System.out.println("2 branch");
//            System.out.println("criminalDetailsDTO: " + criminalDetailsDTO);
//            System.out.println("criminalDTO: " + criminalDTO);
            criminals = criminalService.searchCriminalsByAttributes(criminalDetailsDTO);
        } else {
//            System.out.println("3 branch");
//            System.out.println("criminalDetailsDTO: " + criminalDetailsDTO);
//            System.out.println("criminalDTO: " + criminalDTO);
            criminals = criminalService.searchCriminalsByCriminalInfoNotStrict(criminalDTO).stream()
                    .map(criminalCriminalDTOMapper::toEntity)
                    .collect(Collectors.toList());
        }
        model.addAttribute("criminals", criminals);
        model.addAttribute("criminalSearchDto", criminalSearchDTO);
        return "search_panel";
    }

    private void processFormData(CriminalDetailsDTO dto) {
        // check each field and replace blank strings with null
        if (dto.getEyeColor() != null && dto.getEyeColor().isEmpty()) {
            dto.setEyeColor(null);
        }
        if (dto.getHairColor() != null && dto.getHairColor().isEmpty()) {
            dto.setHairColor(null);
        }
//        if (dto.getHeight() != null && dto.getHeight().isEmpty()) {
//            dto.setHeight(null);
//        }
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

            // Save Criminal Details
            CriminalDetailsDTO criminalDetailsDTO = criminalAddDTO.getCriminalDetailsDto();
            if(!criminalDetailsDTO.isEmpty()) {
                isSuccessDetails = criminalDetailsService.save(criminalDetailsDTO);
            }

            // Save Criminal
            CriminalDTO criminalDTO = criminalAddDTO.getCriminalDto();
            if(!criminalDTO.isEmpty()) {
                Criminal criminal = criminalCriminalDTOMapper.toEntity(criminalDTO);
                isSuccessCriminal = criminalService.save(criminal);
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

    // Update criminal's data
    @PutMapping("/criminal/{criminal_id}/update")
    public String update(@PathVariable("criminal_id") long criminalId,
                                    @RequestBody Criminal criminal) {
        boolean isSuccess = criminalService.update(criminalId, criminal);
        return "redirect:/criminal/" + criminalId;
    }

    // Delete criminal
    @PostMapping("/criminal/{id}/delete")
    public String deleteCriminal(@PathVariable Long id) {
        criminalService.delete(id);
        return "redirect:/";
    }
}
