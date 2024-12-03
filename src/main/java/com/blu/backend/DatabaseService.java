package com.blu.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAllRows(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.queryForList(sql);
    }

    public int insertRows(String tableName, int id, String message) {
        String sql = "INSERT INTO " + tableName + "(id, message) VALUES (" + id + ", \'" + message + "\' )";
        return jdbcTemplate.update(sql);
    }
}
