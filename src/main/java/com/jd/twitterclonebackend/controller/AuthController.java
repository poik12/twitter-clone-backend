package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.request.LoginRequestDto;
import com.jd.twitterclonebackend.dto.request.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.dto.response.AuthResponseDto;
import com.jd.twitterclonebackend.dto.response.EmailConfirmationDto;
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
@ApiRestController
public class AuthController {

    private final AuthService authService;

    // USER REGISTRATION
    @PostMapping(path = "/register")
    public ResponseEntity<UserEntity> createAccount(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.createUserAccount(registerRequestDto));
    }

    // EMAIL CONFIRMATION
    @GetMapping(path ="/confirm")
    public EmailConfirmationDto verifyAccount(@RequestParam("token") String token) {
        return authService.confirmUserAccount(token);
    }

    // LOGIN WITH JWT
    @PostMapping(path ="/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto) {
        // Method only for swagger, CustomAuthenticationFilter does all the rest
    }

    // REFRESH ACCESS TOKEN
    @PostMapping(path ="/token/refresh")
    public AuthResponseDto refreshAccessToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return authService.refreshAccessToken(refreshTokenRequestDto);
    }

    // DELETE ACCOUNT
    @DeleteMapping(path ="/delete")
    public void deleteAccount() {
        authService.deleteUserAccount();
    }

}
