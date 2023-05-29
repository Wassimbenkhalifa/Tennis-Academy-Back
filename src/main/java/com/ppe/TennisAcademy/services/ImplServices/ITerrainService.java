package com.ppe.TennisAcademy.services.ImplServices;

import com.ppe.TennisAcademy.entities.Terrain;

import java.util.List;


public interface ITerrainService {

    Terrain save(Terrain terrain);

    Terrain edit(Terrain terrain);

    void deleteById(Long id);

    Terrain getById(Long id);

    List<Terrain> getAll();

    List<Terrain> getAllEnable(Boolean satut);

    boolean changeTerrainVisibility(long id, Boolean visible);


}
