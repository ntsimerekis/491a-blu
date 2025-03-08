package com.blu.path;
import com.blu.device.RegisteredDevice;
import com.blu.device.RegisteredDeviceRepository;
import com.blu.livepath.Position;
import com.blu.user.User;
import com.blu.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
    private RegisteredDeviceRepository registeredDeviceRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Path> getAllPaths() {
        return pathRepository.findAll();
    }

    public void savePath(String pathName, String filePath, String username, String ipAddress) {
        RegisteredDevice registeredDevice = registeredDeviceRepository.findByIpAddress(ipAddress);
        User user = userRepository.findByEmail(username).get();
        pathRepository.save(new Path(pathName, filePath, user, registeredDevice));
    }

    public void deletePath(Long id) {
        pathRepository.deleteById(id);
    }

    public Path updatePath(Long id, Path pathDetails) {
        Path path = pathRepository.findById(id).orElseThrow();
        // Insert data from the controller
        return pathRepository.save(path);
    }
}
