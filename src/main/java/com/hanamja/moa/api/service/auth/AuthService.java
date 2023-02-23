package com.hanamja.moa.api.service.auth;

import com.hanamja.moa.api.dto.auth.request.LoginRequestDto;
import com.hanamja.moa.api.dto.auth.request.RegenerateAccessTokenRequestDto;
import com.hanamja.moa.api.dto.auth.response.LoginResponseDto;
import com.hanamja.moa.api.dto.auth.response.RegenerateAccessTokenResponseDto;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.jwt.JwtTokenUtil;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_token.UserToken;
import com.hanamja.moa.api.entity.user_token.UserTokenRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import com.hanamja.moa.exception.custom.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByStudentId(loginRequestDto.getStudentId()).orElseThrow(
                () -> NotFoundException.builder().message("유저를 찾을 수 없습니다.")
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .build()
        );

        if (loginRequestDto.getPassword() == null || !passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw NotFoundException.builder().message("잘못된 비밀번호입니다.")
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getStudentId(), user.getRole());

        UserToken userToken = UserToken.builder().user(user).refreshToken(refreshToken).build();
        UserToken savedUserToken = userTokenRepository.save(userToken);

        return LoginResponseDto.of(accessToken, savedUserToken.getRefreshToken());
    }

    public RegenerateAccessTokenResponseDto regenerateAccessToken(RegenerateAccessTokenRequestDto requestDto) {

        UserToken userToken = userTokenRepository.findByRefreshToken(requestDto.getRefreshToken()).orElseThrow(
                () -> NotFoundException.builder().message("해당 유저를 찾을 수 없습니다.").httpStatus(HttpStatus.UNAUTHORIZED).build()
        );

        User user = userToken.getUser();

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getStudentId(), user.getRole());

        return RegenerateAccessTokenResponseDto.of(accessToken, refreshToken);
    }
}
