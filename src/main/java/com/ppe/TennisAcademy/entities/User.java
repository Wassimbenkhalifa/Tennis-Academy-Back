package com.ppe.TennisAcademy.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Role", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;

    @Column
    private String prenom;

    @Column
    private String nom;

    @Column
    private Date dateNaissance;

    @Column
    private String addresse;

    @Column
    private int telephone;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String gender;


    @NotBlank
    @Size(max = 20)
    private String username;


    @Size(max = 120)
//    @JsonIgnore
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name="reset_password_token")
    @Size(max = 120)
    private String resetPasswordToken;

    private Boolean verified;

   @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Media photo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username2, String email2, String encode) {
        this.username=username2;
        this.email=email2;
        this.password=encode;
    }

    public User() {

    }

    public User(String prenom, String nom, Date dateNaissance, int telephone,
                       @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 20) String username,
                       @NotBlank @Size(max = 120) String password, boolean verified) {

        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.verified = verified;
    }

    public User(String prenom, String nom, Date dateNaissance, int telephone,
                       @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 20) String username,
                       @NotBlank @Size(max = 120) String password, Boolean verified, Set<Role> roles) {

        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.verified = verified;
        this.roles = roles;
    }

}

