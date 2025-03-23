package com.blu.user;

import com.blu.device.Device;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Transactional
    int deleteByEmail(String email);

    User getUserByEmail(String email);

    @Query("select u.devices from User u where u.email = :email")
    List<Device> getDevicesByEmail(@Param("email")  String email);

    @Transactional
    @Modifying
    @Query("update User u set u.activeDevice.ipAddress = :device where u.email = :email" )
    void setUserActiveDevice(@Param("email") String email, @Param("device") String ipAddress);

}