package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.dto.*;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
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
    public EmailConfirmationDto verifyAccount(@RequestParam("token") String token) {
        return authService.confirmUserAccount(token);
    }

    // LOGIN WITH JWT
    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto) {
        // Method only for swagger, CustomAuthenticationFilter does all the rest
    }

    // REFRESH ACCESS TOKEN
    @PostMapping("/token/refresh")
    public AuthResponseDto refreshAccessToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return authService.refreshAccessToken(refreshTokenRequestDto);
    }

    // DELETE ACCOUNT
    @DeleteMapping("/delete")
    public void deleteAccount() {
        authService.deleteUserAccount();
    }

}
