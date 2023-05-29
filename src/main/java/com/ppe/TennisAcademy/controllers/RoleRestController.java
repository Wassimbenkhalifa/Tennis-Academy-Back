package com.ppe.TennisAcademy.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.Role;
import com.ppe.TennisAcademy.entities.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ppe.TennisAcademy.repositories.RoleRepository;


@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleRestController {

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("")
    public ResponseEntity getAll() {
        Collection<Role> roles = roleRepository.findAll();
        if (roles != null && !roles.isEmpty()) {
            List<RoleDTO> results = roles.
                    stream()
                    .map(r -> RoleDTO.mapRoleToRoleDTO(r))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }

}