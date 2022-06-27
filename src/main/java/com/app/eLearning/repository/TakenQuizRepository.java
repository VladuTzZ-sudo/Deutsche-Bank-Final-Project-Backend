package com.app.eLearning.repository;

import com.app.eLearning.dao.TakenQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakenQuizRepository extends JpaRepository<TakenQuiz, Integer> {
}
