package com.blu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blu.model.Path;
import org.springframework.stereotype.Repository;

@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
}
