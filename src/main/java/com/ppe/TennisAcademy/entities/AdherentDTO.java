package com.ppe.TennisAcademy.entities;

import java.util.Set;

import org.modelmapper.ModelMapper;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdherentDTO extends UserDTO{

    private int nbrMatchJoues;

    Set<PlanificationDTO> planification;

    public static AdherentDTO mapToAdherentDTO(Adherent adherent){
        return new ModelMapper().map(adherent, AdherentDTO.class);
    }
}
