package com.ppe.TennisAcademy.entities;

import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPublication;

    private  String contenue;

    public Publication(String contenue) {
        this.contenue = contenue;
    }
    public Publication(){

    }
    public static Publication mapToPublication(PublicationDTO publicationDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(publicationDTO, Publication.class));
    }
}
