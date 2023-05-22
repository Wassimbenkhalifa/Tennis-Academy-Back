package com.ppe.TennisTieBreak.session;

import java.util.Date;
import java.util.List;

import com.ppe.TennisTieBreak.sessionLibre.SessionLibre;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;
import com.ppe.TennisTieBreak.terrain.Terrain;

public interface ISessionService {

	Session save(Session session);

	Session edit(Session session);

	void deleteById(Long id);

	Session getById(Long id);

	List<Session> getAll();
	
	List<SessionLibre> getAllLibre();
	
	List<SessionPlanifiee> getAllPlanifiee();
	
	List<Session> findByTerrainAndDateHeureDebut(Terrain terain, Date dateDebut);
}
