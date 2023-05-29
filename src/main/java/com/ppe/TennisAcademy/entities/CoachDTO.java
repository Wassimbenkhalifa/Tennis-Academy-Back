package com.ppe.TennisAcademy.entities;

import java.util.Date;

import org.modelmapper.ModelMapper;



import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CoachDTO extends UserDTO{

    private Date dateEngagement;

    private float rate;

    public static CoachDTO mapToCoachDTO(Coach coach){
        return new ModelMapper().map(coach, CoachDTO.class);
    }
}
