package com.civic.smartcivicsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "complaints")
@Data
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Naya field — kis OFFICER ko assign hui hai
    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    public enum Status {
        PENDING, IN_PROGRESS, RESOLVED
    }
}