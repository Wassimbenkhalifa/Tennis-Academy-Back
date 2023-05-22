package com.ppe.TennisTieBreak.session;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.terrain.Terrain;

import lombok.Data;

@Entity(name="")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typesession", discriminatorType = DiscriminatorType.STRING, length = 20)
public class Session {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSession;
    
    private LocalDateTime dateHeureDebut;
    
    private LocalDateTime dateHeureFin;
    
    @ManyToOne  
    @JoinColumn( name="idTerrain" )
    private Terrain terrain;
    
//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="planification_id")
//    private Planification planification;
    
    
	public static Session mapToSession(SessionDTO sessionDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(sessionDTO, Session.class));
	}

}
