package dev.nugraha.obstest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.nugraha.obstest.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
