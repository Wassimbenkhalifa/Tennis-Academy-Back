package com.ppe.TennisAcademy.services.impl;


import java.util.Date;
import java.util.List;
import com.ppe.TennisAcademy.entities.SeancesLibre;
import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.entities.Seance;
import com.ppe.TennisAcademy.entities.Terrain;
import com.ppe.TennisAcademy.repositories.SeanceLibreRepository;
import com.ppe.TennisAcademy.repositories.SeancePlanifieeRepository;
import com.ppe.TennisAcademy.repositories.SeanceRepository;
import com.ppe.TennisAcademy.services.ISeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SeanceService implements ISeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private SeancePlanifieeRepository seancePlanifieeRepository;

    @Autowired
    private SeanceLibreRepository seanceLibreRepository;

    @Override
    public Seance save(Seance seance) {
        return seanceRepository.save(seance);
    }

    @Override
    public Seance edit(Seance seance) {
        return seanceRepository.save(seance);
    }

    @Override
    public void deleteById(Long id) {
        if (getById(id) != null) {
            this.seanceRepository.deleteById(id);
        }
    }

    @Override
    public Seance getById(Long id) {
        return this.seanceRepository.getById(id);
    }

    @Override
    public List<Seance> getAll() {
        return seanceRepository.findAll();
    }

    @Override
    public List<SeancesLibre> getAllLibre() {
        List<SeancesLibre> listseance=seanceLibreRepository.findAll();
        return listseance;
    }


    @Override
    public List<SeancePlanifiee> getAllPlanifiee() {
        List<SeancePlanifiee> listseance=seancePlanifieeRepository.findAll();
        return listseance;
    }

    @Override
    public List<Seance> findByTerrainAndDateHeureDebut(Terrain terrain, Date dateDebut) {
        return this.seanceRepository.findByTerrainAndDateHeureDebut(terrain, dateDebut);
    }
}
