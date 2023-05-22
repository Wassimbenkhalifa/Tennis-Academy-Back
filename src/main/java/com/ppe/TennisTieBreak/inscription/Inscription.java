//package com.ppe.TennisTieBreak.inscription;
//
//import java.util.Date;
//import java.util.Set;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//
//import org.modelmapper.ModelMapper;
//
//import com.ppe.TennisTieBreak.cours.Cours;
//import com.ppe.TennisTieBreak.cours.CoursDTO;
//import com.ppe.TennisTieBreak.joueur.Joueur;
//import com.ppe.TennisTieBreak.planification.Planification;
//
//import lombok.Data;
//
//@Entity
//@Data
//public class Inscription {
//	
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long idInscription;
//	
//	private Date dateInscription;
//	
//    @OneToOne
//    @JoinColumn(name = "planification_id", referencedColumnName = "idPlanification")
//    private Planification planification;
//	
//    
//    @ManyToMany
//    @JoinTable(
//      name = "inscription_joueur", 
//      joinColumns = @JoinColumn(name = "joueur_id"), 
//      inverseJoinColumns = @JoinColumn(name = "planification_id"))
//    Set<Joueur> joueursInscrits;
//    
//    
//	public static Inscription mapToInscription(InscriptionDTO inscriptionDTO) {
//		ModelMapper modelMapper = new ModelMapper();
//		return (modelMapper.map(inscriptionDTO, Inscription.class));
//	}
//
//}
