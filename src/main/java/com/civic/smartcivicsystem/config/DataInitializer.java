package com.civic.smartcivicsystem.config;

import com.civic.smartcivicsystem.entity.User;
import com.civic.smartcivicsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// App start hote hi demo accounts banata hai. Passwords code mein nahi hain,
// local.properties (ya environment variables) se aate hain. Password set na ho to account nahi banta.
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${CITIZEN_PASSWORD:}")
    private String citizenPassword;

    @Value("${OFFICER_PASSWORD:}")
    private String officerPassword;

    @Override
    public void run(String... args) {
        createIfMissing("Admin User", "admin@test.com", adminPassword, User.Role.ADMIN, null);
        createIfMissing("Test User", "test2@test.com", citizenPassword, User.Role.CITIZEN, null);

        createIfMissing("Ramesh Water Officer", "water@test.com", officerPassword, User.Role.OFFICER, "Water");
        createIfMissing("Suresh Road Officer", "road@test.com", officerPassword, User.Role.OFFICER, "Road");
        createIfMissing("Amit Streetlight Officer", "light@test.com", officerPassword, User.Role.OFFICER, "Streetlight");
        createIfMissing("Vijay Garbage Officer", "garbage@test.com", officerPassword, User.Role.OFFICER, "Garbage");
        createIfMissing("Rohit Electricity Officer", "electricity@test.com", officerPassword, User.Role.OFFICER, "Electricity");
        createIfMissing("Manoj Drainage Officer", "drainage@test.com", officerPassword, User.Role.OFFICER, "Drainage");
        createIfMissing("Sanjay Housing Officer", "housing@test.com", officerPassword, User.Role.OFFICER, "Housing");
        createIfMissing("Neha Health Officer", "health@test.com", officerPassword, User.Role.OFFICER, "Health");
    }

    private void createIfMissing(String name, String email, String rawPassword, User.Role role, String department) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            log.warn("Password not set for {} ({}). Account not created.", email, role);
            return;
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);
        u.setDepartment(department);
        userRepository.save(u);
    }
}
