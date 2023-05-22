package com.ppe.TennisTieBreak.commentaire;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commentaire")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentaireRestController {

	@Autowired
	private ICommentaireService commentaireService;

	@PostMapping("")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommentaireDTO> saveCommentaire(@RequestBody CommentaireDTO commentaireDTO) {

		Commentaire request = Commentaire.mapToCommentaire(commentaireDTO);
		Commentaire result = this.commentaireService.save(request);
		if (result != null) {
			return new ResponseEntity<>(CommentaireDTO.mapToCommentaireDTO(result), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	@PutMapping("/edit")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommentaireDTO> editCommentaire(@RequestBody CommentaireDTO commentaireDTO) {

		Commentaire request = Commentaire.mapToCommentaire(commentaireDTO);
		Commentaire result = this.commentaireService.edit(request);
		if (result != null) {

			return new ResponseEntity<>(CommentaireDTO.mapToCommentaireDTO(result), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	

	@DeleteMapping("/{id}")
	// @PreAuthorize("hasRole('ADMIN')")
	public boolean deleteCommentaire(@PathVariable Long id) {
		CommentaireDTO commentaireDTO = CommentaireDTO.mapToCommentaireDTO(this.commentaireService.getById(id));
		if (commentaireDTO != null) {
			this.commentaireService.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CommentaireDTO> getById(@PathVariable Long id) {

		if (id != null) {
			Commentaire commentaire = this.commentaireService.getById(id);
			if (commentaire != null) {
				return new ResponseEntity<>(CommentaireDTO.mapToCommentaireDTO(commentaire), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CommentaireDTO>> getAllCommentaires() {

		List<Commentaire> commentairesList = this.commentaireService.getAll();
		if (!commentairesList.isEmpty()) {
			List<CommentaireDTO> commentairesDTOList = commentairesList.stream()
					.map(s -> CommentaireDTO.mapToCommentaireDTO(s)).collect(Collectors.toList());
			return new ResponseEntity<>(commentairesDTOList, HttpStatus.OK);
		}
		return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
	}
	
}
