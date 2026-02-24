package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryResponse checkInventory(String skuCode){
        return inventoryMapper.toResponse(
                inventoryRepository.findBySkuCode(skuCode)
                        .orElseThrow(()->new RuntimeException("Inventory not found with skuCode: "+skuCode)));
    }

    public void addInventory(InventoryRequest inventoryRequest){
        Inventory inventory=inventoryMapper.toEntity(inventoryRequest);
        inventoryRepository.save(inventory);
    }
}
