package com.ppe.TennisAcademy.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Entity
@Getter @Setter
@DiscriminatorValue("seancelibre")
public class SeancesLibre extends Seance{

    @ManyToOne
    @JoinColumn(name="idUser")
    private Adherent adherent;



    public SeancesLibre(Adherent adherent) {
        super();
        this.adherent = adherent;
    }

    public SeancesLibre() {
        super();
    }

    public static SeancesLibre mapToSeanceLibre(SeanceLibreDTO seanceLibreDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(seanceLibreDTO, SeancesLibre.class));
    }



}