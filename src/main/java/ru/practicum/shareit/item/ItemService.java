package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDto itemDto, Long id);

    Item updateItem(ItemDto itemDto, Long itemId, Long userId);

    ItemDtoBookings getItem(Long id);

    List<ItemDtoBookings> getItems(Long userId);

    List<ItemDto> searchItems(String query);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long userId);
}
