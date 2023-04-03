package com.hanamja.moa.api.service.auth;

import com.hanamja.moa.api.dto.auth.request.LoginRequestDto;
import com.hanamja.moa.api.dto.auth.request.OnBoardingRequestDto;
import com.hanamja.moa.api.dto.auth.request.RegenerateAccessTokenRequestDto;
import com.hanamja.moa.api.dto.auth.response.LoginResponseDto;
import com.hanamja.moa.api.dto.auth.response.RegenerateAccessTokenResponseDto;
import com.hanamja.moa.api.dto.user.request.SignUpRequestDto;
import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_department.UserDepartment;
import com.hanamja.moa.api.entity.user_department.UserDepartmentRepository;
import com.hanamja.moa.api.entity.user_token.UserToken;
import com.hanamja.moa.api.entity.user_token.UserTokenRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import com.hanamja.moa.exception.custom.UserInputException;
import com.hanamja.moa.filter.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;
    private final UserDepartmentRepository userDepartmentRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        List<String> accessTokens = new ArrayList<>();
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
        String mainAccessTokens = user.getDepartments().stream()
                .filter(userDepartment -> userDepartment.getIsMain().equals(Boolean.TRUE))
                .map(UserDepartment::getDepartment)
                .map(department -> jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), department, user.getRole(), user.getIsActive(), user.getIsOnboarded()))
                .collect(Collectors.toList()).get(0);
        accessTokens.add(mainAccessTokens);
        if (user.getDepartments().size() > 1) {
            accessTokens.addAll(user.getDepartments().stream()
                    .filter(userDepartment -> userDepartment.getIsMain().equals(Boolean.FALSE))
                    .map(UserDepartment::getDepartment)
                    .map(department -> jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), department, user.getRole(), user.getIsActive(), user.getIsOnboarded()))
                    .collect(Collectors.toList()));
        }
//        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), user.getDepartment(), user.getRole(), user.getIsActive(), user.getIsOnboarded());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getStudentId() ,user.getRole(), user.getIsActive());

        UserToken userToken = UserToken.builder().user(user).refreshToken(refreshToken).build();

        if (userTokenRepository.existsByUser_Id(user.getId())) {
            userTokenRepository.updateRefreshToken(user.getId(), refreshToken);
        } else {
            userTokenRepository.save(userToken);
        }

        return LoginResponseDto.of(accessTokens, refreshToken, user.getIsOnboarded(), user.getIsActive());
    }

    @Transactional
    public UserInfoResponseDto onBoardUser(UserAccount userAccount, OnBoardingRequestDto onBoardingRequestDto) {
        User foundUser = userRepository.findUserById(userAccount.getUserId()).orElseThrow(
                () -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당하는 사용자를 찾을 수 없습니다.")
        );

//        Department department = departmentRepository
//                .findByName(onBoardingRequestDto.getDepartment())
//                .orElseThrow(
//                        () -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당하는 학부를 찾을 수 없습니다.")
//                );
//
//        foundUser.updateOnBoardingInfo(onBoardingRequestDto.getGender(), department, onBoardingRequestDto.getImageLink());
        onBoardingRequestDto.getDepartments()
                .forEach(s -> {
                    Department department = departmentRepository.findByName(s).orElseThrow(
                            () -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당하는 학부를 찾을 수 없습니다.")
                    );
                    userDepartmentRepository.save(UserDepartment.builder()
                                    .user(foundUser)
                                    .department(department)
                                    .build());
                });
        foundUser.updateOnBoardingInfo(onBoardingRequestDto.getGender(), onBoardingRequestDto.getImageLink());

        return UserInfoResponseDto.from(userRepository.save(foundUser));
    }

    @Transactional
    public RegenerateAccessTokenResponseDto regenerateAccessToken(RegenerateAccessTokenRequestDto requestDto) {
        List<String> accessTokens = new ArrayList<>();
        UserToken userToken = userTokenRepository.findByRefreshToken(requestDto.getRefreshToken()).orElseThrow(
                () -> NotFoundException.builder().message("해당 유저를 찾을 수 없습니다.").httpStatus(HttpStatus.UNAUTHORIZED).build()
        );

        User user = userToken.getUser();
        String mainAccessTokens = user.getDepartments().stream()
                .filter(userDepartment -> userDepartment.getIsMain().equals(Boolean.TRUE))
                .map(UserDepartment::getDepartment)
                .map(department -> jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), department, user.getRole(), user.getIsActive(), user.getIsOnboarded()))
                .collect(Collectors.toList()).get(0);
        accessTokens.add(mainAccessTokens);
        if (user.getDepartments().size() > 1) {
            accessTokens.addAll(user.getDepartments().stream()
                    .filter(userDepartment -> userDepartment.getIsMain().equals(Boolean.FALSE))
                    .map(UserDepartment::getDepartment)
                    .map(department -> jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), department, user.getRole(), user.getIsActive(), user.getIsOnboarded()))
                    .collect(Collectors.toList()));
        }
//        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getStudentId(), user.getDepartment(), user.getRole(), user.getIsActive(), user.getIsOnboarded());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getStudentId(), user.getRole(), user.getIsActive());

        userTokenRepository.updateRefreshToken(user.getId(), refreshToken);

        return RegenerateAccessTokenResponseDto.of(accessTokens, refreshToken);
    }

    public UserInfoResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        User user = User.builder()
                .studentId(signUpRequestDto.getStudentId())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .name(signUpRequestDto.getName())
                .build();
        if (userRepository.existsByStudentId(signUpRequestDto.getStudentId())) {
            throw UserInputException.builder().message("이미 존재하는 학번입니다.").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        User savedUser = userRepository.save(user);

        return UserInfoResponseDto.from(savedUser);
    }
}
