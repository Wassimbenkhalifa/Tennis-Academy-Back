package com.ppe.TennisAcademy.entities;

import org.modelmapper.ModelMapper;
import lombok.Data;

@Data
public class SeancePlanifieeDTO extends SessionDTO{

    private PlanificationDTO planification;

    public static SeancePlanifieeDTO mapToSeancePlanifieeDTO(SeancePlanifiee seancePlanifiee) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(seancePlanifiee, SeancePlanifieeDTO.class);
    }
}