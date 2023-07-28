package com.ppe.TennisAcademy.entities;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import org.modelmapper.ModelMapper;

@Entity
@Data
public class Planification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPlanification;

    private LocalDate dateDebut;

    private LocalDate dateFin;


    @ManyToOne
    @JoinColumn( name="idCours" )
    private Cours cours;

//    @ManyToMany
//    @JoinTable(
//            name = "inscription_adherent",
//            joinColumns = @JoinColumn(name = "adherent_id"),
//            inverseJoinColumns = @JoinColumn(name = "planification_id"))
//    Set<Adherent> adherentsInscrits;
    public static Planification mapToPlanification(PlanificationDTO planificationDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(planificationDTO, Planification.class));
    }

}
