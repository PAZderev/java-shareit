package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.StateRequest;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDto bookingDto, Long userId);

    Booking updateBooking(Long bookingId, Boolean approved, Long userId);

    Booking getBooking(Long bookingId, Long userId);

    List<Booking> getUsersBookings(StateRequest state, Long userId);

    List<Booking> getUsersItemsBookings(StateRequest state, Long userId);

}
