package com.hanamja.moa.api.controller.auth;

import com.hanamja.moa.api.dto.auth.request.LoginRequestDto;
import com.hanamja.moa.api.dto.auth.request.RegenerateAccessTokenRequestDto;
import com.hanamja.moa.api.dto.auth.response.LoginResponseDto;
import com.hanamja.moa.api.dto.auth.response.RegenerateAccessTokenResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @GetMapping(value = "/info")
    public ResponseEntity<?> myInfo(@AuthenticationPrincipal UserAccount userAccount){
        String studentId = userAccount.getStudentId();
        return ResponseEntity.ok().body(studentId);
    }

    @PostMapping("/regenerate-access-token")
    public ResponseEntity<RegenerateAccessTokenResponseDto> regenerateAccessToken(
            @RequestBody RegenerateAccessTokenRequestDto requestDto
    ) {
        RegenerateAccessTokenResponseDto responseDto = authService.regenerateAccessToken(requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
