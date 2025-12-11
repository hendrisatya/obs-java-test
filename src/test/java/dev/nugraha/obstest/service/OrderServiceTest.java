package dev.nugraha.obstest.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.nugraha.obstest.dto.OrderRequest;
import dev.nugraha.obstest.model.Item;
import dev.nugraha.obstest.model.Order;
import dev.nugraha.obstest.repository.ItemRepository;
import dev.nugraha.obstest.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success() {
        // Arrange
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("Laptop");
        item.setPrice(new BigDecimal("1000"));
        item.setStock(10);

        OrderRequest request = new OrderRequest(itemId, 5);

        // Mock the DB calls
        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.createOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(5, item.getStock()); 
        verify(itemRepository).save(item); 
        verify(orderRepository).save(any(Order.class)); 
    }

    @Test
    void createOrder_InsufficientStock_ThrowsException() {
        // Arrange
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setStock(2); 

        OrderRequest request = new OrderRequest(itemId, 5);

        when(itemRepository.findByIdWithLock(itemId)).thenReturn(Optional.of(item));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(request);
        });

        assertEquals("Insufficient stock available!", exception.getMessage());

        
        verify(orderRepository, never()).save(any());
        assertEquals(2, item.getStock());
    }
}
