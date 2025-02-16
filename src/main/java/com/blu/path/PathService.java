package com.blu.path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
    Basic CRUD for Paths
 */
@Service
public class PathService {

    @Autowired
    private PathRepository pathRepository;

    public List<Path> getAllPaths() {
        return pathRepository.findAll();
    }

    public Optional<Path> getPathById(Long id) {
        return pathRepository.findById(id);
    }

    public Path savePath(Path path) {
        return pathRepository.save(path);
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
