package com.blu.device;
import com.blu.device.dto.DeviceDto;
import com.blu.livepath.LivePathService;
import com.blu.user.User;
import com.blu.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LivePathService livePathService;

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping
    public List<Device> getAllRegisteredDevices() {
        return deviceService.getAllRegisteredDevice();
    }

    @PostMapping("/{email}")
    public Device addRegisteredDevice(@PathVariable String email, @RequestBody DeviceDto deviceDto) {;

        //Extract owner User object
        User owner = userRepository.findByEmail(email).orElseThrow();

        //Create a new RegisteredDevice object

        Device device;

        //Save the new registered device
        if (!deviceRepository.existsByIpAddress(deviceDto.getIpAddress())) {
            //Create a new device object and store it
            device = new Device(deviceDto.getName(),deviceDto.getIpAddress(), owner, deviceDto.isActive());
            deviceRepository.save(device);

            //Add device to list of owner's device
            owner.addDevice(device);
            userRepository.save(owner);
        } else {
            //Fetch our old device
            device = deviceRepository.findByIpAddress(deviceDto.getIpAddress());
        }


        //Check if any device exists
        //If the device we just added was the first
        if (owner.getDevices().size() == 1) {
            //First device, first thread!
            livePathService.startCollecting(device.getIpAddress(), email);

//            //Set this as an active device no matter what
            device.setActive(true);
//
//            //Resave the device
            deviceRepository.save(device);

            //Set user's active device in the database
            userRepository.setUserActiveDevice(email, deviceDto.getIpAddress());
        } else {
            //User already has some registered devices

            //Check to see if user wants this as an active device
            if (deviceDto.isActive()) {
                //Change the current active device to not active
                Device activeDevice = owner.getActiveDevice();
                activeDevice.setActive(false);
                deviceRepository.save(activeDevice);

                //Make the current device active and save
                device.setActive(true);
                deviceRepository.save(device);

                //Change the user's active device
                userRepository.setUserActiveDevice(email, deviceDto.getIpAddress());

                //Switch the user's thread to start listening on the new device
                livePathService.setActiveDevice(email, device.getIpAddress());
            }
        }

        //Add the device to the user's list if it is not already there
//        if (!owner.getDevices().contains(device)) {
//            owner.addDevice(device);
//            userRepository.save(owner);
//        }

        return device;
    }

    @GetMapping("/{email}")
    public List<Device> getDeviceByEmail(@PathVariable String email) {
        return userRepository.getDevicesByEmail(email);
    }

//    @PutMapping("/{id}")
//    public Device updateRegisteredDevice(@PathVariable Long id, @RequestBody Device deviceDetails) {
//        return deviceService.updateRegisteredDevice(id, deviceDetails);
//    }

    @DeleteMapping("/{ipAddress}")
    public void deleteRegisteredDevice(@PathVariable String ipAddress) {
        deviceRepository.deleteByIpAddress(ipAddress);
    }
}
