package com.ppe.TennisTieBreak.publication;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.commentaire.Commentaire;
import com.ppe.TennisTieBreak.commentaire.CommentaireDTO;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;
import com.ppe.TennisTieBreak.utilisateur.UtilisateurDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PublicationDTO {

    private Long idPublication;
	
    private String text;
    
    private LocalDateTime localDateheurePublication;
    
	private UtilisateurDTO utilisateur;
	
    private Set<CommentaireDTO> commentaires;

	
	public static PublicationDTO mapToPublicationDTO(Publication publication) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(publication, PublicationDTO.class);
	}
}
