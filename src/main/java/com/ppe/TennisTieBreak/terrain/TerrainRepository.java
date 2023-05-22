package com.ppe.TennisTieBreak.terrain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TerrainRepository  extends JpaRepository<Terrain, Long>{

	List<Terrain> findByEnable(Boolean enable);
	
	@Query("select t from Terrain t where t not IN(select s.terrain from Session s where s.dateHeureDebut=?1) ")
	List<Terrain> findFreeTerrain(LocalDateTime localDateTime);

}
