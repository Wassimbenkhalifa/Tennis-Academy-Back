package com.ppe.TennisTieBreak.entraineur;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ppe.TennisTieBreak.utilisateur.Utilisateur;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@DiscriminatorValue("entraineur")
public class Entraineur extends Utilisateur{

	private Date dateEngagement;
	
	private float rate;
}