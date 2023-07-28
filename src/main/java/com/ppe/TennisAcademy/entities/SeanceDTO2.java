package com.ppe.TennisAcademy.entities;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class SeanceDTO2 {

    private Long idSeance;

    private LocalDateTime dateHeureDebut;

    private LocalDateTime dateHeureFin;

    private Long terrain;

    private Long planification;

    private Long user;

    public static SeanceDTO mapToSeanceDTO(Seance seance) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(seance, SeanceDTO.class);
    }


}