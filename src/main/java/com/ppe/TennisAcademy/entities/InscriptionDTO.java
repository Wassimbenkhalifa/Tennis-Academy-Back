package com.ppe.TennisAcademy.entities;

import java.util.Date;
import java.util.Set;
import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InscriptionDTO {
    private Long idInscription;
    private Date dateInscription;
    private PlanificationDTO planificationDTO;
    Set<AdherentDTO> adherentsInscritsDTO;

	public static InscriptionDTO mapToInscriptionDTO(Inscription inscription) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(inscription, InscriptionDTO.class);
	}
}