package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.exception.UserNotFindExeption;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/admin")
public class AdminRestController {

    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> adminPage() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PostMapping( value ="/addUser")
    public ResponseEntity<List<User>> addUser (@RequestBody UserDto userDto) {
        try {
            userService.addUser(userDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (UserNotFindExeption e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PutMapping(value = "/updateUser/{id}")
    public ResponseEntity<List<User>> updateUser( @PathVariable Long id , @RequestBody UserDto userDto) {
        try {
            userService.updateUser(userDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFindExeption e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/deleteUser/{id}")
        public ResponseEntity<HttpStatus> deleteUser(  @PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (UserNotFindExeption e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAuth(Principal principal) {
        return ResponseEntity.ok("Authenticated as: " + (principal != null ? principal.getName() : "Anonymous"));
    }

}
