package com.blu.controller;
import com.blu.model.RegisteredDevice;
import com.blu.service.RegisteredDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RegisteredDeviceController {

    @Autowired
    private RegisteredDeviceService registeredDeviceService;

    @GetMapping
    public List<RegisteredDevice> getAllRegisteredDevices() {
        return registeredDeviceService.getAllRegisteredDevice();
    }

    @PostMapping
    public RegisteredDevice addRegisteredDevice(@RequestBody RegisteredDevice registeredDevice) {
        return registeredDeviceService.saveRegisteredDevice(registeredDevice);
    }

    @PutMapping("/{id}")
    public RegisteredDevice updateRegisteredDevice(@PathVariable Long id, @RequestBody RegisteredDevice registeredDeviceDetails) {
        return registeredDeviceService.updateRegisteredDevice(id, registeredDeviceDetails);
    }

    @DeleteMapping
    public void deleteRegisteredDevice(@PathVariable long id) {
        registeredDeviceService.deleteRegisteredDevice(id);
    }
}
