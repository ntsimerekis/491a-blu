package com.blu.livepath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("livepath/")

/*
    For control of the underlying collector threads. Mostly for debug purposes
 */
public class LivePathController {

    @Autowired
    private LivePathService livePathService;

    @GetMapping("/endAll")
    public void endAll() {
        livePathService.stopAllCollectingThreads();
    }

}
