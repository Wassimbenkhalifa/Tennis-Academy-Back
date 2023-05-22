package com.ppe.TennisTieBreak.session;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.joueur.JoueurDTO;
import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.planification.PlanificationDTO;
import com.ppe.TennisTieBreak.terrain.TerrainDTO;

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

	
    private JoueurDTO joueur;

    
	public static SessionDTO mapToSessionDTO(Session session) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(session, SessionDTO.class);
	}
}
