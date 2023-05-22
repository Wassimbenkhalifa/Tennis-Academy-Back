package com.ppe.TennisTieBreak.sessionPlanifiee;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.session.Session;
import com.ppe.TennisTieBreak.sessionLibre.SessionLibre;
import com.ppe.TennisTieBreak.sessionLibre.SessionLibreDTO;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@DiscriminatorValue("planifiee")
public class SessionPlanifiee extends Session{

	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idPlanification")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Planification planification;
    
	public static SessionPlanifiee mapToSessionPlanifiee(SessionPlanifieeDTO sessionPlanifieeDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(sessionPlanifieeDTO, SessionPlanifiee.class));
	}
	
	
}
