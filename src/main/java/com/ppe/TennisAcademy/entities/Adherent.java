package com.ppe.TennisAcademy.entities;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.modelmapper.ModelMapper;

@Entity
@Data
@DiscriminatorValue("ADHERENT")
public class Adherent extends User {


    private int nbrMatchJoues;

    public Adherent() {
        super();
    }

    @ManyToMany
    @JoinTable(name = "Adherent_Terrain",
            joinColumns = @JoinColumn(name = "idTerrain"),
            inverseJoinColumns = @JoinColumn(name = "idUser"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Terrain> terrainsReserves;

    public static Adherent mapToAdherent(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(user, Adherent.class));
    }

}