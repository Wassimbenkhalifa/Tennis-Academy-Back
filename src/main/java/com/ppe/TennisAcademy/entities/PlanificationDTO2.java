package com.ppe.TennisAcademy.entities;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter
@Setter
public class PlanificationDTO2 {

    private Long idPlanification;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Long cours;

    //private Set<AdherentDTO> joueursInscrits;

    public static PlanificationDTO2 mapToPlanificationDTO(Planification planification) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(planification, PlanificationDTO2.class);
    }

}
