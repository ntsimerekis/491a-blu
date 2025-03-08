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

    private final HashMap<String, PositionCollector> collectTasks = new HashMap<>();

    //Starting and stopping path
    public void startCollecting(String ipAddress, String userName) {
        PositionCollector task = new PositionCollector(ipAddress, messagingTemplate, pathDirectory, pathService, userName);
        taskExecutor.execute(task);
        collectTasks.put(ipAddress, task);
    }

    public boolean startRecording(String ipAddress, String pathName, String username) {
        return collectTasks.get(ipAddress).startRecording(pathName, username);
    }

    public boolean pauseCurrentRecording(String ipAddress) {
        return collectTasks.get(ipAddress).pauseRecording();
    }

    public boolean stopRecording(String ipAddress) {
        return collectTasks.get(ipAddress).stopRecording();
    }

    /*
     *  Stops all collecting threads! Careful
     */
    public void stopCollectingThread(String ipAddress) {
        collectTasks.get(ipAddress).stop();
    }

    public void stopAllCollectingThreads() {
        collectTasks.values().forEach(PositionCollector::stop);
    }
}
