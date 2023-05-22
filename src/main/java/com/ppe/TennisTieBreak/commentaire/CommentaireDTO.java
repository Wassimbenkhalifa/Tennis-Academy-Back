package com.ppe.TennisTieBreak.commentaire;


import java.util.Date;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.publication.Publication;
import com.ppe.TennisTieBreak.publication.PublicationDTO;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;
import com.ppe.TennisTieBreak.utilisateur.UtilisateurDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentaireDTO {
	
    private Long idCommentaire;
    
    private String textCommentaire;
    
    private Date localDateheureCommentaire;
    
	private PublicationDTO publication;
	
	private UtilisateurDTO utilisateur;
    
	public static CommentaireDTO mapToCommentaireDTO(Commentaire commentaire) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(commentaire, CommentaireDTO.class);
	}

}
