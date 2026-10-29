package com.civic.smartcivicsystem.service;

import com.civic.smartcivicsystem.entity.User;
import com.civic.smartcivicsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Sign-up form se sirf CITIZEN account ban sakta hai
    public User registerUser(User newUser) {
        if (isBlank(newUser.getName())) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (isBlank(newUser.getEmail()) || !newUser.getEmail().contains("@")) {
            throw new IllegalArgumentException("Please enter a valid email.");
        }
        if (isBlank(newUser.getPassword()) || newUser.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        if (newUser.getRole() == null) {
            newUser.setRole(User.Role.CITIZEN);
        }
        if (newUser.getRole() == User.Role.ADMIN) {
            throw new IllegalArgumentException("Admin accounts cannot be created from the sign-up form.");
        }
        if (newUser.getRole() == User.Role.OFFICER) {
            throw new IllegalArgumentException("Officer accounts are created by the admin, not from the sign-up form.");
        }

        newUser.setDepartment(null);
        newUser.setName(newUser.getName().trim());
        newUser.setEmail(newUser.getEmail().trim().toLowerCase());

        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // identifier = Email ya Name, dono chalega
    public boolean checkLogin(String identifier, String rawPassword) {
        if (isBlank(identifier) || isBlank(rawPassword)) {
            return false;
        }

        User existingUser = userRepository.findByEmail(identifier.trim().toLowerCase()).orElse(null);

        if (existingUser == null) {
            for (User u : userRepository.findAll()) {
                if (u.getName().equalsIgnoreCase(identifier.trim())) {
                    existingUser = u;
                    break;
                }
            }
        }

        if (existingUser == null) {
            return false;
        }

        return passwordEncoder.matches(rawPassword, existingUser.getPassword());
    }

    public List<User> getStaffByDepartment(String department) {
        return userRepository.findByRoleAndDepartment(User.Role.OFFICER, department);
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}