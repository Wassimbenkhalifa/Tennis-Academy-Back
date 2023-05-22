package com.ppe.TennisTieBreak.sessionLibre;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.joueur.JoueurDTO;
import com.ppe.TennisTieBreak.session.SessionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionLibreDTO extends SessionDTO{

	
    private JoueurDTO joueur;

	public static SessionLibreDTO mapToSessionLibreDTO(SessionLibre sessionLibre) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(sessionLibre, SessionLibreDTO.class);
	}
}
