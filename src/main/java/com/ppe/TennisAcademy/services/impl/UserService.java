package com.ppe.TennisAcademy.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import com.ppe.TennisAcademy.entities.Adherent;
import com.ppe.TennisAcademy.entities.Admin;
import com.ppe.TennisAcademy.entities.Role;
import com.ppe.TennisAcademy.entities.User;
import com.ppe.TennisAcademy.entities.Coach;
import com.ppe.TennisAcademy.entities.ERole;
import com.ppe.TennisAcademy.repositories.RoleRepository;
import com.ppe.TennisAcademy.repositories.UserRepository;
import com.ppe.TennisAcademy.utils.exceptions.UserNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.lang.Assert;

@Service
@Transactional
   public class UserService <T extends User> {

    protected final UserRepository<T> userRepository;
    protected final RoleRepository roleRepository;


    public UserService(UserRepository<T> userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        Assert.notNull(this.userRepository, "user repository is not implemented "+this.userRepository.getClass());
    }

    public T save(T user) {
        List<String> strRoles = user.getRoles().stream().map((r) -> r.getName().toString()).collect(Collectors.toList());
        Set<Role> roles = new HashSet<>();

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
                case "ROLE_ADHERENT":
                    Role adherentRole = roleRepository.findByName(ERole.ROLE_ADHERENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role adherent is not found."));
                    roles.add(adherentRole);
                    break;
                default:
                    Role userRoleDefault = roleRepository.findByName(ERole.ROLE_ADHERENT)
                            .orElseThrow(() -> new RuntimeException("Error: default Role (user) is not found."));
                    roles.add(userRoleDefault);
            }
        });

        user.setRoles(roles);
        return  userRepository.save((T) user);
    }

    public T getByID(Long id){
        return this.userRepository.findById(id).orElse(null);
    }

    public List<T> getAll(){
        return this.userRepository.findAll();
    }

    public List<Coach> getAllCoachs(){
        List<User> listUsers=(List<User>) this.userRepository.findAll();
        List<Coach> listCoach=new ArrayList<>();

        for(User u:listUsers) {
            String roleName = String.valueOf(u.getRoles().stream().findFirst().get().getName());
            if( roleName.equals("ROLE_COACH") ) {
                Coach ad = mapUserToCoach(u);
                listCoach.add(ad);
            }
        }

        return listCoach;

    }

    public List<Adherent> getAllAdherents(){
        List<User> listUsers=(List<User>) this.userRepository.findAll();
        List<Adherent> listAdherent=new ArrayList<>();

        for(User u:listUsers) {
            String roleName = String.valueOf(u.getRoles().stream().findFirst().get().getName());
            if( roleName.equals("ROLE_ADHERENT") ) {
                Adherent ad = mapUserToAdherent(u);
                listAdherent.add(ad);
            }
        }

        return listAdherent;

    }

    public Adherent mapUserToAdherent(User user) {
        Adherent ad = new Adherent();
        ad.setIdUser(user.getIdUser());
        ad.setEmail(user.getEmail());
        ad.setGender(user.getGender());
        ad.setAddresse(user.getAddresse());
        ad.setDateNaissance(user.getDateNaissance());
        ad.setNom(user.getNom());
        ad.setPrenom(user.getPrenom());
        ad.setPassword(user.getPassword());
        ad.setPhoto(user.getPhoto());
        ad.setRoles(user.getRoles());
        ad.setTelephone(user.getTelephone());
        ad.setNbrMatchJoues(ad.getNbrMatchJoues());
        ad.setTerrainsReserves(ad.getTerrainsReserves());
        ad.setUsername(user.getUsername());
        ad.setResetPasswordToken(user.getResetPasswordToken());
        ad.setVerified(user.getVerified());
        return ad;
    }

    public Admin mapUserToAdmin(User user) {
        Admin ad = new Admin();
        ad.setIdUser(user.getIdUser());
        ad.setEmail(user.getEmail());
        ad.setGender(user.getGender());
        ad.setAddresse(user.getAddresse());
        ad.setDateNaissance(user.getDateNaissance());
        ad.setNom(user.getNom());
        ad.setPrenom(user.getPrenom());
        ad.setPassword(user.getPassword());
        ad.setPhoto(user.getPhoto());
        ad.setRoles(user.getRoles());
        ad.setTelephone(user.getTelephone());
        ad.setUsername(user.getUsername());
        ad.setResetPasswordToken(user.getResetPasswordToken());
        ad.setVerified(user.getVerified());
        return ad;
    }

    public Coach mapUserToCoach(User user) {
        Coach ad = new Coach();
        ad.setIdUser(user.getIdUser());
        ad.setEmail(user.getEmail());
        ad.setGender(user.getGender());
        ad.setAddresse(user.getAddresse());
        ad.setDateNaissance(user.getDateNaissance());
        ad.setNom(user.getNom());
        ad.setPrenom(user.getPrenom());
        ad.setPassword(user.getPassword());
        ad.setPhoto(user.getPhoto());
        ad.setRoles(user.getRoles());
        ad.setTelephone(user.getTelephone());
        ad.setUsername(user.getUsername());
        ad.setResetPasswordToken(user.getResetPasswordToken());
        ad.setVerified(user.getVerified());
        return ad;
    }


    public List<Admin> getAllAdmins(){
        List<User> listUsers=(List<User>) this.userRepository.findAll();
        List<Admin> listAdmin=new ArrayList<>();

        for(User u:listUsers) {
            String roleName = String.valueOf(u.getRoles().stream().findFirst().get().getName());
            if( roleName.equals("ROLE_ADMIN") ) {
                Admin admin = mapUserToAdmin(u);
                listAdmin.add(admin);
                return listAdmin;
            }
        }

        return listAdmin;

    }


    public void deleteById(Long id){
        this.userRepository.deleteById(id);
    }




    public List<T> findByUsernameOrDescriptionContainingIgnoreCase(String value){
        return this.userRepository.findByUsernameContainingIgnoreCase(value);
    }

    public Boolean existsByUsername(String username){
        return this.userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return this.userRepository.existsByEmail(email);
    }


    public T getByUserName(String userName){
        return (T) userRepository.findByUsername(userName);
    }

    public void updateResetPasswordToken (String token, String email) throws UserNotFoundException {
        User existingUser=userRepository.findByEmail(email);

        if(existingUser!=null) {
            existingUser.setResetPasswordToken(token);
            userRepository.save((T) existingUser);
        }else {
            throw new UserNotFoundException("could not found user with this email  "+email);
        }
    }

    public User get(String resetPasswordToken) {
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user,String newPassword) {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

        String encodedPassword=passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save((T) user);
    }
}
