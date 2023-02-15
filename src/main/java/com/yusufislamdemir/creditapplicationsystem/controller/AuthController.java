package com.yusufislamdemir.creditapplicationsystem.controller;

import com.yusufislamdemir.creditapplicationsystem.dto.request.LoginRequestDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.TokenResponseDto;
import com.yusufislamdemir.creditapplicationsystem.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }
}
