package com.blu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blu.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
