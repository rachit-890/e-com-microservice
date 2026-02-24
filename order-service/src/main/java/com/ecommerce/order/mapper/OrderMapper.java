package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDto orderRequestDto){
        Order order = new Order();
        order.setSkuCode(orderRequestDto.getSkuCode());
        order.setQuantity(orderRequestDto.getQuantity());
        order.setPrice(orderRequestDto.getPrice());
        return order;
    }

    public OrderResponseDto toResponse(Order order){
        return new OrderResponseDto(order.getOrderNumber(), order.getOrderStatus());
    }
}
