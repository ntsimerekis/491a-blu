package com.blu.path;

import com.blu.livepath.LivePathService;
import com.blu.user.User;
import com.blu.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    Endpoints for path stuff
 */
@RestController
@RequestMapping("/paths")
public class PathController {

    @Autowired
    private PathService pathService;

    @Autowired
    private LivePathService livePathService;

    @Autowired
    PathRepository pathRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    // Starting - pausing - resuming - stopping paths ----------------------------------------------------
    @PostMapping("/{email}/{pathName}")
    private boolean newPath(@PathVariable String email, @PathVariable String pathName) {
        return livePathService.startRecording(pathName, email);
    }

    @PostMapping("/{email}/{pathName}/pause")
    public boolean pausePath(@PathVariable String email, @PathVariable String pathName) {
        return livePathService.pauseRecording(email);
    }

    @PostMapping("/{email}/{pathName}/resume")
    public boolean resumePath(@PathVariable String email, @PathVariable String pathName) {
        return livePathService.resumeRecording(email);
    }

    @PostMapping("/{email}/{pathName}/stop")
    public boolean stopPath(@PathVariable String email, @PathVariable String pathName) {
        return livePathService.stopRecording(email);
    }
    // -------------------------------------------------------------------------------------------------

    // Getting already existing paths ------------------------------------------------------------------------
    @GetMapping("/{email}/{pathName}")
    public ResponseEntity<InputStreamResource> getPathFile(@PathVariable String pathName, @PathVariable String email, @RequestParam Optional<Boolean> json) throws FileNotFoundException {
        Path path = pathRepository.getPathByEmailAndName(email, pathName);

        File file = new File(path.getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

    /*
        List out all the paths
     */
    @GetMapping("/{email}")
    public List<ObjectNode> getAllPaths(@PathVariable String email) {
        final List<ObjectNode> paths = new ArrayList<ObjectNode>();
        pathRepository.getPathsByEmail(email).forEach(path -> {
            ObjectNode pathNode = mapper.createObjectNode();
            pathNode.put("name", path.getName());
            pathNode.put("email", path.getUsername());
//            pathNode.put("timestamp", path.getTimestamp());
            pathNode.put("ip_address", path.getIpAddress());
            paths.add(pathNode);
        });

        return paths;
    }
    // -------------------------------------------------------------------------------------------------

    // Delete a Path ------------------------------------------------------------------------
    @DeleteMapping("/{email}/{pathName}")
    public void deletePath(@PathVariable String email, @PathVariable String pathName) {
        pathRepository.deletePathByEmailAndName(email, pathName);
    }
    // -------------------------------------------------------------------------------------------------

}
