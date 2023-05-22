package com.ppe.TennisTieBreak.planification;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.session.Session;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifieeDTO;
import com.ppe.TennisTieBreak.cours.Cours;
import com.ppe.TennisTieBreak.cours.CoursDTO;
import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.joueur.JoueurDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanificationDTO {
	
    private Long idPlanification;
    
    private LocalDate dateDebut;
    
    private LocalDate dateFin;
    
    private String jourSemaine;
    
    private CoursDTO cours;

    private Set<JoueurDTO> joueursInscrits;
    
	public static PlanificationDTO mapToPlanificationDTO(Planification planification) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(planification, PlanificationDTO.class);
	}

}
