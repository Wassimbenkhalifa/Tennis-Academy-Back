package com.ppe.TennisTieBreak.session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppe.TennisTieBreak.terrain.Terrain;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{

	List<Session> findByTerrainAndDateHeureDebut(Terrain terain, Date dateDebut);
		
		@Query(value = "SELECT s.idSession, s.dateHeureDebut, s.dateHeureFin, t.label , c.label FROM Session s left join "
				+ "Planification p on s.planification.idPlanification = p.idPlanification left join Terrain t on s.terrain.idTerrain = t.idTerrain left JOIN "
				+ "Cours c on p.cours.idCours = c.idCours where s.dateHeureDebut >= ?1 AND s.dateHeureDebut <= ?2")
//		@Query(value = "SELECT s.id_session seeionId, s.date_heure_debut sessionDateDebut, s.date_heure_fin sessionDateFin, "
//				+ "s.type_session, t.label terrain, c.label cours FROM session s left join planification p on "
//				+ "s.id_planification = p.id_planification left join terrain t on s.id_terrain = t.id_terrain left JOIN "
//				+ "cours c on p.id_cours = c.id_cours where date(s.date_heure_debut) >= ?1 AND date(s.date_heure_debut) <= ?2", nativeQuery=true)
		List<Object> findSessionByPeriod(LocalDateTime sdt , LocalDateTime edt );

		@Query("select s from Session s  where s.planification.idPlanification in (select p.idPlanification from Planification p) ")
		List<Session> findAllPlanifiee();
		
//		@Query("select s from Session s  where s.utilisateur.idUtilisateur in (select u.idUtilisateur from Utilisateur u) ")
//		List<Session> findAllLibre();

}
