package com.blu.device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
    Basic Device CRUD operations (Done by Luke T.)
 */
@Service
public class RegisteredDeviceService {

    // Calls repo
    @Autowired
    private RegisteredDeviceRepository registeredDeviceRepository;

    // List of registered devices
    public List<RegisteredDevice> getAllRegisteredDevice() {
        return registeredDeviceRepository.findAll();
    }

    // Filter search by default primary key
    public Optional<RegisteredDevice> getRegisteredDeviceById(Long id) {
        return registeredDeviceRepository.findById(id);
    }

    // Saves device to user
    public RegisteredDevice saveRegisteredDevice(RegisteredDevice registeredDevice) {
        return registeredDeviceRepository.save(registeredDevice);
    }

    // Delete device
    public void deleteRegisteredDevice(Long id) {
        registeredDeviceRepository.deleteById(id);
    }

    // Update device properties
    public RegisteredDevice updateRegisteredDevice(Long id, RegisteredDevice registeredDeviceDetails) {
        RegisteredDevice registeredDevice = registeredDeviceRepository.findById(id).orElseThrow();
        // Insert data from the controller
        return registeredDeviceRepository.save(registeredDeviceDetails);
    }

    // Filter device by IPv6
    public RegisteredDevice getRegisteredDeviceByIp(String ipAddress) {
        return getRegisteredDeviceByIp(ipAddress);
    }

}
