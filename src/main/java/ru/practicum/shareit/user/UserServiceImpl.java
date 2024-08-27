package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return userRepository.createUser(user);
    }

    @Override
    public User getUser(Long id) {
        log.info("Getting user: {}", id);
        return userRepository.getUser(id);
    }

    @Override
    public User updateUser(User user, Long userId) {
        log.info("Updating user: {}, with id : {}", user, userId);
        return userRepository.updateUser(user, userId);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        userRepository.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.getUsers();
    }
}
