package com.ppe.TennisTieBreak.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.publication.PublicationRepository;
import com.ppe.TennisTieBreak.sessionLibre.SessionLibre;
import com.ppe.TennisTieBreak.sessionLibre.SessionLibreRepository;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifieeRepository;
import com.ppe.TennisTieBreak.terrain.Terrain;

@Service
public class SessionService implements ISessionService {

	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private SessionPlanifieeRepository sessionPlanifieeRepository;
	
	@Autowired
	private SessionLibreRepository sessionLibreRepository;

	@Override
	public Session save(Session session) {
		return sessionRepository.save(session);
	}

	@Override
	public Session edit(Session session) {
		return sessionRepository.save(session);
	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.sessionRepository.deleteById(id);
		}
	}

	@Override
	public Session getById(Long id) {
		return this.sessionRepository.getById(id);
	}

	@Override
	public List<Session> getAll() {
		return sessionRepository.findAll();
	}
	
	@Override
	public List<SessionLibre> getAllLibre() {
		List<SessionLibre> listsession=sessionLibreRepository.findAll();
		return listsession;
	}
	
	
	@Override
	public List<SessionPlanifiee> getAllPlanifiee() {
		List<SessionPlanifiee> listsession=sessionPlanifieeRepository.findAll();
		return listsession;
	}

	@Override
	public List<Session> findByTerrainAndDateHeureDebut(Terrain terain, Date dateDebut) {
		return this.sessionRepository.findByTerrainAndDateHeureDebut(terain, dateDebut);
	}
}
