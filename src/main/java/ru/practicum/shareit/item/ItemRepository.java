package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private Long currentItemId = 1L;

    public Item findById(Long id) {
        log.info("Find Item by id: {}", id);
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item with id " + id + " not found");
        }
        return items.get(id);
    }

    public Item save(Item item) {
        log.info("Save Item: {}", item);
        item.setId(currentItemId);
        items.put(currentItemId++, item);
        return items.get(item.getId());
    }

    public Item update(Item item) {
        log.info("Update Item: {}", item);
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Item with id " + item.getId() + " not found");
        }
        Item updatingItem = items.get(item.getId());
        Optional.ofNullable(item.getName()).ifPresent(updatingItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(updatingItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(updatingItem::setAvailable);
        Optional.ofNullable(item.getOwnerId()).ifPresent(updatingItem::setOwnerId);
        items.put(item.getId(), updatingItem);
        return items.get(item.getId());
    }

    public void delete(Long id) {
        log.info("Delete Item: {}", id);
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item with id " + id + " not found");
        }
        items.remove(id);
    }

    public List<Item> findAll() {
        log.info("Find All Items");
        return new ArrayList<>(items.values());
    }
}
