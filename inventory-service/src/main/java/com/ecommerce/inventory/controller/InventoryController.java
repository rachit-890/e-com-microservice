package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> isInStock (@PathVariable String skuCode){
        return ResponseEntity.ok(inventoryService.checkInventory(skuCode)) ;
    }

    @PostMapping("/add")
    public ResponseEntity<String > addInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.addInventory(inventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory added successfully");
    }

}
