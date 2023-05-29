package com.ppe.TennisAcademy.services.impl;

import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.repositories.SeancePlanifieeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SeancePlanifieeService {

    @Autowired
    private SeancePlanifieeRepository sessionRepository;

    public SeancePlanifiee save(SeancePlanifiee sessionPlanifiee) {
        SeancePlanifiee result=(SeancePlanifiee) sessionRepository.save(sessionPlanifiee);
        System.out.println(result.toString());

        return result;
    }
}