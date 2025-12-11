package dev.nugraha.obstest.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.nugraha.obstest.dto.InventoryRequest;
import dev.nugraha.obstest.model.Inventory;
import dev.nugraha.obstest.model.Item;
import dev.nugraha.obstest.repository.InventoryRepository;
import dev.nugraha.obstest.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void createMovement_TopUp_IncreasesStock() {
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setStock(10);

        InventoryRequest request = new InventoryRequest(itemId, 5, "T");

        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        inventoryService.createInventoryMovement(request);

        // Assert
        assertEquals(15, item.getStock());
        verify(itemRepository).save(item);
    }

    @Test
    void createMovement_Withdraw_DecreasesStock() {
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setStock(10);

        InventoryRequest request = new InventoryRequest(itemId, 3, "W");

        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        inventoryService.createInventoryMovement(request);

        assertEquals(7, item.getStock());
        verify(itemRepository).save(item);
    }

    @Test
    void createMovement_Withdraw_InsufficientStock_ThrowsException() {
        
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setStock(2);

        InventoryRequest request = new InventoryRequest(itemId, 5, "W");

        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));

        Exception ex = assertThrows(RuntimeException.class, () -> 
            inventoryService.createInventoryMovement(request)
        );
        
        assertTrue(ex.getMessage().contains("Insufficient stock"));
        assertEquals(2, item.getStock());
        verify(inventoryRepository, never()).save(any());
    }
}
