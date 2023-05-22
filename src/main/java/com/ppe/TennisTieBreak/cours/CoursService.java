package com.ppe.TennisTieBreak.cours;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.commentaire.CommentaireRepository;


@Service
public class CoursService implements ICoursService{

	@Autowired
	private CoursRepository coursRepository;
	
	@Override
	public Cours save(Cours cours) {
        return this.coursRepository.save(cours);

	}

	@Override
	public Cours edit(Cours cours) {
        return this.coursRepository.save(cours);
	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.coursRepository.deleteById(id);
		}
	}

	@Override
	public Cours getById(Long id) {
		return this.coursRepository.getById(id);
	}

	@Override
	public List<Cours> getAll() {
        return this.coursRepository.findAll();
	}

}
