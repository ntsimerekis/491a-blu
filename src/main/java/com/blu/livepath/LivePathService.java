package com.blu.livepath;

import com.blu.path.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class LivePathService {
    @Autowired

    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    PathService pathService;

    @Value("${blu.path.path-storage}")
    private String pathDirectory;

    private final Random random = new Random();

    //Each user should have one thread which listens to a device. Then they can change their active device.
    private final HashMap<String, PositionCollector> collectTasks = new HashMap<>();

    //Starting and stopping path
    public void startCollecting(String ipAddress, String userName) {
        PositionCollector task = new PositionCollector(ipAddress, messagingTemplate, pathDirectory, pathService, userName);
        taskExecutor.execute(task);
        collectTasks.put(userName, task);
    }

    public boolean startRecording(String pathName, String username) {
        return collectTasks.get(username).startRecording(pathName, username);
    }

    public boolean pauseRecording(String userName) {
        return collectTasks.get(userName).pauseRecording();
    }

    public boolean resumeRecording(String userName) {
        return collectTasks.get(userName).resumeRecording();
    }

    public boolean stopRecording(String username) {
        return collectTasks.get(username).stopRecording();
    }

    /*
        Set active device
     */
    public boolean setActiveDevice(String userName, String ipAddress) {
        return collectTasks.get(userName).setIpAddress(ipAddress);
    }

    /*
        Check if we are recording
     */
    public boolean isRecording(String userName) {
        try {
            return collectTasks.get(userName).isRecording();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /*
     *  Stops all collecting threads! Careful
     */
    public void stopCollectingThread(String userName) {
        collectTasks.get(userName).stop();
    }

    public void stopAllCollectingThreads() {
        collectTasks.values().forEach(PositionCollector::stop);
    }
}
