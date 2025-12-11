package dev.nugraha.obstest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.nugraha.obstest.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
}
