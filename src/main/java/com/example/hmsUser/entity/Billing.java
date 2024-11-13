package com.example.hmsUser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "Billing")
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billingId")
    private Long billingId;
    @Column(name = "paymentStatus")
    private boolean paymentStatus;
    @Column(name = "billAmount")
    private Long billAmount;
    @Column(name = "paymentDate")
    private Date paymentDate;
    @Column(name = "insuranceCoverage")
    private String insuranceCoverage;

    @Column(name = "patientId")
    private Long patientId;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",nullable = false,unique = true)
    private String email;

}
