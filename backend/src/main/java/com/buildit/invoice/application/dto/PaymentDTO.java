package com.buildit.invoice.application.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fotty on 5/26/16.
 */

@Data
public class PaymentDTO {

    String poUrl;
    int total;
    String email;

}
