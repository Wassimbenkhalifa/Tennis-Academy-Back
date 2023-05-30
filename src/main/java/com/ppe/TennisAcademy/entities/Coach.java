package com.ppe.TennisAcademy.entities;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;



import lombok.Data;
import org.modelmapper.ModelMapper;

@Entity
@Data
@DiscriminatorValue("COACH")
public class Coach extends User{

    private Date dateEngagement;

    private float rate;

   /* public static Coach mapToCoach(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(user, Coach.class));
    }*/
}