package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void addUser(UserDto userDto);
    void updateUser(UserDto userDto);
    User findById(Long id);
    void deleteUser(Long id);
    List<User> getAllUsers();

    public UserDetails loadUserByUsername(String string);
}
