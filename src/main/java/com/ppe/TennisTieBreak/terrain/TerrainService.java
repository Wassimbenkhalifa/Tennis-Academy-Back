package com.ppe.TennisTieBreak.terrain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.session.SessionRepository;


@Service
public class TerrainService implements ITerrainService{

	@Autowired
	private TerrainRepository terrainRepository;
	
	@Override
	public Terrain save(Terrain terrain) {
		return terrainRepository.save(terrain);
	}

	@Override
	public Terrain edit(Terrain terrain) {
		return terrainRepository.save(terrain);
	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.terrainRepository.deleteById(id);
		}
	}

	@Override
	public Terrain getById(Long id) {
		return this.terrainRepository.getById(id);
	}

	@Override
	public List<Terrain> getAll() {
		return terrainRepository.findAll();
	}

	@Override
	public List<Terrain> getAllEnable(Boolean satut) {
		return terrainRepository.findByEnable(satut);
	}

	@Override
	public boolean changeTerrainVisibility(long id, Boolean visible) {
		Terrain terrain = getById(id);
		terrain.setEnable(visible);
		Terrain result = terrainRepository.save(terrain);
		if (result != null) {
			return true;
		}		return false;
	}

}
