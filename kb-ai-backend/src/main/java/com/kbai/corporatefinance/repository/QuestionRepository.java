package com.kbai.corporatefinance.repository;

import com.kbai.corporatefinance.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCreatedAtAfter(LocalDateTime date);
    
    // 가장 최신의 두 개 질문을 가져오는 메서드
    List<Question> findTop2ByOrderByCreatedAtDesc();
}