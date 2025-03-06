package com.blu.path;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
    Simple Jpa Repository to store user's paths
 */
@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
}
