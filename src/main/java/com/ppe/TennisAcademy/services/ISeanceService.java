package com.ppe.TennisAcademy.services;

import com.ppe.TennisAcademy.entities.SeancesLibre;
import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.entities.Seance;
import com.ppe.TennisAcademy.entities.Terrain;

import java.util.Date;
import java.util.List;



public interface ISeanceService {

    Seance save(Seance seance);

        Seance edit(Seance seance);

    void deleteById(Long id);

    Seance getById(Long id);

    List<Seance> getAll();

    List<SeancesLibre> getAllLibre();

    List<SeancePlanifiee> getAllPlanifiee();

    List<Seance> findByTerrainAndDateHeureDebut(Terrain terain, Date dateDebut);
}