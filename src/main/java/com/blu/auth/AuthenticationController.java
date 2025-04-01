package com.blu.auth;

import com.blu.auth.dto.LoginUserDto;
import com.blu.auth.dto.RegisterUserDto;
import com.blu.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
/*
    Handles all authentication endpoints. Wraps around service calls
 */
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {

        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/forgot/{email}")
    public ResponseEntity<Boolean> forgotPassword(@PathVariable String email) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @PostMapping("/confirmForgotPassword/{email}")
    public ResponseEntity<Boolean> confirmForgotPassword(@PathVariable String email, @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(authenticationService.confirmForgotPassword(registerUserDto,email));
    }


    @GetMapping("/confirm/{confirmationToken}")
    public ResponseEntity<Boolean> confirm(@PathVariable String confirmationToken) {
        return ResponseEntity.ok(authenticationService.confirm(confirmationToken));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        /*
            Generate the web token
         */
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}