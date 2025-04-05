package com.blu.device;
import com.blu.device.dto.DeviceDto;
import com.blu.livepath.LivePathService;
import com.blu.user.User;
import com.blu.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseEntity<String> addRegisteredDevice(@PathVariable String email, @RequestBody DeviceDto deviceDto) {;
        //Add brackets if they do not exist.
        if (!deviceDto.getIpAddress().contains("["))
            deviceDto.setIpAddress("[" + deviceDto.getIpAddress() + "]");

        //Initial check on isRecording and active. Let's just get that taken care of right away
        if (livePathService.isRecording(email) && deviceDto.isActive()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("Can not create or switch to an active device while a recording is in progress");
        }

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

//          //Set this as an active device no matter what
            device.setActive(true);
//
//          //Resave the device
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

        return ResponseEntity.ok("Device added successfully");
    }

    @GetMapping("/{email}")
    public List<Device> getDeviceByEmail(@PathVariable String email) {
        return userRepository.getDevicesByEmail(email);
    }

    @GetMapping("/{email}/{ipAddress}")
    public Device getDeviceByIp(@PathVariable String email, @PathVariable String ipAddress) {
        ipAddress = "[" + ipAddress + "]";
        return deviceRepository.findByIpAddress(ipAddress);
    }

//    @PutMapping("/{id}")
//    public Device updateRegisteredDevice(@PathVariable Long id, @RequestBody Device deviceDetails) {
//        return deviceService.updateRegisteredDevice(id, deviceDetails);
//    }

    @DeleteMapping("/{email}/{ipAddress}")
    public ResponseEntity<String> deleteRegisteredDevice(@PathVariable String email, @PathVariable String ipAddress) {
        //temporary
        ipAddress = "[" + ipAddress + "]";

        //Check if user exists
        User user;
        Optional<User> opt_user = userRepository.findByEmail(email);
        if (opt_user.isPresent()){
            user = opt_user.get();
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("User not found");
        }

        //Check if a device is active
        if (Objects.equals(user.getActiveDevice().getIpAddress(), ipAddress)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("Can not delete an active device");
        }

        deviceRepository.deleteByEmailAndIpAddress(email, ipAddress);

        return ResponseEntity.ok("Device deleted successfully");
    }
}
