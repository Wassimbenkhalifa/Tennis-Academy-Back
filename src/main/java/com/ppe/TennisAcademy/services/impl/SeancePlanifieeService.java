package com.ppe.TennisAcademy.services.impl;

import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.repositories.SeancePlanifieeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SeancePlanifieeService {

    @Autowired
    private SeancePlanifieeRepository seanceRepository;

    public SeancePlanifiee save(SeancePlanifiee seancePlanifiee) {
        SeancePlanifiee result=(SeancePlanifiee) seanceRepository.save(seancePlanifiee);
        System.out.println(result.toString());

        return result;
    }
}