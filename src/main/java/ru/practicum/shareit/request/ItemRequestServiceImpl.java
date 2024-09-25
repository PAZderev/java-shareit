package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static ru.practicum.shareit.request.ItemRequestMapper.mapToItemRequest;
import static ru.practicum.shareit.request.ItemRequestMapper.mapToItemRequestDto;
import static ru.practicum.shareit.request.ItemRequestMapper.mapToItemRequestResponseDto;

@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemService itemService;
    @PersistenceContext
    private final EntityManager entityManager;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  @Lazy ItemService itemService,
                                  EntityManager entityManager) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemService = itemService;
        this.entityManager = entityManager;
    }

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto requestDto, long userId) {
        log.info("Adding new item request: {}", requestDto);
        User user = entityManager.getReference(User.class, userId);
        return mapToItemRequestDto(itemRequestRepository.save(mapToItemRequest(requestDto, user)));
    }

    @Override
    public Collection<ItemRequestResponseDto> getUsersItemRequests(long userId) {
        log.info("Getting user item requests for user: {}", userId);
        Collection<ItemRequest> requests = itemRequestRepository.findAllByRequestorId(userId);
        Collection<Item> items = itemService.getItemsByRequestsIds(requests
                .stream()
                .map(ItemRequest::getId)
                .toList());
        return requests.stream()
                .map(itemRequest -> mapToItemRequestResponseDto(itemRequest, items
                        .stream()
                        .filter(item -> item.getRequest().getId().equals(itemRequest.getId()))
                        .map(ItemMapper::mapToItemResponseDto)
                        .toList())
                )
                .sorted(Comparator.comparing(ItemRequestResponseDto::getCreated).reversed())
                .toList();
    }

    @Override
    public Collection<ItemRequest> getAllOthersRequests(long userId) {
        log.info("Getting other item requests for user: {}", userId);
        return itemRequestRepository.findAllByRequestorIdNotOrderByCreated(userId);
    }

    @Override
    public ItemRequestResponseDto getItemRequest(long requestId) {
        log.info("Getting item request: {}", requestId);
        return mapToItemRequestResponseDto(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with %s not found", requestId))),
                itemService.getItemsByRequestsIds(List.of(requestId))
                        .stream()
                        .filter(item -> item.getRequest().getId().equals(requestId))
                        .map(ItemMapper::mapToItemResponseDto)
                        .toList());
    }

    @Override
    public ItemRequest findById(long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %s not found", requestId)));
    }
}
