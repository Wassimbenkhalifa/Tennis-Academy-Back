package com.ppe.TennisAcademy.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.*;
import com.ppe.TennisAcademy.repositories.SeanceLibreRepository;
import com.ppe.TennisAcademy.repositories.SeancePlanifieeRepository;
import com.ppe.TennisAcademy.repositories.SeanceRepository;
import com.ppe.TennisAcademy.repositories.TerrainRepository;
import com.ppe.TennisAcademy.services.impl.PlanificationService;
import com.ppe.TennisAcademy.services.impl.SeanceService;
import com.ppe.TennisAcademy.services.impl.TerrainService;
import com.ppe.TennisAcademy.services.impl.UserService;
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
@RequestMapping("/api/seance")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SeanceRestController {
    @Autowired
    private SeanceService seanceService;

    @Autowired
    private UserService userService;

    @Autowired
    private TerrainService terrainService;

    @Autowired
    private PlanificationService planificationService;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private SeanceLibreRepository seanceLibreRepository;

    @Autowired
    private SeancePlanifieeRepository seancePlanifieeRepository;

    @Autowired
    private TerrainRepository terrainRepository;

    @PostMapping("/add")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeanceDTO> saveSeancePlanifiee(@RequestBody SeanceDTO seanceDTO) {
        Seance request = Seance.mapToSeance(seanceDTO);
        Seance result = this.seanceService.save(request);
        if (result != null) {
            return new ResponseEntity<>(SeanceDTO.mapToSeanceDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


//    @GetMapping("/test/{sdt}/{edt}")
//    public ResponseEntity<?> findSeanceByPeriod(
//            @PathVariable String sdt,
//            @PathVariable String edt){
////		List<Object> result = this.seanceRepository.findSeanceByPeriod(sdt, edt);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime dateTimeDebut = LocalDateTime.parse(sdt+" 00:00", formatter);
//        LocalDateTime dateTimeFin = LocalDateTime.parse(edt+" 23:59", formatter);
//
//        List<Object> result = this.seanceRepository.findSeanceByPeriod(dateTimeDebut, dateTimeFin);
//
//
//        JSONArray parentJsonArray = new JSONArray();
//        // loop through your elements
//        for (int i=0; i<result.size(); i++){
//            JSONObject childJsonArray = new JSONObject();
//            Object[] row = (Object[]) result.get(i);
//            String s= String.valueOf(row[0]);
//
//            Long seanceId=Long.valueOf(s).longValue();
//
//            childJsonArray.put("seanceId", seanceId);
//            childJsonArray.put("dateDebut", String.valueOf(row[1]));
//            childJsonArray.put("dateFin", String.valueOf(row[2]));
////	        childJsonArray.put("seanceType", String.valueOf(row[3]));
//            childJsonArray.put("terrain", String.valueOf(row[3]));
//            childJsonArray.put("cours", String.valueOf(row[4]));
//
//
//            parentJsonArray.add(childJsonArray);
//        }
//
//        String jsonA = JSONArray.toJSONString(parentJsonArray);
//        if (result != null) {
//            return new ResponseEntity<>(jsonA, HttpStatus.OK);
//        } else
//            return new ResponseEntity<>(null, HttpStatus.OK);
//
////		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
////		LocalDateTime dateTimeDebut = LocalDateTime.parse(sdt+" 00:00", formatter);
////		LocalDateTime dateTimeFin = LocalDateTime.parse(edt+" 23:59", formatter);
////
////		Object[] result = this.seanceRepository.findSeanceByPeriod(dateTimeDebut, dateTimeFin);
////		if(result!=null) {
////			for(int i=0;i<result.length;i++) {
////				System.out.println(result);
////			}
////		}else System.out.println("no");
////		return this.seanceRepository.findSeanceByPeriod(dateTimeDebut, dateTimeFin);
//
//    }

    @PutMapping("/edit")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeanceDTO> editSeance(@RequestBody SeanceDTO2 seanceDTO) {

        Seance request = new Seance();

        request.setDateHeureDebut(seanceDTO.getDateHeureDebut());
        request.setDateHeureFin(seanceDTO.getDateHeureFin());
        request.setIdSeance(seanceDTO.getIdSeance());

        Terrain terrain = this.terrainService.getById(seanceDTO.getTerrain());
        Planification planification = this.planificationService.getById(seanceDTO.getPlanification());
        User user = this.userService.getByID(seanceDTO.getUser());

        request.setUser(user);
        request.setPlanification(planification);
        request.setTerrain(terrain);

        Seance result = this.seanceService.edit(request);
        if (result != null) {

            return new ResponseEntity<>(SeanceDTO.mapToSeanceDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteSeance(@PathVariable Long id) {
        SeanceDTO seanceDTO = SeanceDTO.mapToSeanceDTO(this.seanceService.getById(id));
        if (seanceDTO != null) {
            this.seanceService.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeanceDTO> getById(@PathVariable Long id) {

        if (id != null) {
            Seance seance = this.seanceService.getById(id);
            if (seance != null) {
                return new ResponseEntity<>(SeanceDTO.mapToSeanceDTO(seance), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeanceDTO>> getAllSeances() {

        List<Seance> seancesList = this.seanceService.getAll();
        if (!seancesList.isEmpty()) {
            System.out.println(seancesList.get(0));
            List<SeanceDTO> seancesDTOList = seancesList.stream().map(s -> SeanceDTO.mapToSeanceDTO(s))
                    .collect(Collectors.toList());
            System.out.println(seancesDTOList.get(0));
            return new ResponseEntity<>(seancesDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

    @GetMapping("/planifiee")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeancePlanifieeDTO>> getAllPlanifieSeances() {

        List<SeancePlanifiee> seancesList = this.seanceService.getAllPlanifiee();
        if (!seancesList.isEmpty()) {
            List<SeancePlanifieeDTO> seancesDTOList = seancesList.stream()
                    .map(s -> SeancePlanifieeDTO.mapToSeancePlanifieeDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(seancesDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

    @GetMapping("/libre")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeanceLibreDTO>> getAllLibreSeances() {

        List<SeancesLibre> seancesList = this.seanceService.getAllLibre();
        if (!seancesList.isEmpty()) {
            List<SeanceLibreDTO> seancesDTOList = seancesList.stream()
                    .map(s -> SeanceLibreDTO.mapToSeanceLibreDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(seancesDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

}
