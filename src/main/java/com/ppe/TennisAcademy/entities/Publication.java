package com.ppe.TennisAcademy.entities;

import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPublication;

    private  String text;

    private LocalDateTime localDateheurePublication;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn( name="idUser" )
    private User user;


    /*public Publication(String text) {
        this.text = text;
    }
    public Publication(){

    }*/
    public static Publication mapToPublication(PublicationDTO publicationDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(publicationDTO, Publication.class));
    }
}
