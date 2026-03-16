package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.GatewayOrderResponse;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;

public interface PaymentGateway {
    GatewayOrderResponse createOrder(String orderId, BigDecimal amount) throws RazorpayException;

}
