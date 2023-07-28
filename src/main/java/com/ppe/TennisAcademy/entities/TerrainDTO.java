package com.ppe.TennisAcademy.entities;

import com.ppe.TennisAcademy.config.Pathconst;
import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TerrainDTO {

    private Long idTerrain;

    private String label;

    private String type;

    private Boolean enable;

    private MediaDTO photo;


    public static TerrainDTO mapToTerrainDTO(Terrain terrain) {
        ModelMapper modelMapper = new ModelMapper();
        return mapToTerrainDTOWithPhoto(modelMapper.map(terrain, TerrainDTO.class));
    }

    public static TerrainDTO mapToTerrainDTOWithPhoto(TerrainDTO terrainDTO) {
        if(terrainDTO.getPhoto() != null) {
            terrainDTO.getPhoto()
                    .setMediaURL(Pathconst.PATH_IMAGE_TERRAIN + terrainDTO.getPhoto().getFileName());
        }
        return terrainDTO;
    }

    @Override
    public String toString() {
        return "TerrainDTO [id=" + idTerrain + ", name=" + label + ", type=" + type
                + ", enable=" + enable + ", photo=" + photo + "]";
    }
}
