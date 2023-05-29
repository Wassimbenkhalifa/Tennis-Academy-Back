package com.ppe.TennisAcademy.entities;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionDTO {

    private Long idSession;

    private LocalDateTime dateHeureDebut;

    private LocalDateTime dateHeureFin;

    private TerrainDTO terrain;

    private PlanificationDTO planification;


    private AdherentDTO adherentDTO;


    public static SessionDTO mapToSessionDTO(Session session) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(session, SessionDTO.class);
    }
}