package com.blu.user;

import com.blu.device.Device;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
    User CRUD endpoints. Wraps around the services
 */
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    ObjectMapper mapper;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping
    public List<ObjectNode> allUsers() {
        final List<ObjectNode> users = new ArrayList<ObjectNode>();
        userService.allUsers().forEach(user -> {
            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("fullName", user.getFullName());
            userNode.put("email", user.getUsername());
            userNode.put("emailVerified", user.isEmailVerified());
            userNode.put("enabled", user.isEnabled());
            ArrayNode roles = mapper.createArrayNode();
            user.getAuthorities().forEach(authority -> {roles.add(authority.getAuthority());});
            userNode.set("authorities", roles);
            userNode.put("accountNonExpired", user.isAccountNonExpired());
            users.add(userNode);
        });

        return users;
    }

    @PostMapping
    public User updateUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{email}")
    public User updateUser(@PathVariable String email, @RequestBody User userDetails) {
        return userService.updateUser(email, userDetails);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String email) {
        if(userService.deleteUser(email) != 0) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity
                .status(418)
                .body(false);
    }
}