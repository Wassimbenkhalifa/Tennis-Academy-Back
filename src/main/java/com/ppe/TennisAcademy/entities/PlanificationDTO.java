package com.ppe.TennisAcademy.entities;

import java.time.LocalDate;
import java.util.Set;
import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanificationDTO {

    private Long idPlanification;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private CoursDTO cours;

    //private Set<AdherentDTO> joueursInscrits;

    public static PlanificationDTO mapToPlanificationDTO(Planification planification) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(planification, PlanificationDTO.class);
    }

}
