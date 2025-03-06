package com.blu.user;

import com.blu.auth.Dto.ProfileUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@CrossOrigin(origins = "*")
@RestController
public class UserController {
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

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/fetch")
    public User fetchUser(@RequestBody ProfileUserDto profileUserDto) {
        return userService.getUserByEmail(profileUserDto.getEmail());
    }

    @PostMapping
    public User updateUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{email}")
    public User updateUser(@PathVariable String email, @RequestBody User userDetails) {
        return userService.updateUser(email, userDetails);
    }

    @PostMapping("/deleteProfile")
    public ResponseEntity<Boolean> delete(@RequestBody ProfileUserDto ProfileUserDto){
        userService.deleteUser(ProfileUserDto.getEmail());
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}