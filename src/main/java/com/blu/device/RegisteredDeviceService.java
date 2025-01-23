package com.blu.device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisteredDeviceService {

    @Autowired
    private RegisteredDeviceRepository registeredDeviceRepository;

    public List<RegisteredDevice> getAllRegisteredDevice() {
        return registeredDeviceRepository.findAll();
    }

    public Optional<RegisteredDevice> getRegisteredDeviceById(Long id) {
        return registeredDeviceRepository.findById(id);
    }

    public RegisteredDevice saveRegisteredDevice(RegisteredDevice registeredDevice) {
        return registeredDeviceRepository.save(registeredDevice);
    }

    public void deleteRegisteredDevice(Long id) {
        registeredDeviceRepository.deleteById(id);
    }

    public RegisteredDevice updateRegisteredDevice(Long id, RegisteredDevice registeredDeviceDetails) {
        RegisteredDevice path = registeredDeviceRepository.findById(id).orElseThrow();
        // Insert data from the controller
        return registeredDeviceRepository.save(path);
    }
}
