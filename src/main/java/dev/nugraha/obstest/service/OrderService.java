package dev.nugraha.obstest.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.nugraha.obstest.dto.OrderRequest;
import dev.nugraha.obstest.model.Item;
import dev.nugraha.obstest.model.Order;
import dev.nugraha.obstest.repository.ItemRepository;
import dev.nugraha.obstest.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Order createOrder(OrderRequest request) {
        Item item = itemRepository.findByIdWithLock(request.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getStock() < request.quantity()) {
            throw new RuntimeException("Insufficient stock available!"); 
        }

        item.setStock(item.getStock() - request.quantity());
        itemRepository.save(item);

        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString());
        order.setItem(item);
        order.setQuantity(request.quantity());
        order.setTotalPrice(totalPrice);
        
        return orderRepository.save(order);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
}
