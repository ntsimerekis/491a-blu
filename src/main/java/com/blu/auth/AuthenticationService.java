package com.blu.auth;

import com.blu.user.User;
import com.blu.user.UserRepository;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blu.email.EmailService;

@Service
public class AuthenticationService {

    Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final ConfirmationTokenRespository confirmationTokenRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            ConfirmationTokenRespository confirmationTokenRepository,
            EmailService emailService,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        //user object when saved changes
        user = userRepository.save(user);

        ConfirmationToken token = new ConfirmationToken(user);
        logger.info("Token: " + token.getConfirmationToken());

        emailService.send(
                "ntsimerekis@gmail.com",
                user.getEmail(),
                "Your Blu Verification Code",
                "<a href=\"http://localhost:8080/auth/confirm/" + token.getConfirmationToken() + "\"> Email verification link! </a>"
        );

        confirmationTokenRepository.save(token);

        return user;
    }

    public boolean confirm(String confirmationToken) {
        if (confirmationTokenRepository.existsByConfirmationToken(confirmationToken)) {
            User confirmedUser = confirmationTokenRepository.findByConfirmationToken(confirmationToken)
                    .get()
                    .getUser();
            confirmedUser.setEmailVerified(true);
            userRepository.save(confirmedUser);
            return true;
        }
        return false;
    }

    public User authenticate(LoginUserDto input) {

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(input.getEmail()));

        if (user.isEmailVerified()) {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } else {
            throw new UsernameNotFoundException("not verified");
        }

        return user;
    }

    public boolean forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        ConfirmationToken token = new ConfirmationToken(user);
        confirmationTokenRepository.save(token);

        return emailService.send(
                "ntsimerekis@yahoo.com",

                email,

                "Blu Password Reset",

                """
                        Hello, let's reset your password!
                        Click on this link to reset your password.
                        https://localhost:8080/confirmForgotPassword/
                        """ + token.getConfirmationToken()
        );
    }

    public boolean confirmForgotPassword(RegisterUserDto userDto, String confirmationToken) {

        if (confirmationTokenRepository.existsByConfirmationToken(confirmationToken)) {
            User confirmedUser = confirmationTokenRepository.findByConfirmationToken(confirmationToken)
                    .get()
                    .getUser();
            confirmedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(confirmedUser);
            return true;
        }
        return false;
    }
}