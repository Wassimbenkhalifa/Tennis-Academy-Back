package com.ppe.TennisAcademy.entities;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.modelmapper.ModelMapper;

import java.util.Set;

@Entity
@Data
public class Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTerrain;

    private String description;

    private String label;

    private String type;

    private Boolean enable;


//    @ManyToMany(mappedBy = "terrainsReserves")
//    private Set<Adherent> adherents;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Media photo;

    public static Terrain mapToTerrain(TerrainDTO terrainDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(terrainDTO, Terrain.class));
    }

}