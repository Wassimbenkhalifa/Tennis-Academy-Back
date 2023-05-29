package com.ppe.TennisAcademy.entities;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;



import lombok.Data;

@Entity
@Data
@DiscriminatorValue("COACH")
public class Coach extends User{

    private Date dateEngagement;

    private float rate;
}