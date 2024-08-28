package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User getUser(Long id);

    User updateUser(User user, Long userId);

    void deleteUser(Long id);

    List<User> getAllUsers();
}
