package com.ppe.TennisTieBreak.planification;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.cours.Cours;
import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.session.Session;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;

import lombok.Data;

@Entity
@Data
public class Planification {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPlanification;
    
    private LocalDate dateDebut;
    
    private LocalDate dateFin;
    
    private String JourSemaine; // MON: 1, TUE: 2, WED: 3, THU: 4, FRI: 5, SAT: 6, SUN 7 

    @ManyToOne  
    @JoinColumn( name="idCours" )
    private Cours cours;
    
    @ManyToMany
    @JoinTable(
      name = "inscription_joueur", 
      joinColumns = @JoinColumn(name = "joueur_id"), 
      inverseJoinColumns = @JoinColumn(name = "planification_id"))
    Set<Joueur> joueursInscrits;
    
    
	public static Planification mapToPlanification(PlanificationDTO planificationDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(planificationDTO, Planification.class));
	}

}
