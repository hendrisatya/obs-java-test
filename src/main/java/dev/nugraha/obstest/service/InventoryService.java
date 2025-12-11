package dev.nugraha.obstest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.nugraha.obstest.model.Inventory;
import dev.nugraha.obstest.model.Item;
import dev.nugraha.obstest.dto.InventoryRequest;
import dev.nugraha.obstest.repository.ItemRepository;
import dev.nugraha.obstest.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Inventory createInventoryMovement(InventoryRequest request) {
        Item item = itemRepository.findByIdWithLock(request.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        String type = request.type().toUpperCase();

        if ("T".equals(type)) {
            item.setStock(item.getStock() + request.quantity());
        } else if ("W".equals(type)) {
            if (item.getStock() < request.quantity()) {
                throw new RuntimeException("Insufficient stock for withdrawal!");
            }
            item.setStock(item.getStock() - request.quantity());
        }

        itemRepository.save(item);

        // Create the Log Entry
        Inventory inventory = new Inventory();
        inventory.setItem(item);
        inventory.setQuantity(request.quantity());
        inventory.setType(type);
        
        return inventoryRepository.save(inventory);
    }

    public Page<Inventory> getAllInventoryLogs(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }
}
