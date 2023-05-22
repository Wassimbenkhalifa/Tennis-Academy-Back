package com.ppe.TennisTieBreak.joueur;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@DiscriminatorValue("joueur")
public class Joueur extends Utilisateur{


	private int nbrMatchJoues;
	
	
	@ManyToMany(mappedBy = "joueursInscrits")
	Set<Planification> planification;
	
}
