package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.GatewayOrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentGateway implements PaymentGateway {

    private final RazorpayClient razorpayClient;

    @Override
    public GatewayOrderResponse createOrder(String orderId, BigDecimal amount) throws RazorpayException {

        JSONObject object=new JSONObject();
        object.put("amount",amount.multiply(BigDecimal.valueOf(100)));
        object.put("currency","INR");
        object.put("receipt",orderId);
        Order order=razorpayClient.orders.create(object);
        return new GatewayOrderResponse(
                order.get("id"),
                order.get("currency"),
                amount,
                order.get("status"),
                "RAZORPAY"
        );
    }
}
