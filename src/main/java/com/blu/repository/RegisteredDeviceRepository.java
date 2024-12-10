package com.blu.repository;

import com.blu.model.RegisteredDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blu.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredDeviceRepository extends JpaRepository<RegisteredDevice, Long> {
}
