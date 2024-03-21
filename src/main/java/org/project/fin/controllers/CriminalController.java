package org.project.fin.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.Criminal;
import org.project.fin.services.CriminalService;
import org.project.fin.utils.mapper.Mapper;
import org.project.fin.utils.mapper.criminal.CriminalMapper;
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
    private Mapper<Criminal, CriminalDTO> criminalCriminalDTOMapper;

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

//        System.out.println("---------------- Search START -----------------");
//        CriminalDetailsDTO temp = new CriminalDetailsDTO();
//        temp.setEyeColor("green");
//        System.out.println(criminalService.searchCriminalsByAttributes(temp));
//        System.out.println("---------------- Search END -----------------");

        List<CriminalDTO> criminalList = criminalService.findAllDTO();
        model.addAttribute("criminals", criminalList);
        model.addAttribute("criminalDetailsDTO", new CriminalDetailsDTO());
        model.addAttribute("criminalDTO", new CriminalDTO());
        return "search_panel";
    }

    // Filter criminals: do search by attributes only
    @PostMapping("/search")
    public String searchCriminals(@ModelAttribute CriminalDetailsDTO criminalDetailsDTO,
                                  @ModelAttribute CriminalDTO criminalDTO,
                                  Model model) {

        processFormData(criminalDetailsDTO);
//        System.out.println(criminalService.searchCriminalsByAttributes(criminalDetailsDTO));

        List<Criminal> foundList = criminalService.searchCriminalsByAttributes(criminalDetailsDTO);
        Set<Criminal> foundSet = criminalService.searchCriminalsByCriminalInfoStrict(criminalDTO).stream()
                .map(criminalCriminalDTOMapper::toEntity)
                .collect(Collectors.toSet());
//        dtoSet.removeAll(foundDtoList);
        List<Criminal> intersection = foundList.stream()
                .filter(foundDto -> foundSet.stream()
                        .anyMatch(criminalDto -> Objects.equals(criminalDto.getId(), foundDto.getId())))
                .collect(Collectors.toList());

        model.addAttribute("criminals", intersection);
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
    public String add(@RequestBody Criminal criminal) {
        boolean isSuccess = criminalService.save(criminal);
        return "redirect:/";
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
