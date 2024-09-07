package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "name must be not blank")
    private String name;
    @NotNull(message = "description required")
    private String description;
    @NotNull(message = "available field required")
    private Boolean available;
    private List<Comment> comments;
}
