package com.ppe.TennisAcademy.entities;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeanceDTO {

    private Long idSeance;

    private LocalDateTime dateHeureDebut;

    private LocalDateTime dateHeureFin;

    private TerrainDTO terrain;

    private PlanificationDTO planification;


    private AdherentDTO adherentDTO;


    public static SeanceDTO mapToSeanceDTO(Seance seance) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(seance, SeanceDTO.class);
    }
}