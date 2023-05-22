package com.ppe.TennisTieBreak.publication;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.modelmapper.ModelMapper;

import com.ppe.TennisTieBreak.commentaire.Commentaire;
import com.ppe.TennisTieBreak.utilisateur.Utilisateur;

import lombok.Data;

@Entity
@Data
public class Publication {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPublication;
    
    @Column
    private String text;
    
    @Column
    private LocalDateTime localDateheurePublication;
    
    @ManyToOne  
    @JoinColumn(name="idUtilisateur")
	private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "publication",cascade = CascadeType.REMOVE)
    private Set<Commentaire> commentaires;
    
	public static Publication mapToPublication(PublicationDTO publicationDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return (modelMapper.map(publicationDTO, Publication.class));
	}

}
