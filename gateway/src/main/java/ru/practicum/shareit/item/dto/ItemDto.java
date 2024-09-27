package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "name must be not blank")
    @Size(max = 100)
    private String name;
    @NotNull(message = "description required")
    @Size(max = 1000)
    private String description;
    @NotNull(message = "available field required")
    private Boolean available;
    private List<Comment> comments;
    private Long requestId;
}
