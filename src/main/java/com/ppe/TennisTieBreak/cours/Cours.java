package com.ppe.TennisTieBreak.cours;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.modelmapper.ModelMapper;

import lombok.Data;

@Entity
@Data
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCours;
    
    private String label;
    
    private String description;
    
    private int duree;  //duree en nbr de minutes

    private int nbrPlaces;
    
	public static Cours mapToCours(CoursDTO coursDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(coursDTO, Cours.class));
	}

}
