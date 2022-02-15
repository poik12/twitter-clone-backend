package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.configuration.SecurityConfig;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.AuthResponse;
import com.jd.twitterclonebackend.dto.LoginRequest;
import com.jd.twitterclonebackend.dto.RefreshTokenRequest;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${api-version}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // USER REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<UserEntity> createAccount(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUserAccount(registerRequestDto));
    }

    // EMAIL CONFIRMATION
    @GetMapping("/confirm")
    public String verifyAccount(@RequestParam("token") String token) {
        return authService.confirmUserAccount(token);
    }

    // LOGIN WITH JWT
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Method only for swagger, CustomAuthenticationFilter does all the rest
    }

    // REFRESH ACCESS TOKEN
    @PostMapping("/token/refresh")
    public AuthResponse refreshAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshAccessToken(refreshTokenRequest);
    }

    // DELETE ACCOUNT
    @DeleteMapping("/delete")
    public void deleteAccount() {
        authService.deleteUserAccount();
    }

    //TODO: DOESNT WORK
    //    // CHANGE USER ROLE
//    @PutMapping("/role")
//    public ResponseEntity<?> addRoleToUser(String username, UserRole userRole) {
//        userService.changeUserRole(username, userRole);
//        return ResponseEntity.ok().build();
//    }



}
