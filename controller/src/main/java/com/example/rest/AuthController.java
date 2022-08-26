package com.example.rest;

import com.example.entity.User;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.auth.LoginRequest;
import com.example.rest.payload.auth.LoginResponse;
import com.example.rest.payload.auth.SignUpRequest;
import com.example.security.jwt.JwtUtils;
import com.example.security.services.UserDetailsImpl;
import com.example.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.utils.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            return ResponseEntity.ok().body(new LoginResponse(0, jwtCookie.getValue()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok().body(new GenericResponse(ERROR_CODE_INVALID_LOGIN));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            if (userService.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.ok().body(new GenericResponse(ERROR_CODE_EMAIL_ALREADY_TAKEN));
            }
            if (userService.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.ok().body(new GenericResponse(ERROR_CODE_USERNAME_ALREADY_TAKEN));
            }
            // Create new user's account
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            userService.register(user);
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }
}