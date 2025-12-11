package dev.nugraha.obstest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nugraha.obstest.model.Inventory;
import dev.nugraha.obstest.dto.InventoryRequest;
import dev.nugraha.obstest.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> createInventoryMovement(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.createInventoryMovement(request));
    }

    // 2.b Listing with Pagination
    @GetMapping
    public ResponseEntity<Page<Inventory>> getAllInventoryLogs(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getAllInventoryLogs(pageable));
    }
}
