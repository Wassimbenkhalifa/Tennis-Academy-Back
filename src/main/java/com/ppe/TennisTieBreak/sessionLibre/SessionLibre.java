package com.ppe.TennisTieBreak.sessionLibre;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.session.Session;
import com.ppe.TennisTieBreak.session.SessionDTO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("libre")
public class SessionLibre extends Session{
	
    @ManyToOne  
    @JoinColumn(name="idUtilisateur")
    private Joueur joueur;
    
	public static SessionLibre mapToSessionLibre(SessionLibreDTO sessionLibreDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(sessionLibreDTO, SessionLibre.class));
	}

	public SessionLibre(Joueur joueur) {
		super();
		this.joueur = joueur;
	}

	public SessionLibre() {
		super();
	}
	
	

}
