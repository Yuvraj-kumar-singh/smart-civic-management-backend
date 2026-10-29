package com.civic.smartcivicsystem.repository;

import com.civic.smartcivicsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Naya method — ek specific role aur department ke sare users dhundne ke liye
    List<User> findByRoleAndDepartment(User.Role role, String department);
}