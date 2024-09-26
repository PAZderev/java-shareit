package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.utils.StateRequest;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking createBooking(@RequestBody BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@PathVariable long bookingId,
                                 @RequestParam(name = "approved") Boolean approved,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable long bookingId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(name = "state",
                                                     required = false,
                                                     defaultValue = "ALL") StateRequest state) {
        return bookingService.getUsersBookings(state, userId);
    }

    @GetMapping("/owner")
    public List<Booking> getAllUsersItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "state",
                                                          required = false,
                                                          defaultValue = "ALL") StateRequest state) {
        return bookingService.getUsersItemsBookings(state, userId);
    }

}
