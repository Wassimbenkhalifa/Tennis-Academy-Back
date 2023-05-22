package com.ppe.TennisTieBreak.utilisateur;

import static com.ppe.TennisTieBreak.config.Pathconst.PATH_IMAGE_USER_PROFILE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

import com.ppe.TennisTieBreak.admin.Admin;
import com.ppe.TennisTieBreak.admin.AdminDTO;
import com.ppe.TennisTieBreak.config.Pathconst;
import com.ppe.TennisTieBreak.config.UtilisateurDetailsImpl;
import com.ppe.TennisTieBreak.config.VerificationTokenService;
import com.ppe.TennisTieBreak.config.request.LoginRequest;
import com.ppe.TennisTieBreak.config.request.SignupRequest;
import com.ppe.TennisTieBreak.config.response.JwtResponse;
import com.ppe.TennisTieBreak.config.response.MessageResponse;
import com.ppe.TennisTieBreak.config.security.JwtUtils;
import com.ppe.TennisTieBreak.entraineur.Entraineur;
import com.ppe.TennisTieBreak.entraineur.EntraineurDTO;
import com.ppe.TennisTieBreak.joueur.Joueur;
import com.ppe.TennisTieBreak.joueur.JoueurDTO;
import com.ppe.TennisTieBreak.media.IMediaService;
import com.ppe.TennisTieBreak.media.Media;
import com.ppe.TennisTieBreak.media.MediaDTO;
import com.ppe.TennisTieBreak.planification.IPlanificationService;
import com.ppe.TennisTieBreak.planification.Planification;
import com.ppe.TennisTieBreak.role.ERole;
import com.ppe.TennisTieBreak.role.Role;
import com.ppe.TennisTieBreak.role.RoleDTO;
import com.ppe.TennisTieBreak.role.RoleRepository;
import com.ppe.TennisTieBreak.session.ISessionService;
import com.ppe.TennisTieBreak.session.Session;
import com.ppe.TennisTieBreak.session.SessionRepository;
import com.ppe.TennisTieBreak.sessionLibre.SessionLibre;
import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;
import com.ppe.TennisTieBreak.terrain.ITerrainService;
import com.ppe.TennisTieBreak.terrain.Terrain;
import com.ppe.TennisTieBreak.terrain.TerrainDTO;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UtilisateurRestController <T extends Utilisateur> {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    protected UtilisateurService<T> utilisateurService;
    
    @Autowired
    protected IMediaService mediaService;
    
    
    @Autowired
    RoleRepository roleRepository;
    
	@Autowired
	private ISessionService sessionService;
	
	@Autowired
	private SessionRepository sessionRepository;
	
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



    public UtilisateurRestController(UtilisateurService<T> userService) {
        this.utilisateurService = userService;
        Assert.notNull(this.utilisateurService, "user service is not implemented");
    }



    @GetMapping("/verifyverificationToken/{userId}/{verificationToken}")
    public void verifyverificationToken(@PathVariable("userId") Long userId, @PathVariable("verificationToken") String verificationToken) {
        verificationTokenService.verifyverificationToken(userId,verificationToken);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Utilisateur user) {
        List<String> strRoles = user.getRoles().stream().map((r) -> r.getName().toString()).collect(Collectors.toList());
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role joueurRole = roleRepository.findByName(ERole.ROLE_JOUEUR)
                    .orElseThrow(() -> new RuntimeException("Error: Role joueur is not found."));
            roles.add(joueurRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role dmin is not found."));
                        roles.add(adminRole);

                        break;

                    case "ROLE_ENTRAINEUR":
                        Role entraineurRole = roleRepository.findByName(ERole.ROLE_ENTRAINEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role ENTRAINEUR is not found."));
                        roles.add(entraineurRole);

                        break;

                    default:
                        Role joueurRoleDefault = roleRepository.findByName(ERole.ROLE_JOUEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role user is not found."));
                        roles.add(joueurRoleDefault);
                }
            });
        }
        user.setPassword( encoder.encode(user.getPassword()));
        user.setRoles(roles);
        utilisateurService.save((T) user);

        verificationTokenService.generateverificationToken(user.getIdUtilisateur().toString());
        //send email
        String token="http://localhost:8080/api/auth/verifyverificationToken/"+user.getIdUtilisateur()+"/"+verificationTokenService.getverificationToken(user.getIdUtilisateur().toString());
        System.out.println(token);
      //  mailService.sendEmail(user.getEmail(),token);
        return ResponseEntity.ok(new MessageResponse("User updated  successfully!"));
    }


    @GetMapping("/verifyusername/{username}")
    public Boolean existsByUsername(@PathVariable  String username){
        return this.utilisateurService.existsByUsername(username);
    }

    @GetMapping("/verifyemail/{email}")
    public Boolean existsByEmail(@PathVariable  String email){
        return this.utilisateurService.existsByEmail(email);
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UtilisateurDetailsImpl userDetails = (UtilisateurDetailsImpl) authentication.getPrincipal();
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
        if (this.utilisateurService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (utilisateurService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Utilisateur user = new Utilisateur(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        
        System.out.println(signUpRequest.toString());

        System.out.println(user.toString());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role joueurRole = roleRepository.findByName(ERole.ROLE_JOUEUR)
                    .orElseThrow(() -> new RuntimeException("Error: Role user is not found."));
            roles.add(joueurRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role admin is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_ENTRAINEUR":
                        Role entraineurRole = roleRepository.findByName(ERole.ROLE_ENTRAINEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role entraineur is not found."));
                        roles.add(entraineurRole);

                        break;

                    default:
                        Role userRoleDefault = roleRepository.findByName(ERole.ROLE_JOUEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role user is not found."));
                        roles.add(userRoleDefault);
                }
            });
        }

        user.setRoles(roles);
        user.setVerified(false);
        utilisateurService.save((T) user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/{id}")
    public ResponseEntity registerUser(@PathVariable("id") long id, MediaDTO photoProfile) {
        Utilisateur user = utilisateurService.getByID(id);
        photoProfile.setFileName(mediaService.setFileName(photoProfile.getFileName()));
        user.setPhoto(Media.mapToMedia(photoProfile));
        utilisateurService.save((T) user);
        mediaService.upload(photoProfile.getFile(), photoProfile.getFileName(), Pathconst.PATH_IMAGE_USER_PROFILE);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @GetMapping("")
    public ResponseEntity getAll() {
        Collection<T> users =utilisateurService.getAll();
        if (users != null && !users.isEmpty()) {
            List<UtilisateurDTO> results = users.
                    stream()
                    .map(u -> UtilisateurDTO.mapToUserDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }
    
    @GetMapping("/entraineur")
    public ResponseEntity getAllEntraineur() {
        Collection<Entraineur> users =utilisateurService.getAllEntraineurs();
        if (users != null && !users.isEmpty()) {
            List<EntraineurDTO> results = users.
                    stream()
                    .map(u -> EntraineurDTO.mapToEntraineurDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }
    
    @GetMapping("/joueur")
    public ResponseEntity getAllJoueurs() {
        Collection<Joueur> users =utilisateurService.getAllJoueurs();
        if (users != null && !users.isEmpty()) {
            List<JoueurDTO> results = users.
                    stream()
                    .map(u -> JoueurDTO.mapToJoueurDTO(u))
                    .collect(Collectors.toList());
            return new ResponseEntity(results, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
    }
    
    @GetMapping("/admin")
    public ResponseEntity getAllAdmins() {
        Collection<Admin> users =utilisateurService.getAllAdmins();
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
        Utilisateur users = utilisateurService.getByID(id);
        if (users != null) {      	
            return new ResponseEntity(UtilisateurDTO.mapToUserDTO(users), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @GetMapping(value = "/currentuser")
    @ResponseBody
    public Object  currentUser() {

        UtilisateurDetailsImpl user = (UtilisateurDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user!=null ?  new ResponseEntity(UtilisateurDTO.mapUserToUserRole(utilisateurService.getByUserName(user.getUsername())), HttpStatus.OK) :
                new ResponseEntity(null, HttpStatus.FORBIDDEN) ;
    }

    @GetMapping(value = "/currentuser/{id}")

    public ResponseEntity currentUserbyid(@PathVariable("id") Long id) {

        return  new ResponseEntity(UtilisateurDTO.mapUserToUserRole(utilisateurService.getByID(id)), HttpStatus.OK);//UserDTO.mapUserToUserRole(userService.getByUserName(userName));
    }

    @PutMapping(value = "/edit")
    public ResponseEntity updateCurrentUser(@RequestBody UtilisateurDTO utilisateurDTO) {
    	MediaDTO photo = utilisateurDTO.getPhoto();

    	Utilisateur user=utilisateurService.getByID(utilisateurDTO.getIdUtilisateur());
    	Media oldPhoto=user.getPhoto();
		if (photo.getFileName() != null && photo.getFileName() != "" && photo.getFile() != null) {
			photo.setFileName(mediaService.setFileName(photo.getFileName()));
			user.setPhoto(Media.mapToMedia(photo));

		} else {
			user.setPhoto(Media.mapToMedia(null));
		}
		
    	Optional<Role> admin=roleRepository.findByName(ERole.ROLE_ADMIN);
    	Optional<Role> entraineur=roleRepository.findByName(ERole.ROLE_ENTRAINEUR);
    	Optional<Role> joueur=roleRepository.findByName(ERole.ROLE_JOUEUR);

		user.setUsername(utilisateurDTO.getUsername());
		user.setPrenom(utilisateurDTO.getPrenom());
		user.setNom(utilisateurDTO.getNom());
		user.setGender(utilisateurDTO.getGender());
		user.setDateNaissance(utilisateurDTO.getDateNaissance());
		user.setTelephone(utilisateurDTO.getTelephone());
		user.setAddresse(utilisateurDTO.getAddresse());
		user.setVerified(utilisateurDTO.getVerified());
    	user.setEmail(utilisateurDTO.getEmail());
    	user.setUsername(utilisateurDTO.getUsername());
    	
    	Set<Role> roles=utilisateurDTO.getRoles();
    	if(roles.contains(entraineur)) {
        	Entraineur result=(Entraineur) utilisateurService.save((T)user);
        	result.setDateEngagement(utilisateurDTO.getDateEngagement());
        	result=(Entraineur) utilisateurService.save((T)result);
        	

    		if (result != null && utilisateurDTO.getPhoto()!=null && utilisateurDTO.getPhoto().getMediaURL()!=null && oldPhoto!=null) {
    			mediaService.deleteFileByUrl(utilisateurDTO.getPhoto().getMediaURL());
    			mediaService.upload(utilisateurDTO.getPhoto().getFile(), utilisateurDTO.getPhoto().getFileName(),
    					PATH_IMAGE_USER_PROFILE);
    			return new ResponseEntity<>(UtilisateurDTO.mapToUserDTO(result), HttpStatus.OK);
    		}
    		else if(oldPhoto==null) {
    			mediaService.upload(utilisateurDTO.getPhoto().getFile(), utilisateurDTO.getPhoto().getFileName(),
    					PATH_IMAGE_USER_PROFILE);
    			return new ResponseEntity<>(UtilisateurDTO.mapToUserDTO(result), HttpStatus.OK);
    		}
    		return new ResponseEntity<>(null, HttpStatus.OK);
    		
    	}else if(roles.contains(entraineur)) {
        	Joueur result=(Joueur) utilisateurService.save((T)user);
        	result.setNbrMatchJoues(utilisateurDTO.getNbrMatchJoues());
        	result=(Joueur) utilisateurService.save((T)result);
        	

    		if (result != null && utilisateurDTO.getPhoto()!=null && utilisateurDTO.getPhoto().getMediaURL()!=null && oldPhoto!=null) {
    			mediaService.deleteFileByUrl(utilisateurDTO.getPhoto().getMediaURL());
    			mediaService.upload(utilisateurDTO.getPhoto().getFile(), utilisateurDTO.getPhoto().getFileName(),
    					PATH_IMAGE_USER_PROFILE);
    			return new ResponseEntity<>(UtilisateurDTO.mapToUserDTO(result), HttpStatus.OK);
    		}
    		else if(oldPhoto==null) {
    			mediaService.upload(utilisateurDTO.getPhoto().getFile(), utilisateurDTO.getPhoto().getFileName(),
    					PATH_IMAGE_USER_PROFILE);
    			return new ResponseEntity<>(UtilisateurDTO.mapToUserDTO(result), HttpStatus.OK);
    		}
    		return new ResponseEntity<>(null, HttpStatus.OK);
    		
        }else {
        	Admin result=(Admin) utilisateurService.save((T)user);
        	

    		if (result != null && utilisateurDTO.getPhoto()!=null && utilisateurDTO.getPhoto().getMediaURL()!=null && oldPhoto!=null) {
    			mediaService.deleteFileByUrl(utilisateurDTO.getPhoto().getMediaURL());
    			mediaService.upload(utilisateurDTO.getPhoto().getFile(), utilisateurDTO.getPhoto().getFileName(),
    					PATH_IMAGE_USER_PROFILE);
    			return new ResponseEntity<>(UtilisateurDTO.mapToUserDTO(result), HttpStatus.OK);
    		}
    		else if(oldPhoto==null) {
    			mediaService.upload(utilisateurDTO.getPhoto().getFile(), utilisateurDTO.getPhoto().getFileName(),
    					PATH_IMAGE_USER_PROFILE);
    			return new ResponseEntity<>(UtilisateurDTO.mapToUserDTO(result), HttpStatus.OK);
    		}
    		return new ResponseEntity<>(null, HttpStatus.OK);
    		
        }
    	
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        this.utilisateurService.deleteById(id);
    }

    @GetMapping("/search/{value}")
    public ResponseEntity findByName(@RequestParam String value){

        Collection<T>results =
                this.utilisateurService
                .findByUsernameOrDescriptionContainingIgnoreCase(value);
        if(!results.isEmpty()){
            Collection<UtilisateurDTO> responses =
                    results
                    .stream()
                    .map(user -> UtilisateurDTO.mapUserToUserRole(user))
                    .collect(Collectors.toCollection(ArrayList::new));
            return new ResponseEntity(responses, HttpStatus.OK);
        }
        return new ResponseEntity(new ArrayList(), HttpStatus.OK);
    }
    

    public static UtilisateurDTO mapUsersToDTO(Utilisateur user) {

        if (user instanceof Admin) {
            return AdminDTO.mapToAdminDTO((Admin) user);
        }
        if (user instanceof Entraineur) {
            return EntraineurDTO.mapToEntraineurDTO((Entraineur) user);
        }
        if (user instanceof Joueur) {
            return JoueurDTO.mapToJoueurDTO((Joueur) user);
        } else {
            return UtilisateurDTO.mapToUserDTO(user);
        }
    }
    
	
	@PostMapping("/forgetPassword")
	public ResponseEntity forgetPassword(@RequestParam ("email") String email){
		/*
		String token=RandomString.make(120);
		
		try {
			utilisateurService.updateResetPasswordToken(token, email);
			
			String resetUpdatePasswordLink="http://localhost:8081/api/auth/resetPassword?token="+token;
			System.out.println("email = "+email);

			System.out.println("reset password link = "+resetUpdatePasswordLink);
			
			// send email
			
			mailService.sendResetPasswordEmail(email,resetUpdatePasswordLink);

			
		} catch (UtilisateurNotFoundExeption e) {
	        return new ResponseEntity("User with email = "+email+" not found", HttpStatus.OK);
		}

		*/
		
        return new ResponseEntity("Check the link on your email to reset your password", HttpStatus.OK);

	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity ProcessResetPassword(@RequestParam ("newPassword") String newPassword,
												@RequestParam ("token") String token) {
		String message="";
		Utilisateur user=utilisateurService.get(token);
		 if(user!=null) {
			 
			 utilisateurService.updatePassword(user, newPassword);
			 message="User password succefully reset";
		        return new ResponseEntity(HttpStatus.OK); 

		 }else {
			 message="User with token not found";
		        return new ResponseEntity(HttpStatus.OK); 

		 }
	}
	
    @GetMapping("/infoAdmin")
    public ResponseEntity getInfoForAdmin() {
        Collection<T> users = utilisateurService.getAll();
        Collection<Session> sessions=sessionService.getAll();
        Collection<Planification> planification=planificationService.getAll();
        Collection<Terrain> terrain=terrainService.getAll();

        if (users != null && sessions != null && planification != null && terrain != null) {
        	int nbrUtilisateur=0;
        	int nbrSessionLibre=0;
        	int nbrSessionPlanifiee=0;
        	int nbrTerrain=0;
        	int nbrPlanification=0;

        	if(!users.isEmpty())
        		nbrUtilisateur=users.size();
        	
        	if(!planification.isEmpty())
        		nbrPlanification=planification.size();
        	
        	if(!terrain.isEmpty())
        		nbrTerrain=terrain.size();
        	
        	if(!sessions.isEmpty()) {
        		for(Session sess:sessions) {
        			if(sess instanceof SessionLibre)
        				nbrSessionLibre=nbrSessionLibre+1;
        			if(sess instanceof SessionPlanifiee)
        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
        		}
        	}
        	
        	HashMap<String,Integer> result=new HashMap<String,Integer>();//Creating HashMap.
        	result.put("nbrUtilisateur",nbrUtilisateur); //Put elements in Map.
        	result.put("nbrSessionLibre",nbrSessionLibre);
        	result.put("nbrSessionPlanifiee",nbrSessionPlanifiee);
        	result.put("nbrTerrain",nbrTerrain);
        	result.put("nbrPlanification",nbrPlanification);

            return new ResponseEntity(result, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }
    
    @GetMapping("/infoSessionAdmin")
    public ResponseEntity getSessionAdmin() {
      Collection<Session> sessions=sessionService.getAll();
	  	HashMap<String,Integer> result=new HashMap<String,Integer>();//Creating HashMap.
	  	
	  	if (sessions != null) {

	    	
	
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    	for(int i=0;i<12;i++)
	    	{
	    		LocalDateTime now =  LocalDateTime.now().minusMonths(i); 
		    	int nbrSessionLibre=0;
		    	int nbrSessionPlanifiee=0;
		    	  switch(now.getMonthValue()){
		    	   
		          case 1: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }

		              break;
		          case 2: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				29+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 3: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 4: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				30+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 5: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 6: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				30+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		              
		          case 7: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 8: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 9: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				30+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 10: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          case 11: 
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				30+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		          default:
		          {
		        	  if((now.getMonthValue()/10)==1) {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
				    	}else {

							LocalDateTime dateTimeDebut = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				01+" 00:00", formatter);
							LocalDateTime dateTimeFin = LocalDateTime.parse(now.getYear()+"-0"+now.getMonthValue()+"-"+
				    				31+" 23:59", formatter);
					    	List<Object> resultSession = this.sessionRepository.findSessionByPeriod(dateTimeDebut, dateTimeFin);
		
			        		for(Session sess:sessions) {
			        			if(sess instanceof SessionLibre)
			        				nbrSessionLibre=nbrSessionLibre+1;
			        			if(sess instanceof SessionPlanifiee)
			        				nbrSessionPlanifiee=nbrSessionPlanifiee+1;
			        		}
		
				    	}		         
		        	  }
		              break;
		    	  }
		    	
		    	result.put("SL MOIS"+now.getMonthValue()+now.getYear(),nbrSessionLibre);
		    	result.put("SP MOIS"+now.getMonthValue()+now.getYear(),nbrSessionPlanifiee);
		    	
		    	System.out.println(result);
	    		
	    	}

	    	

	        return new ResponseEntity(result, HttpStatus.OK);



	    	}
        return new ResponseEntity(null, HttpStatus.OK);
    }
    
  //  @GetMapping("/infoSessionAdmin")
//    public ResponseEntity getSessionForAdmin() {
//        Collection<Session> sessions=sessionService.getAll();
//    	HashMap<String,Integer> result=new HashMap<String,Integer>();//Creating HashMap.
//
//        if (sessions != null) {
//        	int nbrSessionLibre=0;
//        	int nbrSessionPlanifiee=0;
//
//        	Date dte=new Date();
//        	System.out.println(dte.getYear()+"yyyyy"+dte.getMonth()+"mmmm"+dte.getDay()+"dddd");
//        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    		LocalDateTime dateTimeDebut = LocalDateTime.parse(dte.getYear()+"-"+dte.getMonth()+"-"+dte.getDay()+" 00:00", formatter);
//    		LocalDateTime dateTimeFin = LocalDateTime.parse(edt+" 23:59", formatter);        	
//        	}
//        	
////        	result.put("nbrUtilisateur",nbrUtilisateur); //Put elements in Map.
////        	result.put("nbrSessionLibre",nbrSessionLibre);
////        	result.put("nbrSessionPlanifiee",nbrSessionPlanifiee);
////        	result.put("nbrTerrain",nbrTerrain);
////        	result.put("nbrPlanification",nbrPlanification);
//
//            return new ResponseEntity(result, HttpStatus.OK);
//        }
//        return new ResponseEntity(null, HttpStatus.OK);
//    }
}
