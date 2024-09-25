package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        return itemRequest;
    }

    public static ItemRequestResponseDto mapToItemRequestResponseDto(ItemRequest itemRequest,
                                                                     List<ItemResponseDto> itemResponseDtos) {
        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setDescription(itemRequest.getDescription());
        responseDto.setCreated(itemRequest.getCreated());
        responseDto.setId(itemRequest.getId());
        responseDto.setItems(itemResponseDtos);
        return responseDto;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription(itemRequest.getDescription());
        requestDto.setCreated(itemRequest.getCreated());
        requestDto.setId(itemRequest.getId());
        return requestDto;
    }
}
