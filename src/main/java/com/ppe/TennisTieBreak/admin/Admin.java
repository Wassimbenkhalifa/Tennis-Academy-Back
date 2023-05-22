package com.ppe.TennisTieBreak.admin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("admin")
public class Admin extends Utilisateur{
	
}
