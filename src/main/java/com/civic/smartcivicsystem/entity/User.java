package com.civic.smartcivicsystem.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // WRITE_ONLY: register ke time request se le lenge, par response mein kabhi wapas nahi bhejenge
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Sirf OFFICER ke liye
    private String department;

    public enum Role {
        CITIZEN, ADMIN, OFFICER
    }
}