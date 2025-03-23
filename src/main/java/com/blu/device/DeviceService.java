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
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private LivePathService livePathService;

    public List<Device> getAllRegisteredDevice() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getRegisteredDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

//    public Device saveRegisteredDevice(Device device) {
//        //New device new live thread!
//        String username = "ntsimerekis@yahoo.com";
//        livePathService.startCollecting(device.getIpAddress(), username);
//
//        return deviceRepository.save(device);
//    }

//    public void deleteRegisteredDevice(Long id) {
//        deviceRepository.deleteById(id);
//    }

    public Device updateRegisteredDevice(Long id, Device deviceDetails) {
        Device device = deviceRepository.findById(id).orElseThrow();
        // Insert data from the controller
        return deviceRepository.save(deviceDetails);
    }

}
