package ru.kata.spring.boot_security.demo.dto;

import lombok.Data;
import lombok.NonNull;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;


@Data
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private int age;
    private String password;
    private String email;
    private Set<Long> role;
}
