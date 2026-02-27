package com.ecommerce.order.service;

import com.ecommerce.order.client.InventoryFeignClient;
import com.ecommerce.order.dto.InventoryResponse;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.event.OrderPlacedEvent;
import com.ecommerce.order.kafka.OrderEventProducer;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;
    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        InventoryResponse inventory=inventoryFeignClient.isInStock(orderRequestDto.getSkuCode());
        if(!inventory.isInStock()){
            throw new RuntimeException("Product is out of stock!");
        }
        Order order=orderMapper.toEntity(orderRequestDto);
        String orderId=UUID.randomUUID().toString();
        order.setOrderNumber(orderId);
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        OrderPlacedEvent event=new OrderPlacedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId);
        event.setSkuCode(orderRequestDto.getSkuCode());
        event.setQuantity(orderRequestDto.getQuantity());
        event.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(event);
        return orderMapper.toResponse(order);
    }
}
