package com.ecommerce.payment.controller;

import com.ecommerce.payment.service.RazorpayWebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class WebHookController {

    private final RazorpayWebHookService razorpayWebHookService;

    @PostMapping("/webhook/razorpay")
    public ResponseEntity<Void> handleWebhook(@RequestHeader("X-Razorpay-Signature") String signature, String payload) {
        razorpayWebHookService.processWebhook(payload,signature);
        return ResponseEntity.ok().build();
    }
}
