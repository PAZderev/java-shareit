package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.shareit.utils.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    List<Booking> findBookingByBookerIdAndItemIdAndStatusAndEndBefore(Long bookerId,
                                                                      Long itemId,
                                                                      BookingStatus status,
                                                                      LocalDateTime end);
}
