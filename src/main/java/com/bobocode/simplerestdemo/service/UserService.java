package com.bobocode.simplerestdemo.service;

import com.bobocode.simplerestdemo.exception.UserNotFoundException;
import com.bobocode.simplerestdemo.model.Note;
import com.bobocode.simplerestdemo.model.User;
import com.bobocode.simplerestdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user) {
        Long id = user.getId();
        userRepository.addUser(user);
        log.info("User with ID: {} was added", id);
        return user;
    }

    @Cacheable("user")
    public User getUserById(Long id) {
        return Optional.ofNullable(userRepository.getUserById(id))
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID: %s wasn't found", id)));
    }

    @Cacheable("users")
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void removeUser(Long id) {
        checkUserExistence(id);
        userRepository.removeUser(id);
        log.info("User with ID: {} was deleted", id);
    }

    public Note addNote(Long userId, Note note) {
        checkUserExistence(userId);
        userRepository.addNote(userId, note);
        log.info("Note was added to user with ID: {}", userId);
        return note;
    }

    private void checkUserExistence(Long userId) {
        Optional.ofNullable(userRepository.getUserById(userId))
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID: %s wasn't found", userId)));
    }

    public List<Note> getUserNotes(Long userId) {
        return userRepository.getUserNotes(userId);
    }
}
