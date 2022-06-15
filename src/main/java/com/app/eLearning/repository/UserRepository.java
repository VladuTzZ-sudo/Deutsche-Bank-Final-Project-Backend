package com.app.eLearning.repository;

import com.app.eLearning.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByEmailAndPassword(String email, String password);

    List<User> findByEmail(String email);
}
