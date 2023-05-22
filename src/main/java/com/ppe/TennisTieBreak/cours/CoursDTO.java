package com.ppe.TennisTieBreak.cours;

import org.modelmapper.ModelMapper;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoursDTO {

    private Long idCours;
    
    private String label;
    
    private String description;
    
    private int duree;  //duree en nbr de minutes

    private int nbrPlaces;
    
    
	public static CoursDTO mapToCoursDTO(Cours cours) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(cours, CoursDTO.class);
	}
}

