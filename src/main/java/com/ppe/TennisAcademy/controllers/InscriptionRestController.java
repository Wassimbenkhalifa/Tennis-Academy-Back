/*package com.ppe.TennisAcademy.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.Inscription;
import com.ppe.TennisAcademy.entities.InscriptionDTO;
import com.ppe.TennisAcademy.services.ImplServices.IInscriptionService;
import com.ppe.TennisAcademy.services.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/inscription")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InscriptionRestController {
	@Autowired
	private InscriptionService inscriptionService;//last update

	@PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InscriptionDTO> saveInscription(@RequestBody InscriptionDTO inscriptionDTO) {

		Inscription request = Inscription.mapToInscription(inscriptionDTO);
     	Inscription result = (Inscription) this.inscriptionService.save(request);
		if (result != null) {
			return new ResponseEntity<>(InscriptionDTO.mapToInscriptionDTO(result), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PutMapping("/edit")
	 @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InscriptionDTO> editInscription(@RequestBody InscriptionDTO inscriptionDTO) {

		Inscription request = Inscription.mapToInscription(inscriptionDTO);
		Inscription result = this.inscriptionService.edit(request);
		if (result != null) {

			return new ResponseEntity<>(InscriptionDTO.mapToInscriptionDTO(result), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}


	@DeleteMapping("/{id}")
	// @PreAuthorize("hasRole('ADMIN')")
	public boolean deleteInscription(@PathVariable Long id) {
		InscriptionDTO inscriptionDTO = InscriptionDTO.mapToInscriptionDTO(this.inscriptionService.getById(id));
		if (inscriptionDTO != null) {
			this.inscriptionService.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InscriptionDTO> getById(@PathVariable Long id) {

		if (id != null) {
			Inscription inscription = this.inscriptionService.getById(id);
			if (inscription != null) {
				return new ResponseEntity<>(InscriptionDTO.mapToInscriptionDTO(inscription), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("")
	 @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<InscriptionDTO>> getAllInscriptions() {

		List<Inscription> inscriptionsList = this.inscriptionService.getAll();
		if (!inscriptionsList.isEmpty()) {
			List<InscriptionDTO> inscriptionsDTOList = inscriptionsList.stream()
					.map(s -> InscriptionDTO.mapToInscriptionDTO(s)).collect(Collectors.toList());
			return new ResponseEntity<>(inscriptionsDTOList, HttpStatus.OK);
		}
		return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
	}

}*/
