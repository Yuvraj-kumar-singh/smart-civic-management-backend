package com.civic.smartcivicsystem.controller;

import com.civic.smartcivicsystem.entity.User;
import com.civic.smartcivicsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        try {
            return ResponseEntity.ok(userService.registerUser(newUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginRequest) {
        String identifier = loginRequest.get("identifier");
        String password = loginRequest.get("password");

        if (userService.checkLogin(identifier, password)) {
            return "Login Successful!";
        }
        return "Invalid email/name or password!";
    }

    @GetMapping("/staff/{department}")
    public List<User> getStaffByDepartment(@PathVariable String department) {
        return userService.getStaffByDepartment(department);
    }
}