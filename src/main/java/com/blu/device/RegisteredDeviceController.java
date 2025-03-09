package com.blu.device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
    Handles the endpoints for registered Devices (Done by Luke T.)
*/
@RequestMapping("device")
@RestController
public class RegisteredDeviceController {

    @Autowired
    private RegisteredDeviceService registeredDeviceService;

    // Lists all registered devices
    @GetMapping("/")
    public List<RegisteredDevice> getAllRegisteredDevices() {
        return registeredDeviceService.getAllRegisteredDevice();
    }

    // Adds device
    @PostMapping
    public RegisteredDevice addRegisteredDevice(@RequestBody RegisteredDevice registeredDevice) {
        return registeredDeviceService.saveRegisteredDevice(registeredDevice);
    }

    // Updates an id if needed
    @PutMapping("/{id}")
    public RegisteredDevice updateRegisteredDevice(@PathVariable Long id, @RequestBody RegisteredDevice registeredDeviceDetails) {
        return registeredDeviceService.updateRegisteredDevice(id, registeredDeviceDetails);
    }

    // Deletes device
    @DeleteMapping
    public void deleteRegisteredDevice(@PathVariable long id) {
        registeredDeviceService.deleteRegisteredDevice(id);
    }
}
