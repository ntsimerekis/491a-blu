package com.blu.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repo for the devices (Done by Luke T.)
@Repository
public interface RegisteredDeviceRepository extends JpaRepository<RegisteredDevice, Long> {
    Optional<RegisteredDevice> findByIpAddress(String ipAddress);
}
