package ru.practicum.shareit.user;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long currentUserId = 1L;

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return users.get(id);
    }

    public User createUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new DuplicateRequestException("User with email " + user.getEmail() + " already exists");
        }
        user.setId(currentUserId);
        users.put(currentUserId++, user);
        emails.add(user.getEmail());
        return users.get(user.getId());
    }

    public User updateUser(User user, Long userId) {
        user.setId(userId);
        if (users.get(user.getId()) == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        /*
         Проверка на дубликат emailа с другим пользователем
         */
        if (!users.get(user.getId()).getEmail().equals(user.getEmail()) && emails.contains(user.getEmail())) {
            throw new DuplicateRequestException("User with email " + user.getEmail() + " already exists");
        }
        User updatedUser = users.get(userId);
        Optional.ofNullable(user.getName()).ifPresent(updatedUser::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(updatedUser::setEmail);
        emails.remove(users.get(user.getId()).getEmail());
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public void deleteUser(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        users.remove(id);
    }


}
