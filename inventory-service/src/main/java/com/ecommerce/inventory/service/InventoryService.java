package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.entity.ProcessedOrder;
import com.ecommerce.inventory.event.OrderPlacedEvent;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.repository.ProcessedOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProcessedOrderRepository processedOrderRepository;

    public InventoryResponse checkInventory(String skuCode){
        return inventoryMapper.toResponse(
                inventoryRepository.findBySkuCode(skuCode)
                        .orElseThrow(()->new RuntimeException("Inventory not found with skuCode: "+skuCode)));
    }

    public void addInventory(InventoryRequest inventoryRequest){
        Inventory inventory=inventoryMapper.toEntity(inventoryRequest);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void updateStock(OrderPlacedEvent orderPlacedEvent) {
        if(processedOrderRepository.existsByOrderId(orderPlacedEvent.getOrderId())){
            return;
        }
        Inventory inventory = inventoryRepository.findBySkuCode(orderPlacedEvent.getSkuCode())
                .orElseThrow(()->new RuntimeException("Inventory not found with skuCode: "+orderPlacedEvent.getSkuCode()));
        Integer currentQuantity = inventory.getQuantity();
        Integer orderQuantity = orderPlacedEvent.getQuantity();
        if(currentQuantity < orderQuantity){
            throw new RuntimeException("Insufficient stock for skuCode: "+orderPlacedEvent.getSkuCode());
        }
        inventory.setQuantity(inventory.getQuantity() - orderPlacedEvent.getQuantity());
        inventoryRepository.save(inventory);
        ProcessedOrder processedOrder=new ProcessedOrder();
        processedOrder.setOrderId(orderPlacedEvent.getOrderId());
        processedOrder.setProcessedAt(LocalDateTime.now());
        processedOrderRepository.save(processedOrder);
    }
}
