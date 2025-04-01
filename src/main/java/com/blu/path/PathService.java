package com.blu.path;
import com.blu.device.Device;
import com.blu.device.DeviceRepository;
import com.blu.user.User;
import com.blu.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/*
    Basic CRUD for Paths
 */
@Service
public class PathService {

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePath(String pathName, String filePath, String username, String ipAddress) {
        Device device = deviceRepository.findByIpAddress(ipAddress);
        User user = userRepository.findByEmail(username).get();
        pathRepository.save(new Path(pathName, filePath, user, device));
    }
}
