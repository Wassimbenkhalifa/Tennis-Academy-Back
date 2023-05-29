package com.ppe.TennisAcademy.entities;



import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
import lombok.Data;
import org.modelmapper.ModelMapper;

//
@Entity
@Data
public class Inscription {

@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long idInscription;

	private Date dateInscription;

    @ManyToMany
    @JoinTable(
     name = "inscription_adherent",
      joinColumns = @JoinColumn(name = "adherent_id"),
     inverseJoinColumns = @JoinColumn(name = "planification_id"))
    Set<Adherent> adherentsInscrits;
    public Inscription(Date dateInscription){
        this.dateInscription=dateInscription;

    }
    public Inscription(){

    }
  public static Inscription mapToInscription(InscriptionDTO inscriptionDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(inscriptionDTO, Inscription.class));
	}

}