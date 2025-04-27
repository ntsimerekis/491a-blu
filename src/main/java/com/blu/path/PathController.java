package com.blu.path;

import com.blu.livepath.LivePathService;
import com.blu.livepath.Position;
import com.blu.user.User;
import com.blu.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private static final String UPLOAD_DIR = "uploads/";

    // Customize path
    @PostMapping("custom/{email}/{pathName}")
    private ResponseEntity<String> uploadPath(@PathVariable String email, @PathVariable String pathName, @RequestParam("file") MultipartFile file) {         try {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload!", HttpStatus.BAD_REQUEST);
        }

        java.nio.file.Path uploadPath = Paths.get("paths/" +  email + "/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        java.nio.file.Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);

        //generate JSON


        pathService.savePath(pathName, filePath.toString(), email, "[::1]");

        return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
    } catch (IOException e) {
        return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }

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

        String filePath = path.getFile();

        if (json.isPresent()) {
            if (json.get()) {
                filePath = filePath.replace(".path", ".json");
            }
        }

        File file = new File(filePath);
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
            pathNode.put("timestamp", path.getCreateDate().toString());
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

    // Helper function
    public String convertCsvToJson(MultipartFile file) throws Exception {
        List<Position> points = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header
                    continue;
                }
                String[] fields = line.split(",");
                double x = Double.parseDouble(fields[0]);
                double y = Double.parseDouble(fields[1]);
                String timestamp = fields[2];

                points.add(new Position(x, y, Long.valueOf(timestamp)));
            }
        }

        // Convert list to JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(points);
    }

}
