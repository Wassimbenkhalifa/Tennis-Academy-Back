package com.ppe.TennisTieBreak.commentaire;

import java.util.List;

public interface ICommentaireService {

	Commentaire save(Commentaire commentaire);

	Commentaire edit(Commentaire commentaire);

	void deleteById(Long id);

	Commentaire getById(Long id);

	List<Commentaire> getAll();
}
