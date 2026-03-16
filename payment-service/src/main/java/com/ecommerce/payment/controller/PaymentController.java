package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.dto.PaymentStatusUpdateRequest;
import com.ecommerce.payment.dto.RequestPaymentDto;
import com.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid RequestPaymentDto requestPaymentDto) {
        PaymentResponseDto response=paymentService.createPayment(requestPaymentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(@PathVariable String orderId) {
        PaymentResponseDto response=paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDto> updateStatus(@PathVariable Long paymentId,@RequestBody @Valid PaymentStatusUpdateRequest request){
        PaymentResponseDto response=paymentService.updatePaymentStatus(paymentId,request);
        return ResponseEntity.ok(response);

    }

}
