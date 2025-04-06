package com.blu.livepath;

import com.blu.path.PathService;
import org.apache.logging.log4j.LogManager;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/*
    Requests positions from coap server and puts them into a
 */
public class PositionCollector implements Runnable {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PositionCollector.class);

    String ipAddress;

    //Stop collecting data (unregistering a device)
    private boolean stopThread = false;

    private int sampleRate = 3; //default

    //Record the Path Recording state
    private String pathName;

    private String userName;

    //When this is false and there is some data, data is dumped into path file
    private boolean recording = false;

    //Paused means we do not record data but the overall recording is not stopped
    private boolean paused = false;

    private final String pathDirectory;

    private CoapClient coapClient;

    private final Queue<Position> queue;

    private final Queue<Long> timestamps;

    private final SimpMessagingTemplate messagingTemplate;

    private final PathService pathService;

    //Some Californium setup stuff. Only need to one once but it's nice to have here
    static {
        CoapConfig.register();
        UdpConfig.register();
//        TcpConfig.register();
    }

    public PositionCollector(String ipAddress, SimpMessagingTemplate messagingTemplate, String pathDirectory, PathService  pathService, String initialUserName) {
        this.ipAddress = ipAddress;
        this.userName = initialUserName;
        this.coapClient = new CoapClient("coap://" + ipAddress + "/" + "position");
        this.queue = new LinkedList<>();
        this.timestamps = new LinkedList<>();
        this.messagingTemplate = messagingTemplate;
        this.pathDirectory = pathDirectory;
        this.pathService = pathService;
    }

    @Override
    public void run() {
        while(!stopThread) {
            try {
                CoapResponse response;
                //Read value from CoAP Client. If null, skip.
                if ( (response = coapClient.get()) == null) {
                    continue;
                }

                double[] coords = Arrays.stream(response.getResponseText().split(","))
                        .mapToDouble(Double::parseDouble)
                        .toArray();

                //coords[0] = x, coords[1] = y
                var pos = new Position(coords[0], coords[1]);


                //Tell the frontend about this point
                messagingTemplate.convertAndSend("/topic/" + userName, pos);

                if (recording) {
                    //We are recording, let's record this point
                    if(!paused) {
                        queue.add(pos);
                        timestamps.add(System.currentTimeMillis());

                    }
                } else {
                    //We are not recording. Let's check the queue to see if there is some data recorded
                    if (queue.size() > 1) {
                        dumpPath();
                        //Now the queue is cleared and stored in the database
                        //Recording is STOPPED

                        //Else we are paused and we ought not to dump the path
                    }
                }
                TimeUnit.SECONDS.sleep(sampleRate);
            }
            catch (IOException | NumberFormatException | ConnectorException e ) {
                log.error(e);
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e1) {
                    log.error(e1);
                }
            } catch (InterruptedException e) {
                log.info("Thread interrupted");
            }
        }
        //Here we are going to dump the queue into a file
        dumpPath();
    }

    public boolean startRecording(String pathName, String userName) {
        if (!recording) {
            this.pathName = pathName;
            this.userName = userName;
            recording = true;
            return true;
        }
        // We are recording right now, we can not make a new recording
        return false;
    }

    public boolean pauseRecording() {
        if (recording) {
            this.paused = true;
            return true;
        }
        //can't pause if not recording
        return false;
    }

    public boolean resumeRecording() {
        if (recording) {
            this.paused = false;
            return true;
        }
        //can't resume if not recording
        return false;
    }

    public boolean stopRecording() {
        if (recording) {
            recording = false;
            return true;
        }
        //We are already stopped
        return false;
    }

    public boolean isRecording() {
        return recording;
    }

    public void stop() {
        stopThread = true;
    }

    public boolean setIpAddress(String ipAddress) {

        if (!recording) {
            this.ipAddress = ipAddress;

            coapClient.shutdown();
            coapClient = new CoapClient("coap://" + ipAddress + "/" + "position");
            return true;
        }

        //We can't change the ip address if we are recording!
        return false;
    }


    //Dumps the queue into a path file
    private void dumpPath() {
        String filePath = pathDirectory + userName + "/" + pathName + ".path";
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            PrintStream pathOut = new PrintStream(new FileOutputStream(file));

            //We've defined the toString method in the record
            int numberOfLines = queue.size();

            for (int i = 0; i < numberOfLines; i++) {
                pathOut.println(queue.poll() + "," + timestamps.poll());
            }
            pathOut.close();

            //Store in Mariadb
            pathService.savePath(pathName, filePath, userName, ipAddress);

        } catch (IOException e) {
            log.error(e);
        } finally {
            //revert dump and paused to false, clear queue
            queue.clear();
        }
    }
}
