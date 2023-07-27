package com.ppe.TennisAcademy.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import lombok.Data;
import org.modelmapper.ModelMapper;

@Entity
@Data
@DiscriminatorValue("planifiee")
public class SeancePlanifiee extends Seance {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlanification")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Planification planification;

    public static SeancePlanifiee mapToSeancePlanifiee(SeancePlanifieeDTO seancePlanifieeDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(seancePlanifieeDTO, SeancePlanifiee.class));
    }

}