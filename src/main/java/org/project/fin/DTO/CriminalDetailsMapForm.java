package org.project.fin.DTO;

import org.project.fin.models.Criminal;
import org.project.fin.models.enums.AttributeType;

import java.util.HashMap;
import java.util.Map;

public class CriminalDetailsMapForm {
    private HashMap<AttributeType, String> detailsMap;
    private Criminal criminal;

    public HashMap<AttributeType, String> getDetailsMap() {
        return detailsMap;
    }

    public void setDetailsMap(HashMap<AttributeType, String> detailsMap) {
        this.detailsMap = detailsMap;
    }

    public Criminal getCriminal() {
        return criminal;
    }

    public void setCriminal(Criminal criminal) {
        this.criminal = criminal;
    }
}

