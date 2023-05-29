package com.ppe.TennisAcademy.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.ppe.TennisAcademy.entities.Cours;
import com.ppe.TennisAcademy.entities.CoursDTO;
import com.ppe.TennisAcademy.services.ICoursService;
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
@RequestMapping("/api/cours")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CoursRestController {

    @Autowired
    private ICoursService coursService;


    @PostMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoursDTO> saveCours(@RequestBody CoursDTO coursDTO) {

        Cours request = Cours.mapToCours(coursDTO);
        Cours result = this.coursService.save(request);
        if (result != null) {
            return new ResponseEntity<>(CoursDTO.mapToCoursDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    @PutMapping("/edit")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoursDTO> editCours(@RequestBody CoursDTO coursDTO) {

        Cours request = Cours.mapToCours(coursDTO);
        Cours result = this.coursService.edit(request);
        if (result != null) {

            return new ResponseEntity<>(CoursDTO.mapToCoursDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteCours(@PathVariable Long id) {
        CoursDTO coursDTO = CoursDTO.mapToCoursDTO(this.coursService.getById(id));
        if (coursDTO != null) {
            this.coursService.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoursDTO> getById(@PathVariable Long id) {

        if (id != null) {
            Cours cours = this.coursService.getById(id);
            if (cours != null) {
                return new ResponseEntity<>(CoursDTO.mapToCoursDTO(cours), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CoursDTO>> getAllCours() {

        List<Cours> coursList = this.coursService.getAll();
        if (!coursList.isEmpty()) {
            List<CoursDTO> coursDTOList = coursList.stream()
                    .map(s -> CoursDTO.mapToCoursDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(coursDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

}
