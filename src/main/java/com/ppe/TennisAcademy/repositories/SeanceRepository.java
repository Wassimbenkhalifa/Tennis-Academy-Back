package com.ppe.TennisAcademy.repositories;

import com.ppe.TennisAcademy.entities.Seance;
import com.ppe.TennisAcademy.entities.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long>{
List<Seance> findByTerrainAndDateHeureDebut(Terrain terrain, Date dateDebut);

//@Query(value = "SELECT s.idSeance, s.dateHeureDebut, s.dateHeureFin, t.label , c.label FROM Seance s left join "
      //  + "Planification p on s.planification.idPlanification = p.idPlanification left join Terrain t on s.terrain.idTerrain = t.idTerrain left JOIN "
     //   + "Cours c on p.cours.idCours = c.idCours where s.dateHeureDebut >= ?1 AND s.dateHeureDebut <= ?2")
//		@Query(value = "SELECT s.id_seance seeionId, s.date_heure_debut seanceDateDebut, s.date_heure_fin seanceDateFin, "
//				+ "s.type_seance, t.label terrain, c.label cours FROM seance s left join planification p on "
//				+ "s.id_planification = p.id_planification left join terrain t on s.id_terrain = t.id_terrain left JOIN "
//				+ "cours c on p.id_cours = c.id_cours where date(s.date_heure_debut) >= ?1 AND date(s.date_heure_debut) <= ?2", nativeQuery=true)
//List<Object> findSeanceByPeriod(LocalDateTime sdt , LocalDateTime edt );

//@Query("select s from Seance s  where s.planification.idPlanification in (select p.idPlanification from Planification p) ")
//		List<Seance> findAllPlanifiee();

//		@Query("select s from Seance s  where s.utilisateur.idUtilisateur in (select u.idUtilisateur from Utilisateur u) ")
//		List<Seance> findAllLibre();

        }
