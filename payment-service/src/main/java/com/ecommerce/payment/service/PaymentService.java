package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.dto.PaymentStatusUpdateRequest;
import com.ecommerce.payment.dto.RequestPaymentDto;

public interface PaymentService {
    PaymentResponseDto createPayment(RequestPaymentDto requestPaymentDto);
    PaymentResponseDto getPaymentByOrderId(String orderId);
    PaymentResponseDto updatePaymentStatus(Long paymentID, PaymentStatusUpdateRequest paymentStatusUpdateRequest);
}
