package com.kbai.corporatefinance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(name = "question_text", length = 5000)
    private String text;

    @Column(name = "created_at", columnDefinition = "TEXT")
    private LocalDateTime createdAt;
}
