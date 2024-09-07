package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDtoBookings {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<Comment> comments = new ArrayList<>();

    public ItemDtoBookings(Long id, String name, String description, Boolean available, LocalDateTime lastBooking, LocalDateTime nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}
