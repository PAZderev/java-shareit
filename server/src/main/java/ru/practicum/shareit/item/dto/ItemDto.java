package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<Comment> comments;
    private Long requestId;
}
