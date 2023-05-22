package com.ppe.TennisTieBreak.sessionPlanifiee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.session.Session;
import com.ppe.TennisTieBreak.session.SessionRepository;

@Service
public class SessionPlanifieeService {

	@Autowired
	private SessionRepository sessionRepository;
	
	public SessionPlanifiee save(SessionPlanifiee sessionPlanifiee) {
		SessionPlanifiee result=(SessionPlanifiee) sessionRepository.save(sessionPlanifiee);
		System.out.println(result.toString());

		return result;
	}
}
