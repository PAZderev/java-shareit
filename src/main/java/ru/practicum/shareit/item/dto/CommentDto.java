package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    @NotBlank(message = "Comment must be not blank")
    @Size(max = 1000, message = "Text max size is 1000")
    private String text;
    private String authorName;
    private LocalDateTime created;
}
