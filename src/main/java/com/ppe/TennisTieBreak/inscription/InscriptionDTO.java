//package com.ppe.TennisTieBreak.inscription;
//
//import java.util.Date;
//import java.util.Set;
//
//import org.modelmapper.ModelMapper;
//
//import com.ppe.TennisTieBreak.joueur.JoueurDTO;
//import com.ppe.TennisTieBreak.planification.PlanificationDTO;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class InscriptionDTO {
//
//	
//    private Long idInscription;
//
//	private Date dateInscription;
//
//    private PlanificationDTO planificationDTO;
//
//    Set<JoueurDTO> joueursInscritsDTO;
//
//	public static InscriptionDTO mapToInscriptionDTO(Inscription inscription) {
//		ModelMapper modelMapper = new ModelMapper();
//		return modelMapper.map(inscription, InscriptionDTO.class);
//	}
//}
