package com.blu.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/data")
    public List<Map<String, Object>> getData(@RequestParam String tableName) {
        return databaseService.getAllRows(tableName);
    }

    @GetMapping("/insert")
    public int addData(@RequestParam String tableName, @RequestParam int id, @RequestParam String message) {
        return databaseService.insertRows(tableName, id, message);
    }

}
