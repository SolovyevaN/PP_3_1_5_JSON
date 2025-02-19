package ru.kata.spring.boot_security.demo.exception;

public class UserNotFindExeption extends RuntimeException {
    public UserNotFindExeption(String message) {
        super(message);
    }
}
