package com.blu.path;

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

    @GetMapping("/")
    public List<Path> getAllPaths() {
        return pathService.getAllPaths();
    }

    @PostMapping
    public Path updatePath(@RequestBody Path path) {
        return pathService.savePath(path);
    }

    @PutMapping("/{id}")
    public Path updatePath(@PathVariable Long id, @RequestBody Path pathDetails) {
        return pathService.updatePath(id, pathDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePath(@PathVariable Long id) {
        pathService.deletePath(id);
    }
}
