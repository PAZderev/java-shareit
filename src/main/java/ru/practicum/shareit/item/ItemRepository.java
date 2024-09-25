package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT new ru.practicum.shareit.item.dto.ItemDtoBookings(" +
            "i.id, " +
            "i.name, " +
            "i.description, " +
            "i.available, " +
            "(SELECT MAX(b.end) FROM Booking b " +
            "WHERE b.item.id = i.id " +
            "AND b.end < :now " +
            "AND b.status = ru.practicum.shareit.utils.BookingStatus.APPROVED), " +
            "(SELECT MIN(b.start) FROM Booking b " +
            "WHERE b.item.id = i.id " +
            "AND b.start > :now " +
            "AND b.status = ru.practicum.shareit.utils.BookingStatus.APPROVED)" +
            ") " +
            "FROM Item i " +
            "WHERE i.owner.id = :ownerId")
    List<ItemDtoBookings> findItemsWithBookings(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);

    List<Item> findAllByRequestIdIn(Collection<Long> requestIds);

}