package org.project.fin.services;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.TypedParameterValue;
import org.hibernate.type.StandardBasicTypes;
import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.Criminal;
import org.project.fin.repos.CriminalDetailsRepository;
import org.project.fin.repos.CriminalRepository;
import org.project.fin.utils.mapper.Mapper;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CriminalService {

    private CriminalRepository criminalRepository;
    private CriminalDetailsRepository criminalDetailsRepository;
    private Mapper<Criminal, CriminalDTO> criminalMapper;

    @PersistenceContext
    private EntityManager em;

//    private static final String sqlQueryFor_findByAttributes = "SELECT\n" +
//            "    ct.rowid AS criminal_id,\n" +
//            "    ct.EYE_COLOR,\n" +
//            "    ct.HAIR_COLOR,\n" +
//            "    ct.HEIGHT,\n" +
//            "    ct.BIRTH_PLACE,\n" +
//            "    TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') AS BIRTH_DATE,\n" +
//            "    ct.LAST_RESIDENCE,\n" +
//            "    ct.CITIZENSHIP\n" +
//            "FROM\n" +
//            "    crosstab(\n" +
//            "        'SELECT criminal_id, attribute_type, attribute_value FROM criminal_details ORDER BY 1, 2'," +
//            "        $$VALUES ('EYE_COLOR'), ('HAIR_COLOR'), ('HEIGHT'), ('BIRTH_PLACE'), ('BIRTH_DATE'), ('LAST_RESIDENCE'), ('CITIZENSHIP')$$" +
//            "    ) AS ct(" +
//            "        rowid bigint," +
//            "        EYE_COLOR varchar," +
//            "        HAIR_COLOR varchar," +
//            "        HEIGHT varchar," +
//            "        BIRTH_PLACE varchar," +
//            "        BIRTH_DATE varchar," +
//            "        LAST_RESIDENCE varchar," +
//            "        CITIZENSHIP varchar" +
//            "    )" +
//            "\nWHERE (CAST(:eyeColor AS varchar) IS NULL OR ct.EYE_COLOR = CAST(:eyeColor AS varchar)) " +
//            "\nAND (CAST(:hairColor AS varchar) IS NULL OR ct.HAIR_COLOR = CAST(:hairColor AS varchar)) " +
//            "\nAND (CAST(:height AS varchar) IS NULL OR ct.HEIGHT = CAST(:height AS varchar)) " +
//            "\nAND (CAST(:birthPlace AS varchar) IS NULL OR ct.BIRTH_PLACE = CAST(:birthPlace AS varchar)) " +
//            "\nAND (CAST(:birthDate AS timestamp) IS NULL OR TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') = TO_TIMESTAMP(CAST(:birthDate AS varchar), 'YYYY-MM-DD')) " +
//            "\nAND (CAST(:lastResidence AS varchar) IS NULL OR ct.LAST_RESIDENCE = CAST(:lastResidence AS varchar)) " +
//            "\nAND (CAST(:citizenship AS varchar) IS NULL OR ct.CITIZENSHIP = CAST(:citizenship AS varchar))";

//    private static final String sqlQueryFor_findByAttributes = "SELECT\n" +
//            "    ct.rowid AS criminal_id,\n" +
//            "    ct.EYE_COLOR,\n" +
//            "    ct.HAIR_COLOR,\n" +
//            "    ct.HEIGHT,\n" +
//            "    ct.BIRTH_PLACE,\n" +
//            "    TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') AS BIRTH_DATE,\n" +
//            "    ct.LAST_RESIDENCE,\n" +
//            "    ct.CITIZENSHIP\n" +
//            "FROM\n" +
//            "    crosstab(\n" +
//            "        'SELECT criminal_id, attribute_type, attribute_value FROM criminal_details ORDER BY 1, 2'," +
//            "        $$VALUES ('EYE_COLOR'), ('HAIR_COLOR'), ('HEIGHT'), ('BIRTH_PLACE'), ('BIRTH_DATE'), ('LAST_RESIDENCE'), ('CITIZENSHIP')$$" +
//            "    ) AS ct(" +
//            "        rowid bigint," +
//            "        EYE_COLOR varchar," +
//            "        HAIR_COLOR varchar," +
//            "        HEIGHT varchar," +
//            "        BIRTH_PLACE varchar," +
//            "        BIRTH_DATE varchar," +
//            "        LAST_RESIDENCE varchar," +
//            "        CITIZENSHIP varchar" +
//            "    )" +
//            "\nWHERE (CAST(null AS varchar) IS NULL OR ct.EYE_COLOR = CAST(null AS varchar)) " +
//            "\nAND (CAST(null AS varchar) IS NULL OR ct.HAIR_COLOR = CAST(null AS varchar)) " +
//            "\nAND (CAST(null AS varchar) IS NULL OR ct.HEIGHT = CAST(null AS varchar)) " +
//            "\nAND (CAST(null AS varchar) IS NULL OR ct.BIRTH_PLACE = CAST(null AS varchar)) " +
//            "\nAND (CAST(null AS timestamp) IS NULL OR TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') = TO_TIMESTAMP(CAST(null AS varchar), 'YYYY-MM-DD')) " +
//            "\nAND (CAST(null AS varchar) IS NULL OR ct.LAST_RESIDENCE = CAST(null AS varchar)) " +
//            "\nAND (CAST(null AS varchar) IS NULL OR ct.CITIZENSHIP = CAST(null AS varchar))";

    private static final String sqlQueryFor_findByAttributes =
            "SELECT\n" +
                    "    ct.rowid AS criminal_id,\n" +
                    "    ct.EYE_COLOR,\n" +
                    "    ct.HAIR_COLOR,\n" +
                    "    ct.HEIGHT,\n" +
                    "    ct.BIRTH_PLACE,\n" +
                    "    TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') AS BIRTH_DATE,\n" +
                    "    ct.LAST_RESIDENCE,\n" +
                    "    ct.CITIZENSHIP\n" +
                    "FROM\n" +
                    "    crosstab(\n" +
                    "        'SELECT criminal_id, attribute_type, attribute_value FROM criminal_details ORDER BY 1, 2'," +
                    "        $$VALUES ('EYE_COLOR'), ('HAIR_COLOR'), ('HEIGHT'), ('BIRTH_PLACE'), ('BIRTH_DATE'), ('LAST_RESIDENCE'), ('CITIZENSHIP')$$" +
                    "    ) AS ct(" +
                    "        rowid bigint," +
                    "        EYE_COLOR varchar," +
                    "        HAIR_COLOR varchar," +
                    "        HEIGHT varchar," +
                    "        BIRTH_PLACE varchar," +
                    "        BIRTH_DATE varchar," +
                    "        LAST_RESIDENCE varchar," +
                    "        CITIZENSHIP varchar" +
                    "    )" +
                    "\nWHERE (cast(:eyeColor as varchar) IS NULL OR ct.EYE_COLOR = :eyeColor) " +
                    "\nAND (cast(:hairColor as varchar) IS NULL OR ct.HAIR_COLOR = :hairColor) " +
                    "\nAND (cast(:height as varchar) IS NULL OR ct.HEIGHT = :height) " +
                    "\nAND (cast(:birthPlace as varchar) IS NULL OR ct.BIRTH_PLACE = :birthPlace) " +
                    "\nAND (cast(:birthDate as timestamp) IS NULL OR TO_TIMESTAMP(ct.BIRTH_DATE, 'YYYY-MM-DD') = :birthDate) " +
                    "\nAND (cast(:lastResidence as varchar) IS NULL OR ct.LAST_RESIDENCE = :lastResidence) " +
                    "\nAND (cast(:citizenship as varchar) IS NULL OR ct.CITIZENSHIP = :citizenship)";


    public List<Criminal> findAll() {
        return criminalRepository.findAll();
    }

    public List<CriminalDTO> findAllDTO() {
        return criminalRepository.findAllQuery();
    }

    public Criminal findById(long criminalId) {
        Optional<Criminal> criminalOpt = criminalRepository.findById(criminalId);
        return criminalOpt.orElse(null);
    }

    public boolean save(Criminal criminal) {
        Criminal savedCriminal = criminalRepository.save(criminal);
        return savedCriminal.getId() > 0;
    }

    public boolean update(long criminalId, Criminal criminal) {
        Optional<Criminal> criminalOpt = criminalRepository.findById(criminalId);
        if(criminalOpt.isEmpty()) {
            return false;
        }
        criminalRepository.save(criminal);
        return true;
    }

    public void delete(long id) {
        criminalRepository.deleteById(id);
    }

    private void setParameterWithNullCheck(Query query, String paramName, Object paramValue) {
        if (paramValue != null) {
            query.setParameter(paramName, paramValue);
        } else {
            query.setParameter(paramName, null);
        }
    }

    private List<CriminalDetailsDTO> findByAttributes(CriminalDetailsDTO criminalDetailsDTO) {
        Query query = em.createNativeQuery(sqlQueryFor_findByAttributes);

        String eyeColor1 = criminalDetailsDTO.getEyeColor();
        String hairColor1 = criminalDetailsDTO.getHairColor();
        String height1 = criminalDetailsDTO.getHeight();
        String birthPlace1 = criminalDetailsDTO.getBirthPlace();
        LocalDate birthDate1 = criminalDetailsDTO.getBirthDate();
        String lastResidence1 = criminalDetailsDTO.getLastResidence();
        String citizenship1 = criminalDetailsDTO.getCitizenship();

        setParameterWithNullCheck(query, "eyeColor", criminalDetailsDTO.getEyeColor());
        setParameterWithNullCheck(query, "hairColor", criminalDetailsDTO.getHairColor());
        setParameterWithNullCheck(query, "height", criminalDetailsDTO.getHeight());
        setParameterWithNullCheck(query, "birthPlace", criminalDetailsDTO.getBirthPlace());
        setParameterWithNullCheck(query, "birthDate", criminalDetailsDTO.getBirthDate());
        setParameterWithNullCheck(query, "lastResidence", criminalDetailsDTO.getLastResidence());
        setParameterWithNullCheck(query, "citizenship", criminalDetailsDTO.getCitizenship());

        //        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("eyeColor", criminalDetailsDTO.getEyeColor());
//        parameters.put("hairColor", criminalDetailsDTO.getHairColor());
//        parameters.put("height", criminalDetailsDTO.getHeight());
//        parameters.put("birthPlace", criminalDetailsDTO.getBirthPlace());
//        parameters.put("birthDate", criminalDetailsDTO.getBirthDate());
//        parameters.put("lastResidence", criminalDetailsDTO.getLastResidence());
//        parameters.put("citizenship", criminalDetailsDTO.getCitizenship());
//
//        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
//            if (entry.getValue() == null) {
//                parameters.put(entry.getKey(), null);
//            }
//        }
//
//        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
//            query.setParameter(entry.getKey(), entry.getValue());
//        }

//        query.setParameter("eyeColor", eyeColor1.equals(null) ? eyeColor1 : String.valueOf(null));
//        query.setParameter("hairColor", hairColor1.equals(null) ? hairColor1 : String.valueOf(null));
//        query.setParameter("height", height1.equals(null) ? height1 : String.valueOf(null));
//        query.setParameter("birthPlace", birthPlace1.equals(null) ? birthPlace1 : String.valueOf(null));
//        query.setParameter("birthDate", birthDate1);
//        query.setParameter("lastResidence", lastResidence1.equals(null) ? lastResidence1 : String.valueOf(null));
//        query.setParameter("citizenship", citizenship1.equals(null) ? citizenship1 : String.valueOf(null));

        // METHOD 1
//        query.setParameter("eyeColor", (String) checkNull(eyeColor1));
//        query.setParameter("hairColor", (String) checkNull(hairColor1));
//        query.setParameter("height", (String) checkNull(height1));
//        query.setParameter("birthPlace", (String) checkNull(birthPlace1));
//        query.setParameter("birthDate", (LocalDate) checkNull(birthDate1));//new TypedParameterValue<>(StandardBasicTypes.LOCAL_DATE, birthDate1)
//        query.setParameter("lastResidence", (String) checkNull(lastResidence1));
//        query.setParameter("citizenship", (String) checkNull(citizenship1));

        // METHOD 2
//        query.setParameter("eyeColor", criminalDetailsDTO.getEyeColor());
//        query.setParameter("hairColor", null);
//        query.setParameter("height", null);
//        query.setParameter("birthPlace", null);
//        query.setParameter("birthDate", criminalDetailsDTO.getBirthDate());// formattedBirthDateAsString
//        query.setParameter("lastResidence", null);
//        query.setParameter("citizenship", null);

//        String sqlWithParams = query.unwrap(org.hibernate.query.NativeQuery.class).getQueryString();
//        System.out.println("SQL with parameters: " + sqlWithParams);
        List<Object[]> results = query.getResultList();

        System.out.println();
        if(results.stream().findFirst().isPresent())
            System.out.println("RESULT SET: " + results.stream().findFirst().get());
        else
            System.out.println("EMPTY SET");
        System.out.println();

        List<CriminalDetailsDTO> cdDTOs = results.stream()
                .map(cdDTO -> {
                    Long id = (cdDTO[0] instanceof Long) ? (Long) cdDTO[0] : null;
                    String eyeColor = (cdDTO[1] instanceof String) ? (String) cdDTO[1] : null;
                    String hairColor = (cdDTO[2] instanceof String) ? (String) cdDTO[2] : null;
                    String height = (cdDTO[3] instanceof String) ? (String) cdDTO[3] : null;
                    String birthPlace = (cdDTO[4] instanceof String) ? (String) cdDTO[4] : null;
                    LocalDate birthDate = (cdDTO[5] instanceof LocalDate) ? (LocalDate) cdDTO[5] : null;
                    String lastResidence = (cdDTO[6] instanceof String) ? (String) cdDTO[6] : null;
                    String citizenship = (cdDTO[7] instanceof String) ? (String) cdDTO[7] : null;

                    return new CriminalDetailsDTO(id, eyeColor, hairColor, height, birthPlace, birthDate, lastResidence, citizenship);
                })
                .collect(Collectors.toList());
        return cdDTOs;
    }

    public List<Criminal> searchCriminalsByAttributes(CriminalDetailsDTO criminalDetailsDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBirthDate = null;
        if(criminalDetailsDTO.getBirthDate() != null)
            formattedBirthDate = criminalDetailsDTO.getBirthDate().format(formatter);
//        List<Long> filteredCriminalIds = criminalDetailsRepository.findCriminalsByAttributes_Named_2(
//                        criminalDetailsDTO.getEyeColor(),
//                        criminalDetailsDTO.getHairColor(),
//                        criminalDetailsDTO.getHeight(),
//                        criminalDetailsDTO.getBirthPlace(),
//                        formattedBirthDate ,
//                        criminalDetailsDTO.getLastResidence(),
//                        criminalDetailsDTO.getCitizenship()
//                ).stream()
//                .map(CriminalDetailsDTO::getCriminalId)
//                .collect(Collectors.toList());
//        System.out.println("filteredCriminalIds: "+filteredCriminalIds);

        List<CriminalDetailsDTO> cdDTOs = criminalDetailsRepository.findCriminals_by_Attr_Crosstab_Named_2(
                        criminalDetailsDTO.getEyeColor(),
                        criminalDetailsDTO.getHairColor(),
                        criminalDetailsDTO.getHeight(),
                        criminalDetailsDTO.getBirthPlace(),
                        formattedBirthDate,
                        criminalDetailsDTO.getLastResidence(),
                        criminalDetailsDTO.getCitizenship()
                ).stream()
                .map(cdDTO -> new CriminalDetailsDTO(
                        cdDTO.get(0, Long.class),
                        cdDTO.get(1, String.class),
                        cdDTO.get(2, String.class),
                        cdDTO.get(3, String.class),
                        cdDTO.get(4, String.class),
                        cdDTO.get(5, LocalDate.class),
                        cdDTO.get(6, String.class),
                        cdDTO.get(7, String.class)
                        )
                )
                .collect(Collectors.toList());

//        List<CriminalDetailsDTO> cdDTOs = findByAttributes(criminalDetailsDTO);




//        List<CriminalDetailsDTO> cdDTOs = new ArrayList<>();




//        List<CriminalDetailsDTO> cdDTOs = criminalDetailsRepository.findByAttributesFunc(
//            criminalDetailsDTO.getEyeColor(),
//            criminalDetailsDTO.getHairColor(),
//            criminalDetailsDTO.getHeight(),
//            criminalDetailsDTO.getBirthPlace(),
//            formattedBirthDate,
//            criminalDetailsDTO.getLastResidence(),
//            criminalDetailsDTO.getCitizenship()
//        );

        System.out.println("cdDTOs[0]: "+cdDTOs.stream().findFirst());
        List<Long> filteredCriminalIds = cdDTOs.stream().map(CriminalDetailsDTO::getCriminalId).collect(Collectors.toList());
        System.out.println("filteredCriminalIds: "+filteredCriminalIds);

        List<Criminal> criminals = criminalRepository.findAllByIdIn(filteredCriminalIds);
        return criminals;
    }

    public List<CriminalDTO> searchCriminalsByCriminalInfoStrict(CriminalDTO criminalDTO) {
        List<CriminalDTO> filteredCriminals = criminalRepository.findCriminalsByCriminalInfoStrict(
                criminalDTO.getFirstName(),
                criminalDTO.getLastName(),
                criminalDTO.getNickname(),
                criminalDTO.getCriminalProfession()
        );
        return filteredCriminals;
    }

    public List<CriminalDTO> searchCriminalsByCriminalInfoNotStrict(CriminalDTO criminalDTO) {
        List<CriminalDTO> filteredCriminals = criminalRepository.findCriminalsByCriminalInfoNotStrict(
                criminalDTO.getFirstName(),
                criminalDTO.getLastName(),
                criminalDTO.getNickname(),
                criminalDTO.getCriminalProfession()
        );
        return filteredCriminals;
    }

}
