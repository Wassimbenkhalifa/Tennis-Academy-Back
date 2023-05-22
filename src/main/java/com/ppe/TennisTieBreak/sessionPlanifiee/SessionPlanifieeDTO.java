package com.ppe.TennisTieBreak.sessionPlanifiee;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.planification.PlanificationDTO;
import com.ppe.TennisTieBreak.session.SessionDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class SessionPlanifieeDTO extends SessionDTO{

	private PlanificationDTO planification;
	 
	public static SessionPlanifieeDTO mapToSessionPlanifieeDTO(SessionPlanifiee sessionPlanifiee) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(sessionPlanifiee, SessionPlanifieeDTO.class);
	}
}
