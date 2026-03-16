package com.ecommerce.payment.dto;

import com.ecommerce.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentDto {

    @NotNull
    private String OrderId;

    @Positive
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

}
