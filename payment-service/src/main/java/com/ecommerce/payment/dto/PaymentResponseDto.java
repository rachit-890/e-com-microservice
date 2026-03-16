package com.ecommerce.payment.dto;

import com.ecommerce.payment.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long paymentId;
    private String orderId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String transactionId;
}
