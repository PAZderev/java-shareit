package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto requestDto, long userId);
    @EntityGraph(attributePaths = {"items", "items.owner"})
    Collection<ItemRequestResponseDto> getUsersItemRequests(long userId);
    Collection<ItemRequest> getAllOthersRequests(long userId);
    ItemRequestResponseDto getItemRequest(long requestId);
    ItemRequest findById(long requestId);
}
