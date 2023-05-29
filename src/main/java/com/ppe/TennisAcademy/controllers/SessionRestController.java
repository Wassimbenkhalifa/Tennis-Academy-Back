package com.ppe.TennisAcademy.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ppe.TennisAcademy.entities.*;
import com.ppe.TennisAcademy.repositories.SeanceLibreRepository;
import com.ppe.TennisAcademy.repositories.SeancePlanifieeRepository;
import com.ppe.TennisAcademy.repositories.SessionRepository;
import com.ppe.TennisAcademy.repositories.TerrainRepository;
import com.ppe.TennisAcademy.services.impl.SessionService;
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
@RequestMapping("/api/session")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SessionRestController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SeanceLibreRepository seanceLibreRepository;

    @Autowired
    private SeancePlanifieeRepository seancePlanifieeRepository;

    @Autowired
    private TerrainRepository terrainRepository;

    @PostMapping("/addPlanifiee")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDTO> saveSessionPlanifiee(@RequestBody SessionDTO sessionDTO) {
        Session request = Session.mapToSession(sessionDTO);
        Session result = this.sessionService.save(request);
        if (result != null) {
            return new ResponseEntity<>(SessionDTO.mapToSessionDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


//    @GetMapping("/test/{sdt}/{edt}")
//    public ResponseEntity<?> findSessionByPeriod(
//            @PathVariable String sdt,
//            @PathVariable String edt){
////		List<Object> result = this.sessionRepository.findSessionByPeriod(sdt, edt);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime dateTimeDebut = LocalDateTime.parse(sdt+" 00:00", formatter);
//        LocalDateTime dateTimeFin = LocalDateTime.parse(edt+" 23:59", formatter);
//
//        List<Object> result = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//
//        JSONArray parentJsonArray = new JSONArray();
//        // loop through your elements
//        for (int i=0; i<result.size(); i++){
//            JSONObject childJsonArray = new JSONObject();
//            Object[] row = (Object[]) result.get(i);
//            String s= String.valueOf(row[0]);
//
//            Long sessionId=Long.valueOf(s).longValue();
//
//            childJsonArray.put("sessionId", sessionId);
//            childJsonArray.put("dateDebut", String.valueOf(row[1]));
//            childJsonArray.put("dateFin", String.valueOf(row[2]));
////	        childJsonArray.put("sessionType", String.valueOf(row[3]));
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
////		Object[] result = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
////		if(result!=null) {
////			for(int i=0;i<result.length;i++) {
////				System.out.println(result);
////			}
////		}else System.out.println("no");
////		return this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//    }

    @PutMapping("/edit")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDTO> editSession(@RequestBody SessionDTO sessionDTO) {

        Session request = Session.mapToSession(sessionDTO);
        Session result = this.sessionService.edit(request);
        if (result != null) {

            return new ResponseEntity<>(SessionDTO.mapToSessionDTO(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteSession(@PathVariable Long id) {
        SessionDTO sessionDTO = SessionDTO.mapToSessionDTO(this.sessionService.getById(id));
        if (sessionDTO != null) {
            this.sessionService.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDTO> getById(@PathVariable Long id) {

        if (id != null) {
            Session session = this.sessionService.getById(id);
            if (session != null) {
                return new ResponseEntity<>(SessionDTO.mapToSessionDTO(session), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SessionDTO>> getAllSessions() {

        List<Session> sessionsList = this.sessionService.getAll();
        if (!sessionsList.isEmpty()) {
            System.out.println(sessionsList.get(0));
            List<SessionDTO> sessionsDTOList = sessionsList.stream().map(s -> SessionDTO.mapToSessionDTO(s))
                    .collect(Collectors.toList());
            System.out.println(sessionsDTOList.get(0));
            return new ResponseEntity<>(sessionsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

    @GetMapping("/planifiee")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeancePlanifieeDTO>> getAllPlanifieSessions() {

        List<SeancePlanifiee> sessionsList = this.sessionService.getAllPlanifiee();
        if (!sessionsList.isEmpty()) {
            List<SeancePlanifieeDTO> sessionsDTOList = sessionsList.stream()
                    .map(s -> SeancePlanifieeDTO.mapToSeancePlanifieeDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(sessionsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

    @GetMapping("/libre")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeanceLibreDTO>> getAllLibreSessions() {

        List<SeancesLibre> sessionsList = this.sessionService.getAllLibre();
        if (!sessionsList.isEmpty()) {
            List<SeanceLibreDTO> sessionsDTOList = sessionsList.stream()
                    .map(s -> SeanceLibreDTO.mapToSessionLibreDTO(s)).collect(Collectors.toList());
            return new ResponseEntity<>(sessionsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

}
