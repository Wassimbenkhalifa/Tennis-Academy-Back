package com.ppe.TennisAcademy.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.Planification;
import com.ppe.TennisAcademy.entities.PlanificationDTO;
import com.ppe.TennisAcademy.entities.SeancePlanifiee;
import com.ppe.TennisAcademy.entities.Terrain;
import com.ppe.TennisAcademy.repositories.TerrainRepository;
import com.ppe.TennisAcademy.services.IPlanificationService;
import com.ppe.TennisAcademy.services.impl.PlanificationService;
import com.ppe.TennisAcademy.services.impl.SeancePlanifieeService;
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
@RequestMapping("/api/planification")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlanificationRestController {
    @Autowired
    private IPlanificationService planificationService;
    @Autowired
    private SeancePlanifieeService seancePlanifieeService;

    @Autowired
    private TerrainRepository terrainRepository;

    @PostMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanificationDTO> savePlanification(@RequestBody PlanificationDTO planificationDTO) {

        Planification request = Planification.mapToPlanification(planificationDTO);
        List<LocalDate> daysOfPlanification= PlanificationService.getDatesBetween(planificationDTO.getDateDebut(),
                planificationDTO.getDateFin());

        Planification result = this.planificationService.save(request);


        for(LocalDate day:daysOfPlanification) {
            SeancePlanifiee seancePlanifiee=new SeancePlanifiee();

            LocalDateTime dteDebut=LocalDateTime.of(day.getYear(),day.getMonth(),day.getDayOfMonth(),10,00);
            LocalDateTime dteFin=LocalDateTime.of(day.getYear(),day.getMonth(),day.getDayOfMonth(),11,00);
            seancePlanifiee.setDateHeureDebut(dteDebut);
            seancePlanifiee.setDateHeureFin(dteFin);


            List<Terrain> freeTerrain=this.terrainRepository.findFreeTerrain(dteDebut);
            if(!freeTerrain.isEmpty()) {
                seancePlanifiee.setTerrain(freeTerrain.get(0));
                seancePlanifiee.setPlanification(result);
                this.seancePlanifieeService.save(seancePlanifiee);

            }else {
                this.planificationService.deleteById(result.getIdPlanification());
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

        }

        if (result != null) {
            return new ResponseEntity<>(PlanificationDTO.mapToPlanificationDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/test")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Terrain>> test(@RequestBody PlanificationDTO planificationDTO) {



        String str = planificationDTO.getDateDebut()+" 10:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        List<Terrain> freeTerrain=this.terrainRepository.findFreeTerrain(dateTime);

        System.out.println(freeTerrain.get(0));
        List<LocalDate> daysOfPlanification=PlanificationService.getDatesBetween(planificationDTO.getDateDebut(),
                planificationDTO.getDateFin());



        return new ResponseEntity<List<Terrain>>(freeTerrain, HttpStatus.OK);
    }


    @PutMapping("/edit")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanificationDTO> editPlanification(@RequestBody PlanificationDTO planificationDTO) {

        Planification request = Planification.mapToPlanification(planificationDTO);
        Planification result = this.planificationService.edit(request);
        if (result != null) {

            return new ResponseEntity<>(PlanificationDTO.mapToPlanificationDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteCours(@PathVariable Long id) {
        PlanificationDTO planificationDTO = PlanificationDTO.mapToPlanificationDTO(this.planificationService.getById(id));
        if (planificationDTO != null) {
            this.planificationService.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanificationDTO> getById(@PathVariable Long id) {

        if (id != null) {
            Planification planification = this.planificationService.getById(id);
            if (planification != null) {
                return new ResponseEntity<>(PlanificationDTO.mapToPlanificationDTO(planification), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PlanificationDTO>> getAllPlanification() {

        List<Planification> planificationsList = this.planificationService.getAll();
        if (!planificationsList.isEmpty()) {
            List<PlanificationDTO> planificationsDTOList = planificationsList.stream()
                    .map(s -> PlanificationDTO.mapToPlanificationDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(planificationsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

}