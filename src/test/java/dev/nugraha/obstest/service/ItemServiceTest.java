package dev.nugraha.obstest.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import dev.nugraha.obstest.dto.ItemDto;
import dev.nugraha.obstest.model.Item;
import dev.nugraha.obstest.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_Success() {
        ItemDto dto = new ItemDto(null, "Mouse", new BigDecimal("100000"), 50);
        
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Item result = itemService.createItem(dto);

        assertNotNull(result.getId());
        assertEquals("Mouse", result.getName());
        assertEquals(50, result.getStock());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void getItem_Success() {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.getItem(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getItem_NotFound_ThrowsException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> itemService.getItem(99L));
    }

    @Test
    void getAllItems_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> page = new PageImpl<>(List.of(new Item()));
        when(itemRepository.findAll(pageable)).thenReturn(page);

        Page<Item> result = itemService.getAllItems(pageable);
        assertEquals(1, result.getTotalElements());
    }
}
