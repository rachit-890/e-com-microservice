package com.ecommerce.payment.service;

import com.ecommerce.payment.client.InventoryClient;
import com.ecommerce.payment.client.OrderClient;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.repository.PaymentRepository;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorpayWebHookService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final InventoryClient inventoryClient;



    @Value("${razorpay.webhook-secret}")
    private String webhookSecret;

    public void processWebhook(String payload, String signature) {
       verifySignature(payload,signature);
       JSONObject event=new JSONObject(payload);
       String eventType=event.getString("event");
       log.info("Received Razorpay Webhook Event: {}",eventType);
       switch (eventType){
           case "payment.captured" ->handlePaymentSuccess(event);
           case "payment.failed" -> handlePaymentFailure(event);
           default -> log.warn("Unhandled Razorpay Webhook Event: {}",eventType);
       }
    }

    private void handlePaymentSuccess(JSONObject event) {
        JSONObject paymentEntity=extractPaymentEntity(event);
        String razorpayPaymentId=paymentEntity.getString("id");
        String razorpayOrderId=paymentEntity.getString("orderId");
        Payment payment=paymentRepository.findByTransactionId(razorpayOrderId).orElseThrow(()->new IllegalStateException("Payment not found for razorpayOrderId: "+razorpayOrderId));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(razorpayPaymentId);
        orderClient.confirmOrder(payment.getOrderId());
        inventoryClient.commitEvent(payment.getOrderId());

    }

    private void handlePaymentFailure(JSONObject event) {
        JSONObject paymentEntity=extractPaymentEntity(event);
        String razorpayOrderId=paymentEntity.getString("orderId");
        Payment payment=paymentRepository.findByTransactionId(razorpayOrderId).orElseThrow(()->new IllegalStateException("Payment not found for razorpayOrderId: "+razorpayOrderId));
        payment.setPaymentStatus(PaymentStatus.FAILED);
        inventoryClient.rollbackEvent(payment.getOrderId());
        orderClient.failOrder(payment.getOrderId());
    }

    private JSONObject extractPaymentEntity(JSONObject event) {
        return event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
    }


    private void verifySignature(String payload, String signature) {
        try{
            Utils.verifyWebhookSignature(payload,signature,webhookSecret);
        }catch(Exception e){
            log.error("Invalid Razorpay Webhook Signature");
            throw new RuntimeException("Invalid Razorpay Webhook Signature");
        }
    }


}
