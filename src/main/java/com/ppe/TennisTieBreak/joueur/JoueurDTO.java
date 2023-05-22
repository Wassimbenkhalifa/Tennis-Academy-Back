package com.ppe.TennisTieBreak.joueur;


import java.util.Set;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.planification.PlanificationDTO;
import com.ppe.TennisTieBreak.utilisateur.UtilisateurDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoueurDTO extends UtilisateurDTO{

	private int nbrMatchJoues;
	
	Set<PlanificationDTO> planification;
	
    public static JoueurDTO mapToJoueurDTO(Joueur joueur){
        return new ModelMapper().map(joueur, JoueurDTO.class);
    }
}
