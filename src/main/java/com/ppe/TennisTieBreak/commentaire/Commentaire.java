package com.ppe.TennisTieBreak.commentaire;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.publication.Publication;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;

import lombok.Data;

@Entity
@Data
public class Commentaire {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCommentaire;
	
    @Column
    private Date localDateheureCommentaire;
    
    @Column
    private String textCommentaire;
    
    @ManyToOne  
    @JoinColumn(name="idPublication")
	private Publication publication;
    
    @ManyToOne  
    @JoinColumn(name="idUtilisateur")
	private Utilisateur utilisateur;
    
	public static Commentaire mapToCommentaire(CommentaireDTO commentaireDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(commentaireDTO, Commentaire.class));
	}

}
