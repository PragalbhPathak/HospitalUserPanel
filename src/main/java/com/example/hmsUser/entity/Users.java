package com.example.hmsUser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Entity
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @NotBlank(message = "username can't be blank")
    @Column(name = "username",nullable = false)
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @Size(min = 6,message = "Password should have atleast 6 characters")
    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;


}
