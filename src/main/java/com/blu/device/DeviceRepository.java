package com.blu.device;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsByIpAddress(String ipAddress);

    Device findByIpAddress(String IpAddress);

    @Transactional
    @Modifying
    @Query("DELETE FROM Device d WHERE d.ipAddress = :ipAddress")
        // @Query("DELETE FROM Device d WHERE d.ipAddress = :ipAddress AND d.owner.email = :email")
    void deleteByEmailAndIpAddress(@Param("email") String email, @Param("ipAddress") String ipAddress);
}
