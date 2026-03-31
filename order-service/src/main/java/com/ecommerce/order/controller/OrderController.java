package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderRequestDto));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable String orderId){
        orderService.confirmOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/fail")
    public ResponseEntity<Void> failOrder(@PathVariable String orderId){
        orderService.failOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
