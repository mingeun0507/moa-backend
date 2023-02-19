package com.hanamja.moa.api.controller.auth;

import com.hanamja.moa.api.dto.auth.request.LoginRequestDto;
import com.hanamja.moa.api.dto.auth.request.RegenerateAccessTokenRequestDto;
import com.hanamja.moa.api.dto.auth.response.LoginResponseDto;
import com.hanamja.moa.api.dto.auth.response.RegenerateAccessTokenResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccountService;
import com.hanamja.moa.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto responseDto = authService.login(loginRequestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/regenerate-access-token")
    public ResponseEntity<RegenerateAccessTokenResponseDto> regenerateAccessToken(
            @RequestBody RegenerateAccessTokenRequestDto requestDto
    ) {
        RegenerateAccessTokenResponseDto responseDto = authService.regenerateAccessToken(requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
