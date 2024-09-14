package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        log.info("Adding user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        log.info("Getting user: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User updateUser(User user, Long userId) {
        log.info("Updating user: {}, with id : {}", user, userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }
}
