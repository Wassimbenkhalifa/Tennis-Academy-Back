package com.ppe.TennisTieBreak.entraineur;


import java.util.Date;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.utilisateur.UtilisateurDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EntraineurDTO extends UtilisateurDTO{
	
	private Date dateEngagement;
	
	private float rate;

    public static EntraineurDTO mapToEntraineurDTO(Entraineur entraineur){
        return new ModelMapper().map(entraineur, EntraineurDTO.class);
    }
}
