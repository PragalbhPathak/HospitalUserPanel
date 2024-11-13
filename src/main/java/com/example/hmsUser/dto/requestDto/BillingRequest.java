package com.example.hmsUser.dto.requestDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingRequest {
    private Long billingId;
    private boolean paymentStatus;
    private Long billAmount;
    private Date paymentDate;
    private String insuranceCoverage;

    private Long patientId;
    private String email;
}
