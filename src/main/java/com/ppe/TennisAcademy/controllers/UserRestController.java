package com.ppe.TennisAcademy.controllers;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.ppe.TennisAcademy.config.UserDetailsImpl;
import com.ppe.TennisAcademy.config.VerificationTokenService;
import com.ppe.TennisAcademy.config.request.LoginRequest;
import com.ppe.TennisAcademy.config.request.SignupRequest;
import com.ppe.TennisAcademy.config.response.JwtResponse;
import com.ppe.TennisAcademy.config.response.MessageResponse;
import com.ppe.TennisAcademy.config.security.JwtUtils;
import com.ppe.TennisAcademy.entities.*;
import com.ppe.TennisAcademy.repositories.RoleRepository;
import com.ppe.TennisAcademy.repositories.SeanceLibreRepository;
import com.ppe.TennisAcademy.repositories.SessionRepository;
import com.ppe.TennisAcademy.services.IPlanificationService;
import com.ppe.TennisAcademy.services.ISessionService;
import com.ppe.TennisAcademy.services.ITerrainService;
import com.ppe.TennisAcademy.services.IMediaService;
import com.ppe.TennisAcademy.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.lang.Assert;

import static com.ppe.TennisAcademy.config.Pathconst.PATH_IMAGE_USER_PROFILE;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserRestController <T extends User> {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    protected UserService<T> userService;


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SeanceLibreRepository seanceLibreRepository;

    @Autowired
    private ITerrainService terrainService;

    @Autowired
    private IPlanificationService planificationService;

    @Autowired
    protected PasswordEncoder encoder;

    @Autowired
    protected VerificationTokenService verificationTokenService;

    @Autowired
    JwtUtils jwtUtils;
    private IMediaService mediaService;


    public UserRestController(UserService<T> userService) {
        this.userService = userService;
        Assert.notNull(this.userService, "user service is not implemented");
    }


    @GetMapping("/verifyverificationToken/{userId}/{verificationToken}")
    public void verifyverificationToken(@PathVariable("userId") Long userId, @PathVariable("verificationToken") String verificationToken) {
        verificationTokenService.verifyverificationToken(userId, verificationToken);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody User user) {
        List<String> strRoles = user.getRoles().stream().map((r) -> r.getName().toString()).collect(Collectors.toList());
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "ROLE_ADMIN":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role dmin is not found."));
                    roles.add(adminRole);

                    break;

                case "ROLE_COACH":
                    Role coachRole = roleRepository.findByName(ERole.ROLE_COACH)
                            .orElseThrow(() -> new RuntimeException("Error: Role Coach is not found."));
                    roles.add(coachRole);

                    break;

                default:
                    Role adherentRoleDefault = roleRepository.findByName(ERole.ROLE_ADHERENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role Adherent is not found."));
                    roles.add(adherentRoleDefault);
            }
        });
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(roles);
        userService.save((T) user);
//last update made by wassim
        verificationTokenService.generateverificationToken(user.getIdUser().toString());
        //send email
        String token = "http://localhost:8082/api/auth/verifyverificationToken/" + user.getIdUser() + "/" + verificationTokenService.getverificationToken(user.getIdUser().toString());
        System.out.println(token);
        //  mailService.sendEmail(user.getEmail(),token);
        return ResponseEntity.ok(new MessageResponse("User updated  successfully!"));
    }


    @GetMapping("/verifyusername/{username}")
    public Boolean existsByUsername(@PathVariable String username) {
        return this.userService.existsByUsername(username);
    }

    @GetMapping("/verifyemail/{email}")
    public Boolean existsByEmail(@PathVariable String email) {
        return this.userService.existsByEmail(email);
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (this.userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));


        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        System.out.println(signUpRequest.toString());

        System.out.println(user.toString());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role adherentRole = roleRepository.findByName(ERole.ROLE_ADHERENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role adherent is not found."));
            roles.add(adherentRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role admin is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_COACH":
                        Role coachRole = roleRepository.findByName(ERole.ROLE_COACH)
                                .orElseThrow(() -> new RuntimeException("Error: Role coach is not found."));
                        roles.add(coachRole);

                        break;

                    default:
                        Role userRoleDefault = roleRepository.findByName(ERole.ROLE_ADHERENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role user is not found."));
                        roles.add(userRoleDefault);
                }
            });
        }

        user.setRoles(roles);
        user.setVerified(false);
        user.setPrenom(signUpRequest.getPrenom());
        user.setNom(signUpRequest.getNom());
        userService.save((T) user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @GetMapping("")
    public ResponseEntity getAll() {
        Collection<T> users = userService.getAll();
        if (users != null && !users.isEmpty()) {
            List<UserDTO> results = users.
                    stream()
                    .map(u -> UserDTO.mapToUserDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/coach")
    public ResponseEntity getAllCoachs() {
        Collection<Coach> users = userService.getAllCoachs();
        if (users != null && !users.isEmpty()) {
            List<CoachDTO> results = users.
                    stream()
                    .map(u -> CoachDTO.mapToCoachDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/adherent")
    public ResponseEntity getAllAdherents() {
        Collection<Adherent> users = userService.getAllAdherents();
        if (users != null && !users.isEmpty()) {
            List<AdherentDTO> results = users.
                    stream()
                    .map(u -> AdherentDTO.mapToAdherentDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity getAllAdmins() {
        Collection<Admin> users = userService.getAllAdmins();
        if (users != null && !users.isEmpty()) {
            List<AdminDTO> results = users.
                    stream()
                    .map(u -> AdminDTO.mapToAdminDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        User users = userService.getByID(id);
        if (users != null) {
            return new ResponseEntity(UserDTO.mapToUserDTO(users), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @GetMapping(value = "/currentuser")
    @ResponseBody
    public Object currentUser() {

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user != null ? new ResponseEntity(UserDTO.mapUserToUserRole(userService.getByUserName(user.getUsername())), HttpStatus.OK) :
                new ResponseEntity(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/currentuser/{id}")

    public ResponseEntity currentUserbyid(@PathVariable("id") Long id) {

        return new ResponseEntity(UserDTO.mapUserToUserRole(userService.getByID(id)), HttpStatus.OK);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity updateCurrentUser(@RequestBody UserDTO userDTO) {
        MediaDTO photo = userDTO.getPhoto();

        User user = userService.getByID(userDTO.getIdUser());
        Media oldPhoto = user.getPhoto();
        if (photo.getFileName() != null && photo.getFileName() != "" && photo.getFile() != null) {
            photo.setFileName(mediaService.setFileName(photo.getFileName()));
            user.setPhoto(Media.mapToMedia(photo));

        } else {
            user.setPhoto(Media.mapToMedia(null));
        }

        Optional<Role> admin = roleRepository.findByName(ERole.ROLE_ADMIN);
        Optional<Role> coach = roleRepository.findByName(ERole.ROLE_COACH);
        Optional<Role> adherent = roleRepository.findByName(ERole.ROLE_ADHERENT);

        user.setUsername(userDTO.getUsername());
        user.setPrenom(userDTO.getPrenom());
        user.setNom(userDTO.getNom());
        user.setGender(userDTO.getGender());
        user.setDateNaissance(userDTO.getDateNaissance());
        user.setTelephone(userDTO.getTelephone());
        user.setAddresse(userDTO.getAddresse());
        user.setVerified(userDTO.getVerified());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        Set<Role> roles = userDTO.getRoles();
        if (roles.contains(coach)) {
            Coach result = (Coach) userService.save((T) user);
            result.setDateEngagement(userDTO.getDateEngagement());
            result = (Coach) userService.save((T) result);


            if (result != null && userDTO.getPhoto() != null && userDTO.getPhoto().getMediaURL() != null && oldPhoto != null) {
                mediaService.deleteFileByUrl(userDTO.getPhoto().getMediaURL());
                mediaService.upload(userDTO.getPhoto().getFile(), userDTO.getPhoto().getFileName(),
                        PATH_IMAGE_USER_PROFILE);
                return new ResponseEntity<>(UserDTO.mapToUserDTO(result), HttpStatus.OK);
            } else if (oldPhoto == null) {
                mediaService.upload(userDTO.getPhoto().getFile(), userDTO.getPhoto().getFileName(),
                        PATH_IMAGE_USER_PROFILE);
                return new ResponseEntity<>(UserDTO.mapToUserDTO(result), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.OK);

        } else if (roles.contains(coach)) {
            Adherent result = (Adherent) userService.save((T) user);
            result.setNbrMatchJoues(userDTO.getNbrMatchJoues());
            result = (Adherent) userService.save((T) result);


            if (result != null && userDTO.getPhoto() != null && userDTO.getPhoto().getMediaURL() != null && oldPhoto != null) {
                mediaService.deleteFileByUrl(userDTO.getPhoto().getMediaURL());
                mediaService.upload(userDTO.getPhoto().getFile(), userDTO.getPhoto().getFileName(),
                        PATH_IMAGE_USER_PROFILE);
                return new ResponseEntity<>(UserDTO.mapToUserDTO(result), HttpStatus.OK);
            } else if (oldPhoto == null) {
                mediaService.upload(userDTO.getPhoto().getFile(), userDTO.getPhoto().getFileName(),
                        PATH_IMAGE_USER_PROFILE);
                return new ResponseEntity<>(UserDTO.mapToUserDTO(result), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.OK);

        } else {
            Admin result = (Admin) userService.save((T) user);


            if (result != null && userDTO.getPhoto() != null && userDTO.getPhoto().getMediaURL() != null && oldPhoto != null) {
                mediaService.deleteFileByUrl(userDTO.getPhoto().getMediaURL());
                mediaService.upload(userDTO.getPhoto().getFile(), userDTO.getPhoto().getFileName(),
                        PATH_IMAGE_USER_PROFILE);
                return new ResponseEntity<>(UserDTO.mapToUserDTO(result), HttpStatus.OK);
            } else if (oldPhoto == null) {
                mediaService.upload(userDTO.getPhoto().getFile(), userDTO.getPhoto().getFileName(),
                        PATH_IMAGE_USER_PROFILE);
                return new ResponseEntity<>(UserDTO.mapToUserDTO(result), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.OK);

        }

    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        this.userService.deleteById(id);
    }

    @GetMapping("/search/{value}")
    public ResponseEntity findByName(@RequestParam String value) {

        Collection<T> results =
                this.userService
                        .findByUsernameOrDescriptionContainingIgnoreCase(value);
        if (!results.isEmpty()) {
            Collection<UserDTO> responses =
                    results
                            .stream()
                            .map(user -> UserDTO.mapUserToUserRole(user))
                            .collect(Collectors.toCollection(ArrayList::new));
            return new ResponseEntity(responses, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList(), HttpStatus.OK);
    }


    public static UserDTO mapUsersToDTO(User user) {

        if (user instanceof Admin) {
            return AdminDTO.mapToAdminDTO((Admin) user);
        }
        if (user instanceof Coach) {
            return CoachDTO.mapToCoachDTO((Coach) user);
        }
        if (user instanceof Coach) {
            return CoachDTO.mapToCoachDTO((Coach) user);
        } else {
            return UserDTO.mapToUserDTO(user);
        }
    }


    @PostMapping("/forgetPassword")
    public ResponseEntity forgetPassword(@RequestParam("email") String email) {
		/*
		String token=RandomString.make(120);

		try {
			userService.updateResetPasswordToken(token, email);

			String resetUpdatePasswordLink="http://localhost:8082/api/auth/resetPassword?token="+token;
			System.out.println("email = "+email);

			System.out.println("reset password link = "+resetUpdatePasswordLink);

			// send email

			mailService.sendResetPasswordEmail(email,resetUpdatePasswordLink);


		} catch (UserNotFoundException e) {
	        return new ResponseEntity("User with email = "+email+" not found", HttpStatus.OK);
		}

		*/

        return new ResponseEntity("Check the link on your email to reset your password", HttpStatus.OK);

    }

    @PostMapping("/resetPassword")
    public ResponseEntity ProcessResetPassword(@RequestParam("newPassword") String newPassword,
                                               @RequestParam("token") String token) {
        String message = "";
        UserService<User> usersService;
        User user = userService.get(token);
        if (user != null) {

            userService.updatePassword(user, newPassword);
            message = "User password succefully reset";
            return new ResponseEntity(HttpStatus.OK);

        } else {
            message = "User with token not found";
            return new ResponseEntity(HttpStatus.OK);

        }
    }

    @GetMapping("/infoAdmin")
    public ResponseEntity getInfoForAdmin() {
        Collection<T> users = userService.getAll();
        Collection<Session> sessions = sessionService.getAll();
        Collection<Planification> planification = planificationService.getAll();
        Collection<Terrain> terrain = terrainService.getAll();

        if (users != null && sessions != null && planification != null && terrain != null) {
            int nbrUser = 0;
            int nbrSeanceLibre = 0;
            int nbrSeancePlanifiee = 0;
            int nbrTerrain = 0;
            //int nbrPlanification = 0;

            if (!users.isEmpty())
                nbrUser = users.size();

            //if (!planification.isEmpty())
               // nbrPlanification = planification.size();

            if (!terrain.isEmpty())
                nbrTerrain = terrain.size();

            if (!sessions.isEmpty()) {
                for (Session sess : sessions) {
                    if (sess instanceof SeancesLibre)
                        nbrSeanceLibre = nbrSeanceLibre + 1;
                    if (sess instanceof SeancePlanifiee)
                        nbrSeancePlanifiee = nbrSeancePlanifiee + 1;
                }
            }

            HashMap<String, Integer> result = new HashMap<String, Integer>();//Creating HashMap.
            result.put("le nombre des users est : ", nbrUser); //Put elements in Map.
            result.put("le nombre des séances libres est : ", nbrSeanceLibre);
            result.put("le nombre des séances planifiées est : ", nbrSeancePlanifiee);
            result.put("le nombre des terrains est : ", nbrTerrain);
            //result.put("le nombre des séances libres est : ", nbrPlanification);

            return new ResponseEntity(result, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }

//    @GetMapping("/infoSessionAdmin")
//    public ResponseEntity getSessionAdmin() {
//        Collection<Session> sessions = sessionService.getAll();
//        HashMap<String, Integer> result = new HashMap<String, Integer>();//Creating HashMap.
//
//        if (sessions != null) {
//
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//            for (int i = 0; i < 12; i++) {
//                LocalDateTime now = LocalDateTime.now().minusMonths(i);
//                int nbrSessionLibre = 0;
//                int nbrSessionPlanifiee = 0;
//                switch (now.getMonthValue()) {
//
//                    case 1: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                int nbrSeanceLibre=0;
//                                if (sess instanceof SeancesLibre)
//                                    nbrSeanceLibre = nbrSeanceLibre + 1;
//                                int nbrSeancePlanifiee=0;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSeancePlanifiee = nbrSeancePlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//
//                    break;
//                    case 2: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    29 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 3: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 4: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    30 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 5: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 6: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    30 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//
//                    case 7: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 8: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 9: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    30 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 10: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    case 11: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    30 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                    default: {
//                        if ((now.getMonthValue() / 10) == 1) {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//                        } else {
//
//                            LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    01 + " 00:00", formatter);
//                            LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear() + "-0" + now.getMonthValue() + "-" +
//                                    31 + " 23:59", formatter);
//                            List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
//
//                            for (Session sess : sessions) {
//                                if (sess instanceof SeancesLibre)
//                                    nbrSessionLibre = nbrSessionLibre + 1;
//                                if (sess instanceof SeancePlanifiee)
//                                    nbrSessionPlanifiee = nbrSessionPlanifiee + 1;
//                            }
//
//                        }
//                    }
//                    break;
//                }
//
//                result.put("SL MOIS" + now.getMonthValue() + now.getYear(), nbrSessionLibre);
//                result.put("SP MOIS" + now.getMonthValue() + now.getYear(), nbrSessionPlanifiee);
//
//                System.out.println(result);
//
//            }
//
//
//            return new ResponseEntity(result, HttpStatus.OK);
//
//
//        }
//        return new ResponseEntity(null, HttpStatus.OK);
//    }
}
