package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.AccessRightException;
import ru.practicum.shareit.exception.CommentCreateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.item.CommentMapper.mapToComment;
import static ru.practicum.shareit.item.CommentMapper.mapToCommentDto;
import static ru.practicum.shareit.item.ItemMapper.mapToBookingsFromDto;
import static ru.practicum.shareit.item.ItemMapper.mapToItem;
import static ru.practicum.shareit.item.ItemMapper.mapToItemDto;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Item addItem(ItemDto itemDto, Long id) {
        log.info("Adding new item: {} with ownerId {}", itemDto, id);
        if (id == null) {
            throw new NotFoundException("Id is null");
        }
        userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id " + id + " not found"));
        return itemRepository.save(mapToItem(itemDto, userRepository.findById(id).orElse(null), null));
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
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Item with id " + itemId + " not found"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new AccessRightException("You do not have permission to update this item");
        }
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " not found"));
        return itemRepository.save(mapToItem(itemDto, userRepository.findById(userId).orElse(null), itemId));
    }

    @Override
    public ItemDtoBookings getItem(Long id) {
        log.info("Getting item with id {}", id);
        ItemDto itemDto = mapToItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found")));
        itemDto.setComments(commentRepository.findAllByItemId(id));
        return mapToBookingsFromDto(itemDto);
    }

    @Override
    public List<ItemDtoBookings> getItems(Long userId) {
        log.info("Getting items with userId {}", userId);
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " not found"));
        return itemRepository.findItemsWithBookings(userId, LocalDateTime.now())
                .stream()
                .peek(item -> item.setComments(commentRepository.findAllByItemId(item.getId())))
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String query) {
        log.info("Searching for items with query {}", query);

        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(query)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long itemId, Long userId) {
        log.info("Adding a comment for Item {} by user {}", itemId, userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        System.out.println("****************************************");
        System.out.println("****************************************");
        System.out.println("****************************************");
        System.out.println("TIME : " + LocalDateTime.now());
        System.out.println(bookingRepository.findAll().stream()
                        .filter(booking -> Objects.equals(booking.getItem().getId(), itemId) && Objects.equals(booking.getBooker().getId(), userId))
                .toList());
        System.out.println("****************************************");
        System.out.println("****************************************");
        System.out.println("****************************************");
        System.out.println("****************************************");
        System.out.println("****************************************");

        if (!bookingRepository.findBookingByBookerIdAndItemIdAndStatusAndEndBefore(
                userId,
                itemId,
                BookingStatus.APPROVED,
                LocalDateTime.now()).isEmpty()) {
            return mapToCommentDto(commentRepository.save(mapToComment(commentDto, user, item)));
        }
        throw new CommentCreateException("You can`t add comment to item " + itemId);
    }
}
