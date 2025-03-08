package com.blu.path;

import com.blu.livepath.LivePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("{pathName}")
    private boolean newPath(@PathVariable String pathName) {
        String username = "ntsimerekis@yahoo.com";
        livePathService.startRecording("[::1]", pathName, "ntsimerekis@yahoo.com");
        return true;
    }

    @PostMapping("{pathName}/stop")
    private boolean stopPath(@PathVariable String pathName) {
        String username = "ntsimerekis@yahoo.com";
        return livePathService.stopRecording("[::1]");
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
