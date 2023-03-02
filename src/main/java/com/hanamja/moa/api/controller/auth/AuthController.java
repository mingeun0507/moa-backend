package com.hanamja.moa.api.controller.auth;

import com.hanamja.moa.api.dto.auth.request.LoginRequestDto;
import com.hanamja.moa.api.dto.auth.request.OnBoardingRequestDto;
import com.hanamja.moa.api.dto.auth.request.RegenerateAccessTokenRequestDto;
import com.hanamja.moa.api.dto.auth.response.LoginResponseDto;
import com.hanamja.moa.api.dto.auth.response.RegenerateAccessTokenResponseDto;
import com.hanamja.moa.api.dto.user.request.SignUpRequestDto;
import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.auth.AuthService;
import com.hanamja.moa.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "auth", description = "인증 관련 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${google-form-custom-auth-key}")
    private String googleFormCustomAuthKey;

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<UserInfoResponseDto> signUp(
            @RequestHeader(value="Authorization") String authorization,
            @Validated @RequestBody SignUpRequestDto signUpRequestDto)
    {

        if (!authorization.equals("Bearer " + googleFormCustomAuthKey)){
            throw new UnauthorizedException(HttpStatus.FORBIDDEN, "토큰이 일치하지 않습니다.");
        }

        UserInfoResponseDto responseDto = authService.signUp(signUpRequestDto);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto responseDto = authService.login(loginRequestDto);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "온보딩")
    @PutMapping("/on-boarding")
    public ResponseEntity<UserInfoResponseDto> onBoardUser(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @Validated @RequestBody OnBoardingRequestDto onBoardingRequestDto) {

        return ResponseEntity.ok(authService.onBoardUser(userAccount, onBoardingRequestDto));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/regenerate-access-token")
    public ResponseEntity<RegenerateAccessTokenResponseDto> regenerateAccessToken(
            @Validated @RequestBody RegenerateAccessTokenRequestDto requestDto
    ) {
        RegenerateAccessTokenResponseDto responseDto = authService.regenerateAccessToken(requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
