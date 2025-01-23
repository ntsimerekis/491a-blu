package com.blu.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredDeviceRepository extends JpaRepository<RegisteredDevice, Long> {
}
