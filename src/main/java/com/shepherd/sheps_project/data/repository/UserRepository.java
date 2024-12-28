package com.shepherd.sheps_project.data.repository;

import com.shepherd.sheps_project.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Page<User> findAllByIsEnabled(boolean enabled, Pageable pageable);
}
