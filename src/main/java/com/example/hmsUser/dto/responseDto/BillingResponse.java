package com.example.hmsUser.dto.responseDto;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingResponse {
    private Long billingId;
    private boolean paymentStatus;
    private Long billAmount;
    private Date paymentDate;
    private String insuranceCoverage;

    private Long patientId;
    private String email;

}
