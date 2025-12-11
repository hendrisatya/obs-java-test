package dev.nugraha.obstest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.nugraha.obstest.model.Item;
import dev.nugraha.obstest.dto.ItemDto;
import dev.nugraha.obstest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Transactional
    public Item createItem(ItemDto dto) {
        Item item = new Item();
        item.setName(dto.name());
        item.setPrice(dto.price());
        item.setStock(dto.stock());
        return itemRepository.save(item);
    }

    @Transactional
    public Item editItem(Long id, ItemDto dto) {
        Item item = getItem(id);
        item.setName(dto.name());
        item.setPrice(dto.price());
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
