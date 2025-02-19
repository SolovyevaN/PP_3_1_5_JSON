package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService= roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("authentication", authentication);
        model.addAttribute("userDto", new UserDto());
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        userService.addUser(userDto);
        System.out.println("Выбранная роль: " + userDto.getRole()); // Отладочный вывод
        redirectAttributes.addAttribute("message", "User added successfully! ");
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("roles") List<Long> roleIds,
                             @ModelAttribute UserDto userDto) {
        userDto.setId(id);
        Set<Role> roles = roleService.findRolesByIds(roleIds);
        userDto.setRole(roles.stream().map(Role::getId).collect(Collectors.toSet()));
        userService.updateUser(userDto);
        return "redirect:/admin";
    }
}


