package com.ppe.TennisAcademy.entities;

import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeanceLibreDTO extends SessionDTO{


    private AdherentDTO adherentDTO;

    public static SeanceLibreDTO mapToSessionLibreDTO(SeancesLibre seanceLibre) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(seanceLibre, SeanceLibreDTO.class);
    }
}