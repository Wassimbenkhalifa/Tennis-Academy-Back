package com.ppe.TennisAcademy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ppe.TennisAcademy.entities.Media;


@Repository
public interface MediaRepository extends JpaRepository<Media, Long>{

}
