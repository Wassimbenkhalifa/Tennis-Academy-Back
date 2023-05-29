package com.ppe.TennisAcademy.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.Terrain;
import com.ppe.TennisAcademy.entities.TerrainDTO;
import com.ppe.TennisAcademy.services.ImplServices.ITerrainService;
import com.ppe.TennisAcademy.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ppe.TennisAcademy.entities.MediaDTO;

import static com.ppe.TennisAcademy.config.Pathconst.PATH_IMAGE_TERRAIN;


@RestController
@RequestMapping("/api/terrain")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TerrainRestController {

    @Autowired
    private ITerrainService terrainService;

    @Autowired
    MediaService mediaService;


    @PostMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TerrainDTO> saveTerrain(@RequestBody TerrainDTO terrainDTO) {
        MediaDTO photo = terrainDTO.getPhoto();
        if (photo.getFileName() != null && photo.getFileName() != "" && photo.getFile() != null) {
            photo.setFileName(mediaService.setFileName(photo.getFileName()));
            terrainDTO.setPhoto(photo);
        } else {
            terrainDTO.setPhoto(null);
        }
        Terrain request = Terrain.mapToTerrain(terrainDTO);
        if(request.getEnable()==null)
            request.setEnable(false);
        Terrain result = this.terrainService.save(request);
        if (result != null) {
            mediaService.upload(terrainDTO.getPhoto().getFile(), terrainDTO.getPhoto().getFileName(),
                    PATH_IMAGE_TERRAIN);
            return new ResponseEntity<>(TerrainDTO.mapToTerrainDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    @PutMapping("/edit")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TerrainDTO> editTerrain(@RequestBody TerrainDTO terrainDTO) {
        MediaDTO photo = terrainDTO.getPhoto();
        if (photo.getFileName() != null && photo.getFileName() != "" && photo.getFile() != null) {
            photo.setFileName(mediaService.setFileName(photo.getFileName()));
            terrainDTO.setPhoto(photo);
        } else {
            terrainDTO.setPhoto(null);
        }
        Terrain request = Terrain.mapToTerrain(terrainDTO);
        Terrain result = this.terrainService.edit(request);
        if (result != null) {
            mediaService.deleteFileByUrl(terrainDTO.getPhoto().getMediaURL());
            mediaService.upload(terrainDTO.getPhoto().getFile(), terrainDTO.getPhoto().getFileName(),
                    PATH_IMAGE_TERRAIN);
            return new ResponseEntity<>(TerrainDTO.mapToTerrainDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    @PutMapping("/changeDispo/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> changeTerrainStatus(@PathVariable("id") long id, @RequestBody Boolean status) {

        Boolean result = this.terrainService.changeTerrainVisibility(id, status);

        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteTerrain(@PathVariable Long id) {
        TerrainDTO terrainDTO = TerrainDTO.mapToTerrainDTO(this.terrainService.getById(id));
        if (terrainDTO != null) {
            System.out.println(terrainDTO);
            this.terrainService.deleteById(id);
            mediaService.deleteFileByUrl(terrainDTO.getPhoto().getMediaURL());
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TerrainDTO> getById(@PathVariable Long id) {

        if (id != null) {
            Terrain terrain = this.terrainService.getById(id);
            if (terrain != null) {
                return new ResponseEntity<>(TerrainDTO.mapToTerrainDTO(terrain), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TerrainDTO>> getAllTerrain() {

        List<Terrain> terrainList = this.terrainService.getAll();
        if (!terrainList.isEmpty()) {
            List<TerrainDTO> terrainDTO = terrainList.stream()
                    .map(s -> TerrainDTO.mapToTerrainDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(terrainDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }


    @GetMapping("/active")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TerrainDTO>> getEnableTerrain() {

        List<Terrain> terrainList = this.terrainService.getAllEnable(true);
        if (!terrainList.isEmpty()) {
            List<TerrainDTO> terrainsDTO = terrainList.stream()
                    .map(s -> TerrainDTO.mapToTerrainDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(terrainsDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

}
