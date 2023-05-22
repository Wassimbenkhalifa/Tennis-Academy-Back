package com.ppe.TennisTieBreak.planification;

import java.util.List;

import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;

public interface IPlanificationService {

	Planification save(Planification planification);

	Planification edit(Planification planification);

	void deleteById(Long id);

	Planification getById(Long id);

	List<Planification> getAll();
	
	List<SessionPlanifiee> PlanfierSession(Planification planification);
}
