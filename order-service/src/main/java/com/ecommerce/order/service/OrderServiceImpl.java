package com.ecommerce.order.service;

import com.ecommerce.order.client.InventoryFeignClient;
import com.ecommerce.order.dto.InventoryResponse;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        InventoryResponse inventory=inventoryFeignClient.isInStock(orderRequestDto.getSkuCode());
        if(!inventory.isInStock()){
            throw new RuntimeException("Product is out of stock!");
        }
        Order order=orderMapper.toEntity(orderRequestDto);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }
}
