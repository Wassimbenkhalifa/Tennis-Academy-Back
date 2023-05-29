package com.ppe.TennisAcademy.services;

import java.util.List;

import com.ppe.TennisAcademy.entities.Planification;
import com.ppe.TennisAcademy.entities.SeancePlanifiee;


public interface IPlanificationService {

    Planification save(Planification planification);

    Planification edit(Planification planification);

    void deleteById(Long id);

    Planification getById(Long id);

    List<Planification> getAll();

    List<SeancePlanifiee> PlanfierSeance(Planification planification);
}
