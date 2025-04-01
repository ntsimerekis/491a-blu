package com.blu.auth;

import com.blu.auth.dto.LoginUserDto;
import com.blu.auth.dto.RegisterUserDto;
import com.blu.auth.exception.EmailAlreadyExistsException;
import com.blu.user.User;
import com.blu.user.UserRepository;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blu.email.EmailService;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
    Class handles all authentication backend logic
 */
@Service
public class AuthenticationService {

    Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final ConfirmationTokenRespository confirmationTokenRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    @Value("${blu.auth.email-verification}")
    private boolean requireEmailVerification;

    @Value("${blu.auth.from-email}")
    private String fromEmail;

    @Value("classpath:templates/VerifiedEmail.html")
    private Resource verifiedEmailTemplate;

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

    /*
        Creates a new user, puts them into database
     */
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

        if (requireEmailVerification) {
            ConfirmationToken token = new ConfirmationToken(user);
            logger.info("Token: " + token.getConfirmationToken());

            emailService.send(
                    fromEmail,
                    user.getEmail(),
                    "Your Blu Verification Code",
                    getVerificationEmailString(token.getConfirmationToken())
            );

            confirmationTokenRepository.save(token);
        }
        return user;
    }

    /*
        Unlocks a user if they put in the correct token
     */
    public boolean confirm(String confirmationToken) {
        if (requireEmailVerification) {
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
        return true;
    }

    /*
        Logs in a user
     */
    public User authenticate(LoginUserDto input) {

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(input.getEmail() + " can not be found in user database"));

        if (  requireEmailVerification && !user.isEmailVerified() ) {
            throw new AuthenticationException(input.getEmail() + " not email verified") {};
        } else  {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        }

        return user;
    }

    /*
        Sends out a forgot password link with token
     */
    public boolean forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        ConfirmationToken token = new ConfirmationToken(user);
        confirmationTokenRepository.save(token);

        return emailService.send(
                fromEmail,

                email,

                "Blu Password Reset",

                """
                        Hello, let's reset your password!
                        Click on this link to reset your password.
                        https://localhost:8080/confirmForgotPassword/
                        """ + token.getConfirmationToken()
        );
    }

    /*
        Handles a forgot password confirmation.
     */
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

    /*
        Loads HTML file from resources and injects link into it
     */
    private String getVerificationEmailString(String vericationToken) {
        String emailHTML = vericationToken;
        try {
            String marker = "<!--INSERT VERIFICATION LINK BELOW-->";
            String linkHtml = "<a class=\"btn\" href=\"" + "http://localhost:5173/#/verificationconfirmed?token="
                    + vericationToken + "\">Click here to verify your email</a>";
            emailHTML = StreamUtils.copyToString(verifiedEmailTemplate.getInputStream(), StandardCharsets.UTF_8)
                    .replace(marker, marker + "\n" + linkHtml);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return emailHTML;
    }
}