package com.blu.device;
import com.blu.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class RegisteredDeviceController {

    @Autowired
    private RegisteredDeviceService registeredDeviceService;

    @GetMapping
    public List<RegisteredDevice> getAllRegisteredDevices() {
        return registeredDeviceService.getAllRegisteredDevice();
    }

    @PostMapping
    public RegisteredDevice addRegisteredDevice(@RequestBody RegisteredDevice registeredDevice) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();



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
