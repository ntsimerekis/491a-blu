package com.blu.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsByIpAddress(String ipAddress);

    Device findByIpAddress(String IpAddress);

    void deleteByIpAddress(String IpAddress);
}
