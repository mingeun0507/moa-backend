package com.hanamja.moa.api.controller.auth;

import com.hanamja.moa.api.dto.auth.request.LoginRequestDto;
import com.hanamja.moa.api.dto.auth.request.OnBoardingRequestDto;
import com.hanamja.moa.api.dto.auth.request.RegenerateAccessTokenRequestDto;
import com.hanamja.moa.api.dto.auth.response.LoginResponseDto;
import com.hanamja.moa.api.dto.auth.response.RegenerateAccessTokenResponseDto;
import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "auth", description = "인증 관련 API")
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
    public ResponseEntity<?> myInfo(@AuthenticationPrincipal UserAccount userAccount) {
        String studentId = userAccount.getStudentId();
        return ResponseEntity.ok().body(studentId);
    }

    @PutMapping("/on-boarding")
    public ResponseEntity<UserInfoResponseDto> onBoardUser(@AuthenticationPrincipal UserAccount userAccount, @RequestBody OnBoardingRequestDto onBoardingRequestDto) {

        return ResponseEntity.ok(authService.onBoardUser(userAccount, onBoardingRequestDto));
    }

    @PostMapping("/regenerate-access-token")
    public ResponseEntity<RegenerateAccessTokenResponseDto> regenerateAccessToken(
            @RequestBody RegenerateAccessTokenRequestDto requestDto
    ) {
        RegenerateAccessTokenResponseDto responseDto = authService.regenerateAccessToken(requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
