package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessRightException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.item.ItemMapper.mapToItem;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item addItem(ItemDto itemDto, Long id) {
        log.info("Adding new item: {} with ownerId {}", itemDto, id);
        if (id == null) {
            throw new NotFoundException("Id is null");
        }
        if (userRepository.getUser(id) == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return itemRepository.save(mapToItem(itemDto, id, null));
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long itemId, Long userId) {
        log.info("Updating existing item: {} with ownerId {}", itemDto, userId);
        if (userId == null) {
            throw new NotFoundException("UserId is null");
        }
        if (itemId == null) {
            throw new NotFoundException("ItemId is null");
        }
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
        if (!item.getOwnerId().equals(userId)) {
            throw new AccessRightException("You do not have permission to update this item");
        }
        if (userRepository.getUser(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return itemRepository.update(mapToItem(itemDto, userId, itemId));
    }

    @Override
    public Item getItem(Long id) {
        log.info("Getting item with id {}", id);
        return itemRepository.findById(id);
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        log.info("Getting items with userId {}", userId);
        if (userRepository.getUser(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return itemRepository.findAll()
                .stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String query) {
        log.info("Searching for items with query {}", query);

        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        String lowerCaseQuery = query.toLowerCase();


        return itemRepository.findAll()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseQuery)
                        || item.getDescription().toLowerCase().contains(lowerCaseQuery))
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
