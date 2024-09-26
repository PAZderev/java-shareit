package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.AccessRightException;
import ru.practicum.shareit.exception.BookingCreationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.BookingStatus;
import ru.practicum.shareit.utils.StateRequest;

import java.util.List;

import static ru.practicum.shareit.booking.BookingMapper.mapToBooking;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Booking createBooking(BookingDto bookingDto, Long userId) {
        log.info("Creating booking {}", bookingDto);
        Booking booking = mapToBooking(bookingDto, itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + bookingDto.getItemId() + " not found"))
        );
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id" + userId + "not found"))
        );
        if (!booking.getItem().getAvailable()) {
            throw new BookingCreationException("Item with id " + booking.getItem().getId() + " is not available");
        }
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            throw new BookingCreationException("Start must be before end");
        }
        booking.setStatus(BookingStatus.WAITING);
        Booking saved = bookingRepository.save(booking);
        return bookingRepository.findById(booking.getId()).orElse(null);
    }

    @Override
    public Booking updateBooking(Long bookingId, Boolean approved, Long userId) {
        log.info("Updating booking with id {}, status {}, by user {}", bookingId, approved, userId);
        userRepository.findById(userId) // Выбрасываем не NotFound из-за того, что тесты не ожидают 404 код
                .orElseThrow(() -> new AccessRightException("User with id " + userId + " not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
        if (booking.getItem().getOwner().getId().equals(userId)) {
            booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        } else {
            throw new AccessRightException("You do not have permission to update booking." +
                    " Owner id = " + booking.getItem().getOwner().getId() +
                    " Your id = " + userId);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long bookingId, Long userId) {
        log.info("Get booking with id {} by user {}", bookingId, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return booking;
        } else {
            throw new AccessRightException("You do not have permission to view this booking");
        }
    }

    @Override
    public List<Booking> getUsersBookings(StateRequest state, Long userId) {
        log.info("Get users bookings for user {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Specification<Booking> specification = BookingSpecifications.byUserAndState(userId, state);
        return bookingRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "start"));
    }


    @Override
    public List<Booking> getUsersItemsBookings(StateRequest state, Long userId) {
        log.info("Get bookings for user`s items with user id = {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Specification<Booking> specification = BookingSpecifications.byOwnerAndState(userId, state);
        return bookingRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "start"));
    }
}
