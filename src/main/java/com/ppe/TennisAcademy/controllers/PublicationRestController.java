package com.ppe.TennisAcademy.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.Publication;
import com.ppe.TennisAcademy.entities.PublicationDTO;
import com.ppe.TennisAcademy.services.ImplServices.IPublicationService;
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
@RequestMapping("/api/publication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicationRestController {

    @Autowired
    private IPublicationService publicationService;

    @PostMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicationDTO> savePublication(@RequestBody PublicationDTO publicationDTO) {

        Publication request = Publication.mapToPublication(publicationDTO);
        Publication result = this.publicationService.save(request);
        if (result != null) {
            return new ResponseEntity<>(PublicationDTO.mapToPublicationDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    @PutMapping("/edit")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicationDTO> editPublication(@RequestBody PublicationDTO publicationDTO) {

        Publication request = Publication.mapToPublication(publicationDTO);
        Publication result = this.publicationService.edit(request);
        if (result != null) {

            return new ResponseEntity<>(PublicationDTO.mapToPublicationDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public boolean deletePublication(@PathVariable Long id) {
        PublicationDTO publicationDTO = PublicationDTO.mapToPublicationDTO(this.publicationService.getById(id));
        if (publicationDTO != null) {
            this.publicationService.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublicationDTO> getById(@PathVariable Long id) {

        if (id != null) {
            Publication publication = this.publicationService.getById(id);
            if (publication != null) {
                return new ResponseEntity<>(PublicationDTO.mapToPublicationDTO(publication), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PublicationDTO>> getAllPublications() {

        List<Publication> publicationsList = this.publicationService.getAll();
        if (!publicationsList.isEmpty()) {
            List<PublicationDTO> publicationsDTOList = publicationsList.stream()
                    .map(s -> PublicationDTO.mapToPublicationDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(publicationsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

}
