package com.blu.path;

import com.blu.livepath.LivePathService;
import com.blu.user.User;
import com.blu.user.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    // Starting stopping paths ------------------------------------------------------------------------
    @PostMapping("{pathName}")
    private boolean newPath(@PathVariable String pathName) {
        String username = "ntsimerekis@yahoo.com";
        livePathService.startRecording("[::1]", pathName, "ntsimerekis@yahoo.com");
        return true;
    }

    @PostMapping("{pathName}/stop")
    public boolean stopPath(@PathVariable String pathName) {
        String username = "ntsimerekis@yahoo.com";
        return livePathService.stopRecording("[::1]");
    }
    // -------------------------------------------------------------------------------------------------

    //Getting already existing paths
    @GetMapping("{pathName}")
    public ResponseEntity<InputStreamResource> getPathFile(@PathVariable String pathName, @RequestParam Optional<Boolean> json) throws FileNotFoundException {
        String username = "ntsimerekis@yahoo.com";

        Path path = pathRepository.getPathByEmailAndName(username, pathName);

        File file = new File(path.getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

//    @GetMapping
//    public List<Path> getAllPaths() {
//        return pathService.getAllPaths();
//    }
//
//    @PostMapping
//    public Path updatePath(@RequestBody Path path) {
//        return pathService.savePath(path);
//    }
//
//    @PutMapping("/{id}")
//    public Path updatePath(@PathVariable Long id, @RequestBody Path pathDetails) {
//        return pathService.updatePath(id, pathDetails);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deletePath(@PathVariable Long id) {
//        pathService.deletePath(id);
//    }
}
