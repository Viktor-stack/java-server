package com.rujavacours.auth.controller;

import com.rujavacours.auth.entity.Activity;
import com.rujavacours.auth.entity.Role;
import com.rujavacours.auth.entity.User;
import com.rujavacours.auth.exception.JsonException;
import com.rujavacours.auth.exception.RoleNotFoundException;
import com.rujavacours.auth.exception.UserAlreadyActivatedException;
import com.rujavacours.auth.exception.UserOrEmailExistsException;
import com.rujavacours.auth.service.EmailService;
import com.rujavacours.auth.service.UserDetailsImpl;
import com.rujavacours.auth.service.UserDetailsServiceImpl;
import com.rujavacours.auth.service.UserService;
import com.rujavacours.auth.util.CookieUtils;
import com.rujavacours.auth.util.JwtUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.rujavacours.auth.service.UserService.DEFAULT_ROLE;


import javax.validation.Valid;
import java.util.UUID;


@Log
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final EmailService emailService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookieUtils cookieUtils, EmailService emailService, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/test")
    public String test() {
        return "OK";
    }


    @PostMapping("/test-no-auth")
    public String testNoAuth() {
        return "test-no-auth";
    }

    @PostMapping("/test-whit-auth")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String testWhitAuth() {
        return "Hello USER";
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity logout() {
        HttpCookie cookie = cookieUtils.deleteCookie();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/update-password")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> updatePassword(@RequestBody String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        int updateCount = userService.updatePassword(encoder.encode(password), user.getUsername());
        return ResponseEntity.ok(updateCount == 1);
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) throws RoleNotFoundException {

        if (userService.userExists(user.getUsername(), user.getEmail())) {
            throw new UserOrEmailExistsException("User or email already exists");
        }


        Role userRole = userService.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found.")); // если в БД нет такой роли - выбрасываем исключение
        user.getRoles().add(userRole);

        user.setPassword(encoder.encode(user.getPassword())); // одностороннее шифрование пароля

        // создание активации пользователя в ДБ
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setUuid(UUID.randomUUID().toString());
        userService.register(user, activity);

        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());

        return ResponseEntity.ok().build();// создали пользователя!
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {
        Activity activity = userService.findActivityByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("Activate Not found with uuid" + uuid));
        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated");
        }
        int updateCount = userService.activate(uuid);
        return ResponseEntity.ok(updateCount == 1);
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.isActivated()) {
            throw new DisabledException("User disabled");
        }

        String jwt = jwtUtils.createAccessToken(userDetails.getUser());

        HttpCookie cookie = cookieUtils.createJwtCookie(jwt);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
    }

    @PostMapping("/resend-activate-email")
    public ResponseEntity resendActivateEmail(@RequestBody String usernameOrEmail) {
        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(usernameOrEmail);
        Activity activity = userService.findActivityById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Activity not found with user" + usernameOrEmail));
        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated " + user.getUsername());
        }
        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/send-reset-password-email")
    public ResponseEntity sendEmailResendPassword(@RequestBody String email) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
        User user = userDetails.getUser();
        user.setPassword(null);
        if (userDetails != null) {
            emailService.sendResetPasswordEmail(user.getEmail(), jwtUtils.crateEmailResetToken(user));
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handlerException(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
