package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDto itemDto, Long id);

    Item updateItem(ItemDto itemDto, Long itemId, Long userId);

    Item getItem(Long id);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> searchItems(String query);

}
