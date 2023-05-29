package com.ppe.TennisAcademy.services;


import java.util.Date;
import java.util.List;
import com.ppe.TennisAcademy.entities.SeancesLibre;
import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.entities.Session;
import com.ppe.TennisAcademy.entities.Terrain;
import com.ppe.TennisAcademy.repositories.SeanceLibreRepository;
import com.ppe.TennisAcademy.repositories.SeancePlanifieeRepository;
import com.ppe.TennisAcademy.repositories.SessionRepository;
import com.ppe.TennisAcademy.services.ImplServices.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SessionService implements ISessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SeancePlanifieeRepository seancePlanifieeRepository;

    @Autowired
    private SeanceLibreRepository seanceLibreRepository;

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
    public List<SeancesLibre> getAllLibre() {
        List<SeancesLibre> listsession=seanceLibreRepository.findAll();
        return listsession;
    }


    @Override
    public List<SeancePlanifiee> getAllPlanifiee() {
        List<SeancePlanifiee> listsession=seancePlanifieeRepository.findAll();
        return listsession;
    }

    @Override
    public List<Session> findByTerrainAndDateHeureDebut(Terrain terrain, Date dateDebut) {
        return this.sessionRepository.findByTerrainAndDateHeureDebut(terrain, dateDebut);
    }
}
