package com.mja123.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
public class BookEntity implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String title;
    @Column(nullable = false, length = 255)
    private String author;
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;
    @Column(length = 100)
    private String genre;
    @Column(name = "published_year")
    private Integer publishedYear;
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now().withNano(0);
        }
    }
}
