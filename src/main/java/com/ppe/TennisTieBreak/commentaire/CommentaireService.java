package com.ppe.TennisTieBreak.commentaire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.publication.IPublicationService;

@Service
public class CommentaireService implements ICommentaireService{
	
	@Autowired
	private CommentaireRepository commentaireRepository;

	@Override
	public Commentaire save(Commentaire commentaire) {
        return this.commentaireRepository.save(commentaire);
	}

	@Override
	public Commentaire edit(Commentaire commentaire) {
        return this.commentaireRepository.save(commentaire);
	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.commentaireRepository.deleteById(id);
		}
	}

	@Override
	public Commentaire getById(Long id) {
		return this.commentaireRepository.getById(id);
	}

	@Override
	public List<Commentaire> getAll() {
		return this.commentaireRepository.findAll();

	}

}
