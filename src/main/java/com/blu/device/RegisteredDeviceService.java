package com.blu.device;
import com.blu.livepath.LivePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
    Basic Device CRUD operations
 */
@Service
public class RegisteredDeviceService {

    @Autowired
    private RegisteredDeviceRepository registeredDeviceRepository;

    @Autowired
    private LivePathService livePathService;

    public List<RegisteredDevice> getAllRegisteredDevice() {
        return registeredDeviceRepository.findAll();
    }

    public Optional<RegisteredDevice> getRegisteredDeviceById(Long id) {
        return registeredDeviceRepository.findById(id);
    }

    public RegisteredDevice saveRegisteredDevice(RegisteredDevice registeredDevice) {
        //New device new live thread!
        String username = "ntsimerekis@yahoo.com";
        livePathService.startCollecting(registeredDevice.getIpAddress(),username);

        return registeredDeviceRepository.save(registeredDevice);
    }

    public void deleteRegisteredDevice(Long id) {
        registeredDeviceRepository.deleteById(id);
    }

    public RegisteredDevice updateRegisteredDevice(Long id, RegisteredDevice registeredDeviceDetails) {
        RegisteredDevice registeredDevice = registeredDeviceRepository.findById(id).orElseThrow();
        // Insert data from the controller
        return registeredDeviceRepository.save(registeredDeviceDetails);
    }

}
