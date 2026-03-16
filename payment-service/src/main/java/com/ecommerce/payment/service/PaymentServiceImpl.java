package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.dto.PaymentStatusUpdateRequest;
import com.ecommerce.payment.dto.RequestPaymentDto;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.exception.PaymentNotFoundException;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.util.TransactionGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDto createPayment(RequestPaymentDto requestPaymentDto) {
       Payment payment= Payment.builder().orderId(requestPaymentDto.getOrderId())
                .amount(requestPaymentDto.getAmount())
                .paymentMethod(requestPaymentDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(TransactionGenerator.generate()).build();
       Payment savedPayment= paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    @Override
    public PaymentResponseDto getPaymentByOrderId(String orderId) {
        Payment payment=paymentRepository.findByOrderId(orderId).orElseThrow(()->
                new PaymentNotFoundException(String.format("Payment with orderId %s not found",orderId)));

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(Long paymentID, PaymentStatusUpdateRequest paymentStatusUpdateRequest) {
        Payment payment=paymentRepository.findById(paymentID).orElseThrow(()-> new PaymentNotFoundException(String.format("Payment with id %s not found",paymentID)));
        payment.setPaymentStatus(paymentStatusUpdateRequest.getPaymentStatus());
        return mapToResponse(payment);
    }

    private PaymentResponseDto mapToResponse(Payment payment){
        return new PaymentResponseDto(payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getTransactionId());
    }
}
