package com.blu.path;

import com.blu.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
    Simple Jpa Repository to store user's paths
 */
@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
    String file(String file);

    Path getPathById(PathKey pathKey);

    @Query("SELECT p FROM Path p WHERE p.id.user.email = :email")
    List<Path> getPathsByEmail(@Param("email") String email);

    //We have to put this in since JPA doesn't make an automati query for Embedded IDs
    @Query("SELECT p FROM Path p WHERE p.id.name = :name and p.id.user.email = :email")
    Path getPathByEmailAndName(@Param("email") String email, @Param("name") String name);

    @Query("DELETE FROM Path p WHERE p.id.name = :name and p.id.user.email = :email")
    void deletePathByEmailAndName(@Param("email") String email, @Param("name") String name);
}
