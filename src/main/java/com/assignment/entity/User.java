package com.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    private String image;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private boolean status;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "verification_code")
    private String verificationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String name, Date dob, Integer sex, String email) {
        this.name = name;
        this.dob = dob;
        this.sex = sex;
        this.email = email;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Transient
    public String getUserImage() {
        if(image == null || id == null) return "/images/default-user.png";
        return "/user-image/" + this.id + "/" + this.image;
    }
}
