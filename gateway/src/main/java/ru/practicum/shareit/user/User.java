package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class User {
    private Long id;
    private String name;
    @Email(message = "Email must be valid")
    @NotNull(message = "Email required")
    private String email;
}