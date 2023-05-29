package com.ppe.TennisAcademy.services;

import com.ppe.TennisAcademy.entities.SeancesLibre;
import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.entities.Session;
import com.ppe.TennisAcademy.entities.Terrain;

import java.util.Date;
import java.util.List;



public interface ISessionService {

    Session save(Session session);

        Session edit(Session session);

    void deleteById(Long id);

    Session getById(Long id);

    List<Session> getAll();

    List<SeancesLibre> getAllLibre();

    List<SeancePlanifiee> getAllPlanifiee();

    List<Session> findByTerrainAndDateHeureDebut(Terrain terain, Date dateDebut);
}