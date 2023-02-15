package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.request.LoginRequestDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.TokenResponseDto;
import com.yusufislamdemir.creditapplicationsystem.security.TokenGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;

    private final TokenGenerator tokenGenerator;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, TokenGenerator tokenGenerator, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
    }


    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            Authentication auth =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getTcIdentityNumber(), loginRequestDto.getPassword()));
            return TokenResponseDto.builder()
                    .token(tokenGenerator.generate(auth))
                    .userDto(userService.getUserDtoByTcIdentityNumber(loginRequestDto.getTcIdentityNumber()))
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException(loginRequestDto.getTcIdentityNumber());
        }
    }
}