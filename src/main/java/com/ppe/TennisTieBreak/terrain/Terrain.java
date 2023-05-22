package com.ppe.TennisTieBreak.terrain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.media.Media;

import lombok.Data;

@Entity
@Data
public class Terrain {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTerrain;
    
    private String label;
    
    private String type;
    
    private Boolean enable;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Media photo;
	
	
	public static Terrain mapToTerrain(TerrainDTO terrainDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(terrainDTO, Terrain.class));
	}

}
