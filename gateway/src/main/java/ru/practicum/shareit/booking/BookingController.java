package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.StateRequest;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@PathVariable long bookingId,
                                                @RequestParam(name = "approved") Boolean approved,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(name = "state",
                                                              required = false,
                                                              defaultValue = "ALL") StateRequest state) {
        return bookingClient.getUsersBookings(state, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllUsersItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(name = "state",
                                                                   required = false,
                                                                   defaultValue = "ALL") StateRequest state) {
        return bookingClient.getUsersItemsBookings(state, userId);
    }

}
